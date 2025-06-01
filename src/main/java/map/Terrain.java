package map;

import com.raylib.Raylib.Texture;
import utils.TextureLoader;

import static com.raylib.Raylib.LoadTexture;

public class Terrain {
    private final TerrainEnum terrain;
    private final Texture texture;
    private final TextureLoader loader;

    public Terrain(TerrainEnum terrain, TextureLoader loader) {
        this.terrain = terrain;
        this.loader = loader;
        //this.texture = LoadTexture(terrain.getPath() + terrain.getName() + ".png");
        this.texture = loader.load(terrain.getPath() + terrain.getName() + ".png");
    }
    public TerrainEnum getTerrainEnum() {
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
