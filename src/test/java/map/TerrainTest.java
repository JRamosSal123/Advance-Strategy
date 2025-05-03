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
        // Inicializa los mocks
        //colorMock = mock(ColorUtils.class);

        // Configura el mock para que devuelva un objeto Texture cuando se llama al método load
        textureLoaderMock = mock(TextureLoader.class);
        when(textureLoaderMock.load(anyString())).thenReturn(mock(Texture.class));

        // Crea la instancia de Terrain
        terrain = new Terrain(TerrainEnum.GRASS, textureLoaderMock); // Asumiendo que TerrainEnum.PLAINS es un valor válido
    }

    @Test
    void testTerrainEnumAssignment() {
        // Verifica que el TerrainEnum se asigna correctamente
        assertEquals(TerrainEnum.GRASS, terrain.getTerrainEnum(), "El TerrainEnum debe ser PLAINS.");
    }

    @Test
    void testTextureLoading() {
        // Verifica que la textura es cargada correctamente
        assertNotNull(terrain.getTexture(), "La textura no debe ser nula.");

        // Verifica que el mock de TextureLoader se llama con el argumento correcto
        verify(textureLoaderMock, times(1)).load(anyString());
    }

    @Test
    void testToString() {
        // Verifica que el método toString devuelve la cadena correcta
        String expectedString = "Terrain: Grass(isOccupied: false)(path: assets/tiles/)(defenseLevel: 0)(mobilityLevel: 0)";
        assertEquals(expectedString, terrain.toString(), "El toString no devuelve el valor esperado.");
    }

    @Test
    void testTexturePath() {
        // Verifica que la textura se carga con el nombre correcto
        String expectedPath = TerrainEnum.GRASS.getPath() + TerrainEnum.GRASS.getName() + ".png";
        verify(textureLoaderMock, times(1)).load(expectedPath);
    }
}

