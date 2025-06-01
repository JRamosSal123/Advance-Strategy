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

    public Button(RectangleUtils rect, ColorUtils baseColor, ColorUtils hoverColor, Texture texture, ColorUtils color) {
        this.rect = rect;
        this.baseColor = baseColor;
        this.hoverColor = hoverColor;
        this.texture = texture;
        this.color = color;
        this.text = null;
        this.textSize = 0;
    }

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
        hovered = ScreenUtils.isMouseOver(getRect(), mousePos);
    }

    public boolean isHovered() {
        return hovered;
    }

    public boolean isClicked(Vector2 mousePosition) {
        update(mousePosition);
        return mousePosition != null && isHovered() && IsMouseButtonPressed(0);
    }

    public ColorUtils selectedColorRect() {
        return isHovered() ? getHoverColor() : getBaseColor();
    }

    public void draw() {
        ColorUtils currentColor = selectedColorRect();
        DrawRectangleRounded(getRect().toRaylibRectangle(), 0.5F, 100, currentColor.toRaylibColor());

        if (getTexture() != null) {
            DrawTexture(getTexture(), getRect().getPositionX(), getRect().getPositionY(), getColor().toRaylibColor());
        } else if (getText() != null) {
            int fontSize = getTextSize();
            int textWidth = MeasureText(getText(), fontSize);
            int textX = getRect().getPositionX() + (getRect().getWidth() - textWidth) / 2;
            int textY = getRect().getPositionY() + (getRect().getHeight() - fontSize) / 2;

            DrawText(getText(), textX, textY, fontSize, getColor().toRaylibColor());
        }
    }

    public RectangleUtils getRect() {
        return rect;
    }
    public ColorUtils getBaseColor() {
        return baseColor;
    }
    public ColorUtils getHoverColor() {
        return hoverColor;
    }
    public Texture getTexture() {
        return texture;
    }
    public String getText() {
        return text;
    }
    public int getTextSize() {
        return textSize;
    }
    public ColorUtils getColor() {
        return color;
    }
}


