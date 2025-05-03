package units;

import com.raylib.Raylib.Vector2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.AnimationState;
import utils.ColorUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TroopTest {

    private Troop troop;

    @BeforeEach
    void setUp() {
        troop = new Troop(UnitEnum.SOLDIER, 100, 0, 0);
    }

    @Test
    void testInitialPositionAndDestination() {
        assertEquals(0, troop.getPosition().x());
        assertEquals(0, troop.getPosition().y());
        assertEquals(0, troop.getDestination().x());
        assertEquals(0, troop.getDestination().y());
    }

    @Test
    void testSetDestination() {
        troop.setDestination(64, 64);
        assertEquals(64, troop.getDestination().x());
        assertEquals(64, troop.getDestination().y());
    }

    @Test
    void testSetAndGetPosibleMovement() {
        List<Vector2> list = new ArrayList<>();
        Vector2 v = new Vector2();
        v.x(32); v.y(32);
        list.add(v);

        troop.setPosibleMovement(list);
        assertEquals(1, troop.getPosibleMovement().size());
        assertEquals(32, troop.getPosibleMovement().get(0).x());
    }

    @Test
    void testValidMovement() {
        Vector2 v = new Vector2();
        v.x(32); v.y(32);
        List<Vector2> list = new ArrayList<>();
        list.add(v);
        troop.setPosibleMovement(list);

        assertTrue(troop.validMovement(32, 32));
        assertFalse(troop.validMovement(64, 64));
    }

    @Test
    void testMoveUpdatesPosition() {
        Vector2 v = new Vector2();
        v.x(1); v.y(0);
        troop.setPosibleMovement(List.of(v));

        boolean finished = troop.move(1, 0);
        assertEquals(1, troop.getPosition().x());
        assertFalse(finished);
    }

    @Test
    void testMoveToDestinationFinishes() {
        // Simular que ya estamos en destino
        troop.setDestination(0, 0);
        troop.setPosition(0, 0);
        troop.setCurrentState(AnimationState.RIGHT);

        boolean finished = troop.move(0, 0);
        assertTrue(finished);
        assertTrue(troop.getCurrentState() == AnimationState.IDLE_RIGHT || troop.getCurrentState() == AnimationState.IDLE_LEFT);
    }

    @Test
    void testAttackDealsDamage() {
        Troop enemy = new Troop(UnitEnum.SOLDIER, 100, 0, 0);
        int defense = 5;
        int expectedDamage = UnitEnum.SOLDIER.getDamage() - defense;

        troop.attack(enemy, defense);
        assertEquals(100 - expectedDamage, enemy.getLife());
    }

    @Test
    void testCheckAreaGeneratesPositions() {
        troop.checkArea();
        assertFalse(troop.getPosibleMovement().isEmpty());
    }

    @Test
    void testAdjacentGeneratesPositions() {
        boolean result = troop.adjacent();
        assertFalse(result);
        assertEquals(4, troop.getPosibleAttack().size());
    }

    @Test
    void testRemoveVectorPosibleMovement() {
        // Simula una posición ocupada
        Troop other = new Troop(UnitEnum.SOLDIER, 100, 32, 0);
        Vector2 v = new Vector2();
        v.x(32); v.y(0);
        troop.setPosibleMovement(new ArrayList<>(List.of(v)));

        troop.removeVectorPosibleMovement(List.of(other));
        assertTrue(troop.getPosibleMovement().isEmpty());
    }

    @Test
    void testRemoveVectorAttack() {
        // Necesita posiciones de ataque simuladas
        Troop enemy = new Troop(UnitEnum.SOLDIER, 100, 32, 0);
        troop.setPosition(0, 0);
        troop.adjacent();  // Genera posiciones de ataque
        troop.removeVectorAttack(List.of(enemy));

        assertTrue(troop.getPosibleAttack().stream()
                .anyMatch(v -> v.x() == 32 && v.y() == 0));
    }

    @Test
    void testToStringReturnsCorrectFormat() {
        String result = troop.toString();
        assertTrue(result.contains("life:"));
        assertTrue(result.contains(UnitEnum.SOLDIER.toString()));
    }
}

