package ui;

import utils.ColorUtils;
import utils.RectangleUtils;

import static com.raylib.Raylib.*;

public class ButtonClicker extends Button {

    private boolean clicked = false;

    public ButtonClicker(RectangleUtils rect, ColorUtils baseColor, ColorUtils hoverColor, Texture texture, ColorUtils color) {
        super(rect, baseColor, hoverColor, texture, color);
    }

    public ButtonClicker(RectangleUtils rect, ColorUtils baseColor, ColorUtils hoverColor, String text, int textSize, ColorUtils color) {
        super(rect, baseColor, hoverColor, text, textSize, color);
    }

    @Override
    public boolean isClicked(Vector2 mousePosition) {
        if(!clicked) {
            clicked = mousePosition != null && isHovered() && IsMouseButtonPressed(0);
        }
        return mousePosition != null && isHovered() && IsMouseButtonPressed(0);
    }

    public void resetClicked() {
        clicked = false;
    }

    @Override
    public ColorUtils selectedColorRect() {
        ColorUtils currentColor;
        if(clicked) {
            currentColor = getHoverColor();
        }
        else {
            currentColor = isHovered() ? getHoverColor() : getBaseColor();
        }
        return currentColor;
    }
}


