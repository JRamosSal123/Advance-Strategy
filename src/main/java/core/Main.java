package core;

import IA.GameIA;
import P2P.GameP2P;
import ui.*;

import java.io.IOException;
import java.util.Objects;

import static com.raylib.Raylib.*;
import static com.raylib.Raylib.SetTargetFPS;

public class Main {
    private static final int screenWidth = 800;
    private static final int screenHeight = 480;
    private static final String gameTitle = "Advance Strategy";
    private static final int fps = 60;
    private static final String mapPath = "assets/Map.CSV";
    private static final String musicPath = "assets/sound/music.mp3";
    private static final String iconPath = "assets/screen/icon.png";
    private static String ip;
    private static int port;
    private static int difficulty = 0;


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

                    MainScreen mainScreen = new MainScreen();
                    currentScreen = mainScreen.run();
                    break;

                case DIFFICULTY_IA:
                    //Draw difficulty menu IA
                    ConfigAIScreen aiScreen = new ConfigAIScreen();
                    difficulty = aiScreen.runScreen();
                    if(difficulty == 0){
                        currentScreen = Screen.MAIN;
                    }
                    else{
                        currentScreen = Screen.GAME_IA;
                    }
                    break;

                case GAME_IA:
                    if(difficulty != 0){
                        GameIA gameIA = new GameIA(difficulty);
                        gameIA.main();
                    }

                    currentScreen = Screen.MAIN;
                    break;

                case CONFIG_P2P:
                    //Draw config menu
                    ConfigP2PScreen p2pScreen = new ConfigP2PScreen();
                    p2pScreen.runScreen();
                    ip = p2pScreen.getIp();
                    port = p2pScreen.getPort();
                    if(Objects.equals(ip, "" ) && port == -1){
                        currentScreen = Screen.MAIN;
                    }else {
                        currentScreen = Screen.GAME_P2P;
                    }
                    break;

                case GAME_P2P:
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
        SetWindowIcon(LoadImage(iconPath));
        SetConfigFlags(FLAG_MSAA_4X_HINT);
        InitAudioDevice();
        SetTargetFPS(fps);
    }

    private static void unloadAndClose() {

        CloseAudioDevice();
        CloseWindow();
    }
}
