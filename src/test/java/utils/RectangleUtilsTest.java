package utils;

import com.raylib.Raylib.Rectangle;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RectangleUtilsTest {

    @Test
    public void testConstructorAndGetters() {
        RectangleUtils rect = new RectangleUtils(10, 20, 30, 40);

        assertEquals(10, rect.getPositionX());
        assertEquals(20, rect.getPositionY());
        assertEquals(30, rect.getWidth());
        assertEquals(40, rect.getHeight());
    }

    @Test
    public void testToRaylibRectangle() {
        RectangleUtils rectUtils = new RectangleUtils(15, 25, 50, 60);
        Rectangle rayRect = rectUtils.toRaylibRectangle();

        assertEquals(15.0f, rayRect.x());
        assertEquals(25.0f, rayRect.y());
        assertEquals(50.0f, rayRect.width());
        assertEquals(60.0f, rayRect.height());
    }

    @Test
    public void testNegativeValues() {
        RectangleUtils rect = new RectangleUtils(-5, -10, -20, -30);

        assertEquals(-5, rect.getPositionX());
        assertEquals(-10, rect.getPositionY());
        assertEquals(-20, rect.getWidth());
        assertEquals(-30, rect.getHeight());
    }
}

