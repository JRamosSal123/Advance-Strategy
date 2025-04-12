package map;

import com.raylib.Raylib.Texture;
import static com.raylib.Raylib.LoadTexture;

public class Terrain {
    private final TerrainEnum terrain;
    private final Texture texture;

    public Terrain(TerrainEnum terrain) {
        this.terrain = terrain;
        this.texture = LoadTexture(terrain.getPath() + terrain.getName() + ".png");
    }
    public TerrainEnum getTerrain() {
        return terrain;
    }

    public Texture getTexture() {
        return texture;
    }

    @Override
    public String toString() {
        return "Terrain: " + terrain.toString();
    }

}
