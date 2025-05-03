package utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColorUtilsTest {

    private ColorUtils color;

    @BeforeEach
    void setUp() {
        color = new ColorUtils(255, 100, 50);
    }

    @Test
    void testConstructorWithRGB() {
        assertEquals(255, color.getR());
        assertEquals(100, color.getG());
        assertEquals(50, color.getB());
        assertEquals(255, color.getA());  // Default alpha value should be 255
    }

    @Test
    void testConstructorWithRGBA() {
        ColorUtils colorWithAlpha = new ColorUtils(255, 100, 50, 128);
        assertEquals(255, colorWithAlpha.getR());
        assertEquals(100, colorWithAlpha.getG());
        assertEquals(50, colorWithAlpha.getB());
        assertEquals(128, colorWithAlpha.getA());  // Custom alpha value
    }

    @Test
    void testSetAlpha() {
        color.setAlpha(128);
        assertEquals(128, color.getA());
    }

    @Test
    void testGetters() {
        assertEquals(255, color.getR());
        assertEquals(100, color.getG());
        assertEquals(50, color.getB());
        assertEquals(255, color.getA());
    }
}

