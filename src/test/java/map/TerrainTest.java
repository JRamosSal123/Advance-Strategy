package map;
import com.raylib.Raylib.Texture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import utils.ColorUtils;
import utils.TextureLoader;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class TerrainTest {

    @Mock
    private TextureLoader textureLoaderMock;

    private Terrain terrain;

    @BeforeEach
    void setUp() {
        textureLoaderMock = mock(TextureLoader.class);
        when(textureLoaderMock.load(anyString())).thenReturn(mock(Texture.class));
        terrain = new Terrain(TerrainEnum.GRASS, textureLoaderMock);
    }

    @Test
    void testTerrainEnumAssignment() {
        assertEquals(TerrainEnum.GRASS, terrain.getTerrainEnum(), "El TerrainEnum debe ser PLAINS.");
    }

    @Test
    void testTextureLoading() {
        assertNotNull(terrain.getTexture(), "La textura no debe ser nula.");
        verify(textureLoaderMock, times(1)).load(anyString());
    }

    @Test
    void testToString() {
        String expectedString = "Terrain: Grass(path: assets/tiles/)(defenseLevel: 0)(mobilityLevel: 1)";
        assertEquals(expectedString, terrain.toString(), "El toString no devuelve el valor esperado.");
    }

    @Test
    void testTexturePath() {
        String expectedPath = TerrainEnum.GRASS.getPath() + TerrainEnum.GRASS.getName() + ".png";
        verify(textureLoaderMock, times(1)).load(expectedPath);
    }
}

