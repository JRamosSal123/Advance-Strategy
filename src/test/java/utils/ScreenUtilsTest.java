package utils;

import com.raylib.Raylib.Vector2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ScreenUtilsTest {

    @Test
    public void testMouseOverInsideRectangle() {
        RectangleUtils rect = new RectangleUtils(100, 100, 200, 150);
        Vector2 pos = new Vector2().x(150).y(120);

        assertFalse(ScreenUtils.isMouseOver(rect, pos));
    }

    @Test
    public void testMouseOverOnEdge() {
        RectangleUtils rect = new RectangleUtils(100, 100, 200, 150);
        Vector2 pos = new Vector2().x(100).y(100); // Top-left corner

        assertFalse(ScreenUtils.isMouseOver(rect, pos));
    }

    @Test
    public void testMouseOverOutsideRectangle() {
        RectangleUtils rect = new RectangleUtils(100, 100, 200, 150);
        Vector2 pos = new Vector2().x(50).y(50); // Outside

        assertFalse(ScreenUtils.isMouseOver(rect, pos));
    }

    @Test
    public void testMouseOverWithNullPosition() {
        RectangleUtils rect = new RectangleUtils(100, 100, 200, 150);

        assertFalse(ScreenUtils.isMouseOver(rect, null));
    }
}

