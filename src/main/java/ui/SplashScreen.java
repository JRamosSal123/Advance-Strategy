package ui;

import utils.ColorUtils;

import static com.raylib.Raylib.*;

public class SplashScreen {
    private final ColorUtils raywhite = new ColorUtils(245, 245, 245);
    private final ColorUtils greyUp = new ColorUtils(65,65,65);
    private final ColorUtils greyDown = new ColorUtils(40,40,40);
    private final ColorUtils white = new ColorUtils(255,255,255,0);

    private final String path = "assets/screen/splash/";

    private final Texture logo = LoadTexture(path + "SplashScreen.png");

    public void drawSplashScreen(int alpha) {
        if(alpha > 255){
            alpha = 255 - (alpha - 255);
        }
        white.setAlpha(alpha);
        ClearBackground(raywhite.toRaylibColor());
        BeginDrawing();
        DrawRectangleGradientV(0,0,800,480,greyUp.toRaylibColor() , greyDown.toRaylibColor());
        DrawTexture(logo,0,0,white.toRaylibColor());
        EndDrawing();
    }

}
