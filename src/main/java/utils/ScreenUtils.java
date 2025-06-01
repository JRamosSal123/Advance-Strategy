package utils;

import core.Main;
import static com.raylib.Raylib.*;

public class ScreenUtils {

    public static boolean isMouseOver(RectangleUtils rect, Vector2 pos) {
        if (pos == null) return false;
        return pos.x() >= rect.getPositionX() && pos.x() <= rect.getPositionX() + rect.getWidth() &&
                pos.y() >= rect.getPositionY() && pos.y() <= rect.getPositionY() + rect.getHeight();
    }

    public static void startFrame(ColorUtils backgroundTop, ColorUtils backgroundBottom) {
        ClearBackground(backgroundTop.toRaylibColor());
        BeginDrawing();
        DrawRectangleGradientV(0, 0, Main.getScreenWidth(), Main.getScreenHeight(), backgroundTop.toRaylibColor(), backgroundBottom.toRaylibColor());
    }

    public static void endFrame() {
        EndDrawing();
    }

    public static void drawOverlay(String text) {
        DrawRectangle(0, 0, 800, 480, new ColorUtils(0, 0, 0, 100).toRaylibColor());
        int fontSize = 50;
        int textWidth = MeasureText(text, fontSize);
        int textX = (Main.getScreenWidth() - textWidth) / 2;
        int textY = (Main.getScreenHeight() - fontSize) / 2;

        DrawText(text, textX, textY, fontSize, new ColorUtils(0, 0, 0, 255).toRaylibColor());
    }

}
