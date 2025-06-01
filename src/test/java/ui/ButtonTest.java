package ui;

import com.raylib.Raylib.Vector2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.ColorUtils;
import utils.RectangleUtils;
import utils.ScreenUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ButtonTest {

    private RectangleUtils rect;
    private ColorUtils baseColor;
    private ColorUtils hoverColor;
    private ColorUtils textColor;

    @BeforeEach
    public void setup() {
        rect = mock(RectangleUtils.class);
        baseColor = new ColorUtils(100, 100, 100, 255);
        hoverColor = new ColorUtils(150, 150, 150, 255);
        textColor = new ColorUtils(200, 200, 200, 255);
    }

    @Test
    public void testHoverDetection() {
        Button button = new Button(rect, baseColor, hoverColor, "Click", 20, textColor);
        Vector2 mousePos = new Vector2();
        mousePos.x(50);
        mousePos.y(50);

        mockStatic(ScreenUtils.class);
        when(ScreenUtils.isMouseOver(rect, mousePos)).thenReturn(true);

        button.update(mousePos);
        assertTrue(button.isHovered());

        when(ScreenUtils.isMouseOver(rect, mousePos)).thenReturn(false);
        button.update(mousePos);
        assertFalse(button.isHovered());
    }

    @Test
    public void testSelectedColorRect() {
        Button button = new Button(rect, baseColor, hoverColor, "Click", 20, textColor);

        Vector2 mousePos = new Vector2();
        mousePos.x(50);
        mousePos.y(50);
        button.update(mousePos);
        assertEquals(baseColor.getRed(), button.selectedColorRect().getRed());
        assertEquals(baseColor.getGreen(), button.selectedColorRect().getGreen());
        assertEquals(baseColor.getBlue(), button.selectedColorRect().getBlue());
        assertEquals(baseColor.getAlpha(), button.selectedColorRect().getAlpha());

        // Forzamos hovered a true para probar el color hover
        button = spy(button);
        doReturn(true).when(button).isHovered();
        assertEquals(hoverColor, button.selectedColorRect());
    }
}

