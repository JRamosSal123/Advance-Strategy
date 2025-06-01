package IA;

import core.Game;
import units.Troop;
import units.Unit;
import utils.ColorEnum;
import java.io.IOException;
import static com.raylib.Raylib.*;

public class GameIA extends Game {

    private int difficulty;
    int framesCounter = 0;
    boolean mouseClicked = false;
    int tx = 0, ty = 0;
    boolean iaMove = true;

    public GameIA(int difficulty) {
        super();
        this.difficulty = difficulty;
    }

    public void main() throws IOException {
        startGame();
    }

    public void startGame() {
        initGame();
        isMyTurn = true;
        Music music = setupMusic();
        BeginDrawing();
        ClearBackground(ColorEnum.WHITE.getColorUtils().toRaylibColor());

        while (!WindowShouldClose() && !isFinished) {
            UpdateMusicStream(music);
            pause();
            endTurn();
            checkVictoryConditions();

            if (!isPause && isMyTurn) {
                handleMouseInput();
                clearUnits();
            }
            if (!isPause && !isMyTurn) {
                if(iaMove) {
                    MinMax minMax = new MinMax(player, opponent, difficulty, board);
                    String msg = minMax.minmax();
                    processMessage(msg);
                    iaMove = false;
                    if(msg.startsWith("ATTACK:")){
                        iaMove = true;
                    }

                }
                if(!isMyTurn && enemyUnit != null){
                    boolean inMove = !enemyUnit.move(toX, toY, allUnits, board);
                    if(!inMove){
                        toX = -1;
                        toY = -1;
                        isMyTurn = true;
                        iaMove = true;
                    }
                }
            }
            framesCounter++;
            DrawFrame();
        }

        isMyTurn = false;
        unload(music);

        while (!WindowShouldClose() && !finished.isClicked(GetMousePosition()) && isFinished) {
            DrawFrame();
        }
    }

    private void handleMouseInput() {
        if (IsMouseButtonPressed(1)) {
            unitSelect = null;
        }

        if (IsMouseButtonPressed(0)) {
            if (unitSelect != null && unitMove == null) {
                Vector2 mousePos = GetMousePosition();
                positionMouseUnit = mousePos;
                int targetX = normaliceCord((int) mousePos.x());
                int targetY = normaliceCord((int) mousePos.y());

                if (attemptAttack(targetX, targetY)) return;

                prepareUnitMovement(mousePos);
            } else if (unitSelect == null && unitMove == null) {
                selectUnit();
            }
        }
        updateUnitMovement();
    }

    private boolean attemptAttack(int targetX, int targetY) {
        for (Vector2 attackPos : unitSelect.getPosibleAttack()) {
            if (targetX == attackPos.x() && targetY == attackPos.y()) {
                attack();
                clearUnits();
                return true;
            }
        }
        return false;
    }

    private void prepareUnitMovement(Vector2 mousePos) {
        mouseClicked = true;
        positionMouseUnit = mousePos;
        unitMove = unitSelect;
        unitSelect = null;

        tx = (int) unitMove.getPosition().x();
        ty = (int) unitMove.getPosition().y();
    }

    private void updateUnitMovement() {
        if (mouseClicked && framesCounter % 60 == 0 && unitMove != null) {
            int targetX = normaliceCord((int) positionMouseUnit.x());
            int targetY = normaliceCord((int) positionMouseUnit.y());

            if (!isValid) {
                isValid = unitMove.validMovement(targetX, targetY);
            }

            mouseClicked = !unitMove.move(targetX, targetY, allUnits, board);

            if (!mouseClicked) {
                if (isValid) {
                    unitsUsed.add(unitMove);
                    isValid = false;
                }
                unitMove = null;
            }
        }
    }

    private void selectUnit() {
        positionMouseUnit = GetMousePosition();
        Unit unit;
        unit = player.searchUnitByPos(normaliceCord((int) positionMouseUnit.x()),normaliceCord((int) positionMouseUnit.y()));

        if(!unitsUsed.contains(unit)){
            if(unit instanceof Troop){
                unitSelect = (Troop) unit;
            }
        }

        if(unitSelect != null){
            unitSelect.removeVectorPosibleMovement(allUnits);
            unitSelect.removeVectorAttack(opponent.getUnits());
            unitSelect.checkArea(allUnits,board);
        }
    }

    private void checkVictoryConditions() {
        if(player.isLoser()){
            isLose = true;
            isFinished = true;
        }

        if(opponent.isLoser()){
            isWin = true;
            isFinished = true;
        }
    }

    private void endTurn() {
        if(isMyTurn && next.isClicked(GetMousePosition()) && unitSelect == null && unitMove == null){
            isMyTurn = !isMyTurn;
            unitsUsed.clear();
        }
    }

    private void pause() {
        int key = GetKeyPressed();
        if (key == 80){
            isPause = !isPause;
        }
    }

    private void attack(){
        int targetX = normaliceCord((int) positionMouseUnit.x());
        int targetY = normaliceCord((int) positionMouseUnit.y());

        for (Vector2 v : unitSelect.getPosibleAttack()) {
            if ((int) v.x() == targetX && (int) v.y() == targetY) {
                Unit target = opponent.searchUnitByPos(targetX, targetY);

                if (target != null) {
                    int defense = board.getTerrain((int) target.getPosition().y(), (int) target.getPosition().x()).getTerrainEnum().getDefenseLevel();
                    unitSelect.attack(target, defense);
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
            enemyUnit = (Troop) opponent.searchUnitByPos(fromX, fromY);

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

            Troop troop = (Troop) opponent.searchUnitByPos(xRecive, yRecive);
            troop.attack(player.searchUnitByPos(xAttack,yAttack), board.getTerrain(normaliceCord(yAttack), normaliceCord(xAttack)).getTerrainEnum().getDefenseLevel());

            clearUnits();

        } else if (msg.equals("END")) {
            isMyTurn = true;
            isFinished = true;

        }
    }
}

