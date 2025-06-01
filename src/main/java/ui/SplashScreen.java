package ui;

import utils.ColorEnum;
import utils.ColorUtils;
import static com.raylib.Raylib.*;

public class SplashScreen {
    private final ColorUtils white = new ColorUtils(255,255,255,0);

    private final String path = "assets/screen/";

    private final Texture logo = LoadTexture(path + "SplashScreen.png");

    public void drawSplashScreen(int alpha) {
        if(alpha > 255){
            alpha = 255 - (alpha - 255);
        }
        white.setAlpha(alpha);
        ClearBackground(ColorEnum.WHITE.getColorUtils().toRaylibColor());
        BeginDrawing();
        DrawRectangleGradientV(0,0,800,480,ColorEnum.GREY_TOP.getColorUtils().toRaylibColor() , ColorEnum.GREY_BOTTOM.getColorUtils().toRaylibColor());
        DrawTexture(logo,0,0,white.toRaylibColor());
        EndDrawing();
    }

}
