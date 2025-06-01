package map;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TerrainEnumTest {

    @Test
    public void testEnumValues() {
        TerrainEnum grass = TerrainEnum.GRASS;
        assertEquals("Grass", grass.getName());
        assertEquals("assets/tiles/", grass.getPath());
        assertEquals(0, grass.getDefenseLevel());
        assertEquals(1, grass.getMovementCost());

        TerrainEnum forest = TerrainEnum.FOREST;
        assertEquals("Forest", forest.getName());
        assertEquals(1, forest.getDefenseLevel());
        assertEquals(2, forest.getMovementCost());

        TerrainEnum mountain = TerrainEnum.MOUNTAIN;
        assertEquals("Mountain", mountain.getName());
        assertEquals(2, mountain.getDefenseLevel());
        assertEquals(3, mountain.getMovementCost());

        TerrainEnum streetCurveDownLeft = TerrainEnum.STREET_CURVE_DOWN_LEFT;
        assertEquals("Street_c_d_l", streetCurveDownLeft.getName());
        assertEquals(0, streetCurveDownLeft.getDefenseLevel());
        assertEquals(1, streetCurveDownLeft.getMovementCost());
    }

    @Test
    public void testToString() {
        TerrainEnum grass = TerrainEnum.GRASS;
        assertEquals("Grass(path: assets/tiles/)(defenseLevel: 0)(mobilityLevel: 1)", grass.toString());

        TerrainEnum forest = TerrainEnum.FOREST;
        assertEquals("Forest(path: assets/tiles/)(defenseLevel: 1)(mobilityLevel: 2)", forest.toString());

        TerrainEnum mountain = TerrainEnum.MOUNTAIN;
        assertEquals("Mountain(path: assets/tiles/)(defenseLevel: 2)(mobilityLevel: 3)", mountain.toString());
    }
}

