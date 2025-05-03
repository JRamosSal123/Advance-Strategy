package ui;

import utils.ColorUtils;
import utils.RectangleUtils;
import utils.ScreenUtils;

import static com.raylib.Raylib.*;

public class Button {

    private final RectangleUtils rect;
    private final ColorUtils baseColor;
    private final ColorUtils hoverColor;
    private final Texture texture;
    private final String text;
    private final int textSize;
    private final ColorUtils color;
    private boolean hovered = false;

    // Constructor para botón con textura
    public Button(RectangleUtils rect, ColorUtils baseColor, ColorUtils hoverColor, Texture texture, ColorUtils color) {
        this.rect = rect;
        this.baseColor = baseColor;
        this.hoverColor = hoverColor;
        this.texture = texture;
        this.color = color;

        this.text = null;
        this.textSize = 0;
    }

    // Constructor para botón con texto
    public Button(RectangleUtils rect, ColorUtils baseColor, ColorUtils hoverColor, String text, int textSize, ColorUtils color) {
        this.rect = rect;
        this.baseColor = baseColor;
        this.hoverColor = hoverColor;
        this.text = text;
        this.textSize = textSize;
        this.color = color;

        this.texture = null;
    }

    public void update(Vector2 mousePos) {
        hovered = ScreenUtils.isMouseOver(rect, mousePos);
    }

    public boolean isHovered() {
        return hovered;
    }

    public boolean isClicked(Vector2 mousePosition) {
        update(mousePosition);
        return mousePosition != null && isHovered() && IsMouseButtonPressed(0);
    }

    public void draw() {
        ColorUtils currentColor = hovered ? hoverColor : baseColor;
        DrawRectangleRounded(rect.toRaylibRectangle(), 0.5F, 100, currentColor.toRaylibColor());

        if (texture != null) {
            DrawTexture(texture, rect.getPositionX(), rect.getPositionY(), color.toRaylibColor());
        } else if (text != null) {
            int fontSize = textSize; // Puedes hacer esto configurable si quieres
            int textWidth = MeasureText(text, fontSize);
            int textX = rect.getPositionX() + (rect.getWidth() - textWidth) / 2;
            int textY = rect.getPositionY() + (rect.getHeight() - fontSize) / 2;

            DrawText(text, textX, textY, fontSize, color.toRaylibColor());
        }
    }
}


