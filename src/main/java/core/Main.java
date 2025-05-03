package core;

import P2P.GameP2P;
import ui.*;

import java.io.IOException;
import java.util.Objects;

import static com.raylib.Colors.*;
import static com.raylib.Raylib.*;
import static com.raylib.Raylib.SetTargetFPS;

public class Main {
    private static final int screenWidth = 800;
    private static final int screenHeight = 480;
    private static final String gameTitle = "Advance Strategy";
    private static final int fps = 60;
    private static final String mapPath = "assets/Map.CSV";
    private static final String musicPath = "assets/sound/music.mp3";
    private static String ip;
    private static int port;


    public static void main(String[] args) throws IOException {
        createWindow();

        Screen currentScreen = Screen.SPlASH;
        int frameCounter = 0;
        int alpha = 0;

        while (!WindowShouldClose()){
            switch (currentScreen){
                case SPlASH:
                    //Draw SplasScreen
                    if (frameCounter < 500) {
                        SplashScreen splashScreen = new SplashScreen();
                        splashScreen.drawSplashScreen(alpha);
                        alpha++;
                    }
                    else{
                        frameCounter = 0;
                        currentScreen = Screen.MAIN;
                    }

                    frameCounter++;
                    break;

                case MAIN:

                    MainScreen test = new MainScreen();
                    currentScreen = test.run();
                    break;

                case DIFFICULTY_IA:
                    //Draw difficulty menu IA
                    System.out.println("DIFFICULTY_IA");
                    currentScreen = Screen.SPlASH;
                    break;

                case CONFIG_P2P:
                    //Draw config menu
                    ConfigP2PScreen ConfigP2PScreen = new ConfigP2PScreen();
                    ConfigP2PScreen.runScreen();
                    ip = ConfigP2PScreen.getIp();
                    port = ConfigP2PScreen.getPort();
                    if(Objects.equals(ip, "" ) && port == -1){
                        currentScreen = Screen.MAIN;
                    }else {
                        currentScreen = Screen.GAME;
                    }
                    break;

                case GAME:
                    GameP2P gameP2P;
                    if(Objects.equals(ip, "")){
                        gameP2P = new GameP2P(port);
                    }
                    else{
                        gameP2P = new GameP2P(ip, port);
                    }
                    gameP2P.main();

                    currentScreen = Screen.MAIN;
                    break;

                default:
                    break;
            }
            //drawFrame();
        }
        unloadAndClose();

    }

    public static int getScreenWidth() {
        return screenWidth;
    }
    public static int getScreenHeight() {
        return screenHeight;
    }
    public static int getFps() {
        return fps;
    }
    public static String getMapPath() {
        return mapPath;
    }
    public static String getMusicPath() {
        return musicPath;
    }

    private static void createWindow() {
        InitWindow(screenWidth, screenHeight, gameTitle);
        SetConfigFlags(FLAG_MSAA_4X_HINT);
        InitAudioDevice();
        SetTargetFPS(fps);
    }

    private static void drawFrame(){
        ClearBackground(RAYWHITE);
        BeginDrawing();
        //Draws Here

        EndDrawing();
    }

    private static void unloadAndClose() {
        //UnloadMusicStream(music);

        CloseAudioDevice();
        CloseWindow();
    }
}
