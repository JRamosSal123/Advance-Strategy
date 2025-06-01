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
        assertEquals(255, color.getRed());
        assertEquals(100, color.getGreen());
        assertEquals(50, color.getBlue());
        assertEquals(255, color.getAlpha());  // Default alpha value should be 255
    }

    @Test
    void testConstructorWithRGBA() {
        ColorUtils colorWithAlpha = new ColorUtils(255, 100, 50, 128);
        assertEquals(255, colorWithAlpha.getRed());
        assertEquals(100, colorWithAlpha.getGreen());
        assertEquals(50, colorWithAlpha.getBlue());
        assertEquals(128, colorWithAlpha.getAlpha());  // Custom alpha value
    }

    @Test
    void testSetAlpha() {
        color.setAlpha(128);
        assertEquals(128, color.getAlpha());
    }

    @Test
    void testGetters() {
        assertEquals(255, color.getRed());
        assertEquals(100, color.getGreen());
        assertEquals(50, color.getBlue());
        assertEquals(255, color.getAlpha());
    }
}

