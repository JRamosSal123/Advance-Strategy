package P2P;

import core.Game;
import player.Player;
import units.Troop;
import units.Unit;
import utils.ColorEnum;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import static com.raylib.Raylib.*;

public class GameP2P extends Game {

    private final String ip;
    private final int port;
    private P2PConnection connection;
    private boolean isServer;

    public GameP2P(String ip, int port) {
        this(ip, port, false);
    }

    public GameP2P(int port) {
        this("", port, true);
    }

    private GameP2P(String ip, int port, boolean isServer) {
        super();
        this.ip = ip;
        this.port = port;
        this.isServer = isServer;
    }

    public void main() throws IOException {

        setupConnection();

        startReceiverThread();

        startGame();
    }

    private void setupConnection() throws IOException {
        connection = new P2PConnection();
        if (isServer) {
            connection.connectAsServer(port);
            isMyTurn = true;
        } else {
            connection.connectAsClient(ip, port);
            isMyTurn = false;
        }
        messageQueue = new ConcurrentLinkedQueue<>();
    }

    private void startReceiverThread() {
        Thread receiverThread = new Thread(() -> {
            while (true) {
                try {
                    String msg = connection.receive();
                    if (msg != null) messageQueue.add(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        });
        receiverThread.setDaemon(true);
        receiverThread.start();
    }

    public void startGame(){

        initGame();
        Music music = setupMusic();
        BeginDrawing();
        ClearBackground(ColorEnum.WHITE.getColorUtils().toRaylibColor());

        int framesCounter = 0;

        boolean mouse = false;
        int tx = 0;
        int ty = 0;

        while (!WindowShouldClose() && !isFinished){
            UpdateMusicStream(music);

            pause();

            endTurn();

            processIncomingMessages(framesCounter);

            checkVictoryConditions();

            if(!isPause && isMyTurn) {
                if (IsMouseButtonPressed(1)) {
                    unitSelect = null;
                }

                if (IsMouseButtonPressed(0) && unitSelect != null && unitMove == null) {
                    positionMouseUnit = GetMousePosition();
                    int targetX = normaliceCord((int) positionMouseUnit.x());
                    int targetY = normaliceCord((int) positionMouseUnit.y());
                    boolean isAttack = false;
                    for(Vector2 v : unitSelect.getPosibleAttack()){
                        if(targetX == v.x() && targetY == v.y()){
                            isAttack = true;
                            attack();
                            clearUnits();
                            break;
                        }
                    }
                    if(!isAttack){

                        mouse = true;
                        positionMouseUnit = GetMousePosition();
                        unitMove = unitSelect;
                        unitSelect = null;

                        tx = (int) unitMove.getPosition().x();
                        ty = (int) unitMove.getPosition().y();
                    }

                }

                if (mouse && framesCounter % 60 == 0 && unitMove != null) {
                    if(!isValid) isValid = unitMove.validMovement(normaliceCord((int) positionMouseUnit.x()), normaliceCord((int) positionMouseUnit.y()));
                    mouse = !unitMove.move(normaliceCord((int) positionMouseUnit.x()), normaliceCord((int) positionMouseUnit.y()), allUnits, board);
                    connection.send("MOVE:" + tx + ":" + ty + ":" + normaliceCord((int) positionMouseUnit.x()) + ":" + normaliceCord((int) positionMouseUnit.y()));

                    if(!mouse){
                        if(isValid) {
                            unitsUsed.add(unitMove);
                            isValid = false;
                        }
                        unitMove = null;
                    }
                }

                if (IsMouseButtonPressed(0) && unitSelect == null && unitMove == null) {
                    selectUnit();
                }

                if(!connection.isConnected()){
                    isFinished = true;
                }

            }
            framesCounter++;
            DrawFrame();
        }

        connection.send("END");
        isMyTurn = false;
        connection.close();
        unload(music);

    while (!WindowShouldClose() && !finished.isClicked(GetMousePosition()) && isFinished) {
            DrawFrame();
        }
    }

    private void selectUnit() {
        positionMouseUnit = GetMousePosition();
        Unit unit;
        if(isServer){
            unit = player.searchUnitByPos(normaliceCord((int) positionMouseUnit.x()),normaliceCord((int) positionMouseUnit.y()));
        }
        else{
            unit = opponent.searchUnitByPos(normaliceCord((int) positionMouseUnit.x()),normaliceCord((int) positionMouseUnit.y()));
        }

        if(!unitsUsed.contains(unit)){
            if(unit instanceof Troop){
                unitSelect = (Troop) unit;
            }
        }

        if(unitSelect != null){
            unitSelect.checkArea(allUnits,board);
            unitSelect.removeVectorPosibleMovement(allUnits);
            unitSelect.removeVectorAttack(isServer ? opponent.getUnits() : player.getUnits());
        }
    }

    private void checkVictoryConditions() {
        if(player.isLoser()){
            if(isServer){
                isLose = true;
            }else {
                isWin = true;
            }
            connection.send("END_TURN");
            isFinished = true;
        }

        if(opponent.isLoser()){
            if(isServer){
                isWin = true;
            }else {
                isLose = true;
            }
            connection.send("END_TURN");
            isFinished = true;
        }
    }

    private void processIncomingMessages(int framesCounter) {
        while (!messageQueue.isEmpty()) {
            processMessage(messageQueue.poll());

            //Mover unidad enemiga fuera de turno
            if(!isMyTurn && enemyUnit != null){
                boolean inMove = !enemyUnit.move(toX, toY, allUnits, board);
                if(!inMove){
                    toX = -1;
                    toY = -1;
                }
            }
            framesCounter++;
            DrawFrame();
        }
    }

    private void endTurn() {
        if(isMyTurn && next.isClicked(GetMousePosition()) && unitSelect == null && unitMove == null){
            isMyTurn = !isMyTurn;
            Player troop = isServer ? player : opponent;
            for (Unit unit : troop.getUnits()) {
                if (unit instanceof Troop) {
                    String msg = "SYNC:" + unit.getId() + ":" + (int) unit.getPosition().x() + ":" + (int) unit.getPosition().y();
                    connection.send(msg);
                    //System.out.println(msg);
                }
            }
            connection.send("END_TURN");
            unitsUsed.clear();
        }
    }

    private void pause() {
        int key = GetKeyPressed();
        if (key == 80){
            isPause = !isPause;
            connection.send("PAUSE_KEY");
        }
    }

    private void attack(){
        int targetX = normaliceCord((int) positionMouseUnit.x());
        int targetY = normaliceCord((int) positionMouseUnit.y());

        // Verifica si la posición clicada está en el rango de ataque
        for (Vector2 v : unitSelect.getPosibleAttack()) {
            if ((int) v.x() == targetX && (int) v.y() == targetY) {
                Unit target = isServer
                        ? opponent.searchUnitByPos(targetX, targetY)
                        : player.searchUnitByPos(targetX, targetY);

                if (target != null) {
                    int defense = board.getTerrain((int) target.getPosition().y(), (int) target.getPosition().x()).getTerrainEnum().getDefenseLevel();
                    unitSelect.attack(target, defense);
                    connection.send("ATTACK:" + targetX + ":" + targetY + ":" + (int)unitSelect.getPosition().x() + ":" + (int)unitSelect.getPosition().y());
                    unitsUsed.add(unitSelect);
                    unitSelect = null;
                    break;
                }
            }
        }
    }

    private void processMessage(String msg) {
        if (msg.startsWith("MOVE:") && toX == -1 && toY == -1) {
            String[] parts = msg.split(":");
            int fromX = Integer.parseInt(parts[1]);
            int fromY = Integer.parseInt(parts[2]);
            toX = Integer.parseInt(parts[3]);
            toY = Integer.parseInt(parts[4]);

            if(!isServer){
                enemyUnit = (Troop) player.searchUnitByPos(fromX, fromY);
            }else{
                enemyUnit = (Troop) opponent.searchUnitByPos(fromX, fromY);
            }

        } else if (msg.equals("END_TURN")) {
            isMyTurn = true;

        } else if (msg.equals("PAUSE_KEY")) {
            isPause = !isPause;

        } else if (msg.startsWith("ATTACK:")) {
            String[] parts = msg.split(":");
            int xAttack = Integer.parseInt(parts[1]);
            int yAttack = Integer.parseInt(parts[2]);
            int xRecive = Integer.parseInt(parts[3]);
            int yRecive = Integer.parseInt(parts[4]);

            if(isServer){
                Troop troop = (Troop) opponent.searchUnitByPos(xRecive, yRecive);
                troop.attack(player.searchUnitByPos(xAttack,yAttack), board.getTerrain(normaliceCord(yAttack), normaliceCord(xAttack)).getTerrainEnum().getDefenseLevel());
            }else {
                Troop troop = (Troop) player.searchUnitByPos(xRecive, yRecive);
                troop.attack(opponent.searchUnitByPos(xAttack,yAttack), board.getTerrain(normaliceCord(yAttack), normaliceCord(xAttack)).getTerrainEnum().getDefenseLevel());
            }

            clearUnits();

        } else if (msg.equals("END")) {
            isMyTurn = true;
            isFinished = true;

        }else if (msg.startsWith("SYNC:")) {
            //System.out.println(msg);
            String[] parts = msg.split(":");
            int id = Integer.parseInt(parts[1]);
            int x = Integer.parseInt(parts[2]);
            int y = Integer.parseInt(parts[3]);

            // Actualizar la posición de la unidad del oponente
            List<Unit> targetUnits = isServer ? player.getUnits() : opponent.getUnits() ;
            for (Unit unit : targetUnits) {
                if (unit instanceof Troop && unit.getId() == id) {
                    unit.setPosition(x,y);
                    break;
                }
            }
        }
    }
}
