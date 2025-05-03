package P2P;

import com.raylib.Raylib;
import core.Main;
import map.Board;
import player.Player;
import ui.Button;
import units.Troop;
import units.Unit;
import utils.ColorUtils;
import utils.RectangleUtils;
import utils.ScreenUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.raylib.Raylib.*;
import static com.raylib.Raylib.DrawRectangleRounded;


public class GameP2P {
    private final ColorUtils areaColor = new ColorUtils(102,191,225,150);
    private final ColorUtils enemyColor = new ColorUtils(255,0,0,150);
    private final ColorUtils black = new ColorUtils(0,0,0);
    private final ColorUtils greyLight = new ColorUtils(200, 200, 200);
    private final ColorUtils greyDark = new ColorUtils(150, 150, 150);
    private final ColorUtils white = new ColorUtils(255,255,255);

    private Board board;
    private Player player;
    private Player opponent;
    private Vector2 positionMouseUnit;
    private Troop unitSelect;
    private Troop unitMove;
    private boolean isMyTurn;

    private final String ip;
    private final int port;

    private P2PConnection connection;
    private boolean isServer;
    private ConcurrentLinkedQueue<String> messageQueue;

    private Troop enemyUnit;
    private int toX = -1;
    private int toY = -1;

    Button next;
    Button finished;

    boolean isPause = false;
    boolean isFinished = false;
    boolean isWin = false;
    boolean isLose = false;
    boolean isValid = false;

    List<Unit> unitsUsed = new ArrayList<>();

    public GameP2P(String ip, int port) {
        this(ip, port, false);
    }

    public GameP2P(int port) {
        this("", port, true);
    }

    private GameP2P(String ip, int port, boolean isServer) {
        this.ip = ip;
        this.port = port;
        this.isServer = isServer;
        this.next = new Button(
                new RectangleUtils(730,420,60,50),
                greyLight,
                greyDark,
                "Next",
                20,
                black
        );
        this.finished = new Button(
                new RectangleUtils(300,300,220,70),
                greyLight,
                greyDark,
                "Finished",
                50,
                black
        );
    }

