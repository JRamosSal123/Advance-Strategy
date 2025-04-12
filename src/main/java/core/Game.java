package core;

import map.Board;
import player.Player;
import units.Unit;
import utils.ColorAux;
import static com.raylib.Colors.*;
import static com.raylib.Raylib.*;
import static com.raylib.Raylib.GetKeyPressed;
import java.util.ArrayList;
import java.util.List;


public class Game{
    private final int screenWidth = 800;
    private final int screenHeight = 480;
    private final String gameTitle = "Advance Strategy";
    private final int fps = 60;
    private final String mapPath = "assets/Map.CSV";

    private final ColorAux areaColor = new ColorAux(102,191,225,150);

    private Board board;
    private Player player;
    private Player opponent;
    private Vector2 positionMouse;
    private Unit unitSelect;
    private Unit unitMove;


    public void main() {
        CreateWindow();
        startGame();
    }

    public int getScreenWidth() {
        return this.screenWidth;
    }
    public int getScreenHeight() {
        return this.screenHeight;
    }
    public int getFps() {
        return this.fps;
    }
    public String getMapPath() {
        return this.mapPath;
    }

    private void CreateWindow() {
        InitWindow(screenWidth, screenHeight, gameTitle);
        SetConfigFlags(FLAG_MSAA_4X_HINT);
        InitAudioDevice();

        SetTargetFPS(fps);
    }

    private void initGame(){
        board = new Board(this);
        player = new Player(GREEN);
        player.loadUnits();
        opponent = new Player(BEIGE);
        opponent.loadUnits1();
        unitSelect = null;
        unitMove = null;
    }

    private void startGame(){

        initGame();
        int framesCounter = 0;

        boolean pause = false;
        boolean mouse2 = false;

        BeginDrawing();
        Music music = LoadMusicStream("assets/sound/music.mp3");
        PlayAudioStream(music.stream());


        while (!WindowShouldClose()){

            int key = GetKeyPressed();
            //if(key != 0){System.out.println(key);}
            if (key == 80){
                pause = !pause;
                key = 0;
            }

            ClearBackground(RAYWHITE);
            UpdateMusicStream(music);

            if(!pause) {

                //Control de Unidades con el ratón (clic izquierdo, seleccionar y move; clic derecho, deseliccionar)
                if (IsMouseButtonPressed(1)) {
                    unitSelect = null;
                }

                if (IsMouseButtonPressed(0) && unitSelect != null) {
                    mouse2 = true;
                    positionMouse = GetMousePosition();
                    unitMove = unitSelect;
                    unitSelect = null;
                }

                if (mouse2 && framesCounter % 60 == 0 && unitMove != null) {
                    mouse2 = !unitMove.move(normaliceCord((int) positionMouse.x()), normaliceCord((int) positionMouse.y()));
                    if(!mouse2){unitMove = null;}
                }

                if (IsMouseButtonPressed(0) && unitSelect == null) {
                    positionMouse = GetMousePosition();
                    unitSelect = player.searchUnit(normaliceCord((int) positionMouse.x()),normaliceCord((int) positionMouse.y()));

                    if(unitSelect != null){
                        List<Unit> allUnits = new ArrayList<>();
                        allUnits.addAll(player.getUnits());
                        allUnits.addAll(opponent.getUnits());

                        unitSelect.removeVectorPosibleMovement(allUnits);
                    }
                }

                framesCounter++;
            }
            DrawFrame(board, player, opponent, unitSelect, pause);
        }
        unloadAndClose(music);
    }

    private void UpdateFrame(){

    }

    private void DrawFrame(Board board, Player player, Player enemigo, Unit unitSelect, boolean pause){
        board.drawMap();
        player.drawUnits(player.getUnits());
        enemigo.drawUnits(enemigo.getUnits());

        if (unitSelect != null){
            unitSelect.drawArea(areaColor.toRaylibColor());
        }
        if(pause){
            ColorAux colorAux = new ColorAux(0,0,0,100);
            DrawRectangle(0,0,800,480,colorAux.toRaylibColor());
            DrawText("Pausa",318,190,50,BLACK);
        }
        EndDrawing();

    }

    private void unloadAndClose(Music music){
        unload(music);
        close();
    }

    private void unload(Music music){
        UnloadMusicStream(music);
    }

    private void close(){
        CloseAudioDevice();
        CloseWindow();
    }

    private int normaliceCord(int cord){
        return (cord/32)*32;
    }
}
