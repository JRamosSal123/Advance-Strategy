package utils;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ColorEnumTest {

    @Test
    public void testWhiteColor() {
        ColorUtils color = ColorEnum.WHITE.getColorUtils();
        assertEquals(255, color.getRed());
        assertEquals(255, color.getGreen());
        assertEquals(255, color.getBlue());
        assertEquals(255, color.getAlpha());
    }

    @Test
    public void testBlackColor() {
        ColorUtils color = ColorEnum.BLACK.getColorUtils();
        assertEquals(0, color.getRed());
        assertEquals(0, color.getGreen());
        assertEquals(0, color.getBlue());
        assertEquals(255, color.getAlpha());
    }

    @Test
    public void testAreaColorWithAlpha() {
        ColorUtils color = ColorEnum.AREA.getColorUtils();
        assertEquals(102, color.getRed());
        assertEquals(191, color.getGreen());
        assertEquals(225, color.getBlue());
        assertEquals(150, color.getAlpha());
    }

    @Test
    public void testEnemyColorWithAlpha() {
        ColorUtils color = ColorEnum.ENEMY.getColorUtils();
        assertEquals(255, color.getRed());
        assertEquals(0, color.getGreen());
        assertEquals(0, color.getBlue());
        assertEquals(150, color.getAlpha());
    }

    @Test
    public void testAllColorsHaveValidRange() {
        for (ColorEnum colorEnum : ColorEnum.values()) {
            ColorUtils color = colorEnum.getColorUtils();
            assertTrue(color.getRed() >= 0 && color.getRed() <= 255, "Red out of range");
            assertTrue(color.getGreen() >= 0 && color.getGreen() <= 255, "Green out of range");
            assertTrue(color.getBlue() >= 0 && color.getBlue() <= 255, "Blue out of range");
            assertTrue(color.getAlpha() >= 0 && color.getAlpha() <= 255, "Alpha out of range");
        }
    }
}

