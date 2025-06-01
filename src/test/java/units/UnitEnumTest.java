package units;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UnitEnumTest {

    @Test
    public void testEnumValues() {
        UnitEnum soldier = UnitEnum.SOLDIER;
        assertEquals("Soldier", soldier.getName());
        assertEquals(2, soldier.getDamage());
        assertEquals(2, soldier.getMobility());

        UnitEnum tank = UnitEnum.TANK;
        assertEquals("Tank", tank.getName());
        assertEquals(5, tank.getDamage());
        assertEquals(5, tank.getMobility());

        UnitEnum artillery = UnitEnum.ARTILLERY;
        assertEquals("Artillery", artillery.getName());
        assertEquals(3, artillery.getDamage());
        assertEquals(3, artillery.getMobility());

        UnitEnum structure = UnitEnum.STRUCTURE;
        assertEquals("Structure", structure.getName());
        assertEquals(0, structure.getDamage());
        assertEquals(0, structure.getMobility());
    }

    @Test
    public void testToString() {
        UnitEnum soldier = UnitEnum.SOLDIER;
        assertEquals("Soldier(damage: 2)(mobility: 2)", soldier.toString());

        UnitEnum tank = UnitEnum.TANK;
        assertEquals("Tank(damage: 5)(mobility: 5)", tank.toString());

        UnitEnum artillery = UnitEnum.ARTILLERY;
        assertEquals("Artillery(damage: 3)(mobility: 3)", artillery.toString());

        UnitEnum structure = UnitEnum.STRUCTURE;
        assertEquals("Structure(damage: 0)(mobility: 0)", structure.toString());
    }
}

