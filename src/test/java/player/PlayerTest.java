package player;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import units.Structure;
import units.Troop;
import units.Unit;
import utils.ColorUtils;

import java.util.List;
import com.raylib.Raylib.Vector2;
import com.raylib.Raylib.Texture;
import utils.TextureLoader;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PlayerTest {

    private Player player;
    private ColorUtils colorMock;
    private TextureLoader textureLoaderMock;

    @BeforeEach
    public void setUp() {
        colorMock = mock(ColorUtils.class);
        textureLoaderMock = mock(TextureLoader.class);
        when(textureLoaderMock.load(anyString())).thenReturn(mock(Texture.class));
        player = new Player(colorMock, textureLoaderMock);
    }

    @Test
    public void testPlayerInitialization() {
        assertNotNull(player.getUnits());
        assertTrue(player.getUnits().isEmpty());
        assertEquals(colorMock, player.getColor());
    }

    @Test
    public void testLoadUnitsPlayer() {
        player.loadUnitsPlayer();
        List<Unit> units = player.getUnits();
        assertEquals(4, units.size());
        assertInstanceOf(Troop.class, units.get(0));
        assertInstanceOf(Structure.class, units.get(3));
    }

    @Test
    public void testLoadUnitsOpponent() {
        player.loadUnitsOpponent();
        List<Unit> units = player.getUnits();
        assertEquals(4, units.size());
        assertInstanceOf(Troop.class, units.get(0));
        assertInstanceOf(Structure.class, units.get(3));
    }

    @Test
    public void testClearDeathUnit() {
        Unit aliveUnit = mock(Unit.class);
        when(aliveUnit.getLife()).thenReturn(10);

        Unit deadUnit = mock(Unit.class);
        when(deadUnit.getLife()).thenReturn(0);

        player.getUnits().add(aliveUnit);
        player.getUnits().add(deadUnit);

        player.clearDeathUnit();
        assertEquals(1, player.getUnits().size());
        assertTrue(player.getUnits().contains(aliveUnit));
    }

    @Test
    public void testIsLoserWithStructure() {
        Unit structure = mock(Structure.class);
        player.getUnits().add(structure);
        assertFalse(player.isLoser());
    }

    @Test
    public void testIsLoserWithoutStructure() {
        Unit troop = mock(Troop.class);
        player.getUnits().add(troop);
        assertTrue(player.isLoser());
    }

    @Test
    public void testSearchUnitFound() {
        Unit mockUnit = mock(Unit.class);
        Vector2 position = new Vector2();
        position.x(100);
        position.y(200);
        when(mockUnit.getPosition()).thenReturn(position);

        player.getUnits().add(mockUnit);
        Unit result = player.searchUnit(100, 200);
        assertEquals(mockUnit, result);
    }

    @Test
    public void testSearchUnitNotFound() {
        Unit mockUnit = mock(Unit.class);
        Vector2 position = new Vector2();
        position.x(10);
        position.y(20);
        when(mockUnit.getPosition()).thenReturn(position);

        player.getUnits().add(mockUnit);
        Unit result = player.searchUnit(0, 0);
        assertNull(result);
    }

    @Test
    public void testDrawUnitsCallsDraw() {
        Unit mockUnit1 = mock(Unit.class);
        Unit mockUnit2 = mock(Unit.class);
        player.drawUnits(List.of(mockUnit1, mockUnit2));
        verify(mockUnit1).draw(colorMock,0);
        verify(mockUnit2).draw(colorMock,0);
    }
}

