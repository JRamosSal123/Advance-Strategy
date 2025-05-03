package units;

import com.raylib.Raylib.Vector2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.ColorUtils;

import static org.junit.jupiter.api.Assertions.*;

class StructureTest {

    private Structure structure;

    @BeforeEach
    void setUp() {
        structure = new Structure(UnitEnum.STRUCTURE, 150, 64, 96);
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals(150, structure.getLife());
        assertEquals(64, (int) structure.getPosition().x());
        assertEquals(96, (int) structure.getPosition().y());
        assertEquals(UnitEnum.STRUCTURE, structure.getUnitEnum());
    }

    @Test
    void testSetLife() {
        structure.setLife(100);
        assertEquals(100, structure.getLife());
    }

    @Test
    void testSetPosition() {
        structure.setPosition(128, 160);
        assertEquals(128, (int) structure.getPosition().x());
        assertEquals(160, (int) structure.getPosition().y());
    }
}