    public void main() throws IOException {

        // Conectarse como cliente o servidor antes de iniciar el juego
        setupConnection();

        // Hilo para escuchar mensajes entrantes
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
        ClearBackground(white.toRaylibColor());

        int framesCounter = 0;

        boolean mouse = false;
        int tx = 0;
        int ty = 0;

        while (!WindowShouldClose() && !isFinished){
            //float deltaTime = GetFrameTime();
            UpdateMusicStream(music);
            handleInput();

            pause();

            endTurn();

            // Procesar mensajes entrantes
            processIncomingMessages(framesCounter);

            checkVictoryConditions();

            if(!isPause && isMyTurn) {

                //Control de Unidades con el ratón (clic izquierdo, seleccionar y move; clic derecho, deseliccionar)

                //Deselecionar una unidad
                if (IsMouseButtonPressed(1)) {
                    //unitsUsed.remove(unitSelect);
                    unitSelect = null;
                }

                //Seleccionar posicion a la que mover la unidad
                if (IsMouseButtonPressed(0) && unitSelect != null && unitMove == null) {
                    positionMouseUnit = GetMousePosition();
                    int targetX = normaliceCord((int) positionMouseUnit.x());
                    int targetY = normaliceCord((int) positionMouseUnit.y());
                    boolean isAttack = false;
                    for(Vector2 v : unitSelect.getPosibleAttack()){
                        if(targetX == v.x() && targetY == v.y()){
                            isAttack = true;
                            attack();
                            player.clearDeathUnit();
                            opponent.clearDeathUnit();
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

                //Mover unidad
                if (mouse && framesCounter % 60 == 0 && unitMove != null) {
                    if(!isValid) isValid = unitMove.validMovement(normaliceCord((int) positionMouseUnit.x()), normaliceCord((int) positionMouseUnit.y()));
                    mouse = !unitMove.move(normaliceCord((int) positionMouseUnit.x()), normaliceCord((int) positionMouseUnit.y()));
                    connection.send("MOVE:" + tx + ":" + ty + ":" + normaliceCord((int) positionMouseUnit.x()) + ":" + normaliceCord((int) positionMouseUnit.y()));

                    if(!mouse){
                        if(isValid) {
                            unitsUsed.add(unitMove);
                            isValid = false;
                        }
                        unitMove = null;
                    }
                }

                //Seleccionar unidad
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
            unit = player.searchUnit(normaliceCord((int) positionMouseUnit.x()),normaliceCord((int) positionMouseUnit.y()));
        }
        else{
            unit = opponent.searchUnit(normaliceCord((int) positionMouseUnit.x()),normaliceCord((int) positionMouseUnit.y()));
        }

        if(!unitsUsed.contains(unit)){
            if(unit instanceof Troop){
                unitSelect = (Troop) unit;
            }
        }

        if(unitSelect != null){
            List<Unit> allUnits = new ArrayList<>();
            allUnits.addAll(player.getUnits());
            allUnits.addAll(opponent.getUnits());

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
                boolean inMove = !enemyUnit.move(toX, toY);
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

    private void handleInput() {

    }

    private void initGame(){
        board = new Board(this);
        player = new Player(new ColorUtils(0, 228, 48), Raylib::LoadTexture);
        player.loadUnitsPlayer();
        opponent = new Player(new ColorUtils(211, 176, 131), Raylib::LoadTexture);
        opponent.loadUnitsOpponent();
        unitSelect = null;
        unitMove = null;
    }

    private Music setupMusic(){
        Music music = Raylib.LoadMusicStream(Main.getMusicPath());
        PlayAudioStream(music.stream());
        SetMusicVolume(music,0.5F);
        return music;
    }

    private void attack(){
        int targetX = normaliceCord((int) positionMouseUnit.x());
        int targetY = normaliceCord((int) positionMouseUnit.y());

        // Verifica si la posición clicada está en el rango de ataque
        for (Vector2 v : unitSelect.getPosibleAttack()) {
            if ((int) v.x() == targetX && (int) v.y() == targetY) {
                Unit target = isServer
                        ? opponent.searchUnit(targetX, targetY)
                        : player.searchUnit(targetX, targetY);

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
                enemyUnit = (Troop) player.searchUnit(fromX, fromY);
            }else{
                enemyUnit = (Troop) opponent.searchUnit(fromX, fromY);
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
                Troop troop = (Troop) opponent.searchUnit(xRecive, yRecive);
                troop.attack(player.searchUnit(xAttack,yAttack), board.getTerrain(normaliceCord(yAttack), normaliceCord(xAttack)).getTerrainEnum().getDefenseLevel());
            }else {
                Troop troop = (Troop) player.searchUnit(xRecive, yRecive);
                troop.attack(opponent.searchUnit(xAttack,yAttack), board.getTerrain(normaliceCord(yAttack), normaliceCord(xAttack)).getTerrainEnum().getDefenseLevel());
            }

            player.clearDeathUnit();
            opponent.clearDeathUnit();

        } else if (msg.equals("END")) {
            isMyTurn = true;
            isFinished = true;

        }
    }

    public void DrawFrame(){
        board.drawMap();
        player.drawUnits(player.getUnits());
        opponent.drawUnits(opponent.getUnits());

        if(unitSelect == null && unitMove == null && isMyTurn){
            next.update(GetMousePosition());
            next.draw();
        }
        if(isFinished){
            finished.update(GetMousePosition());
            finished.draw();
        }

        if (unitSelect != null){
            unitSelect.drawArea(areaColor, enemyColor);

            RectangleUtils rectContain = new RectangleUtils(5,5,70,50);
            DrawRectangleRounded(rectContain.toRaylibRectangle(), 0.5F, 100, greyDark.toRaylibColor());
            RectangleUtils rect = new RectangleUtils(10,10,60,40);
            DrawRectangleRounded(rect.toRaylibRectangle(), 0.5F, 100, greyLight.toRaylibColor());

            DrawText(unitSelect.getUnitEnum().getName(),12,15, 10,black.toRaylibColor());
            DrawText("Life: " + unitSelect.getLife(),12,25, 10,black.toRaylibColor());
            DrawText("Attack: " + unitSelect.getUnitEnum().getDamage(),12,35, 10,black.toRaylibColor());
        }

        if(isWin) ScreenUtils.drawOverlay("WIN");
        if(isLose) ScreenUtils.drawOverlay("LOSE");
        if(!isWin && !isLose && isFinished) ScreenUtils.drawOverlay("Rival Abandoned");
        if(isPause) ScreenUtils.drawOverlay("PAUSE");

        EndDrawing();

    }

    private void unload(Music music){
        UnloadMusicStream(music);
    }

    private int normaliceCord(int cord){
        return (cord/32)*32;
    }

}
