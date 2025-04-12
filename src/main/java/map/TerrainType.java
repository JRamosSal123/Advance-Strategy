package map;

import com.raylib.Raylib.Texture;

public class TerrainType {
    private final String name;
    private boolean isOccupied;
    private final String path;
    private final int defenseLevel;
    private final int mobilityLevel;

    public TerrainType(String name, String path, int defenseLevel, int mobilityLevel) {
        this.name = name;
        this.isOccupied = false;
        this.path = path;
        this.defenseLevel = defenseLevel;
        this.mobilityLevel = mobilityLevel;
    }

    public String getName() {
        return name;
    }

    public boolean getIsOccupied() {
        return isOccupied;
    }

    public String getPath() {
        return path;
    }

    public int getDefenseLevel() {
        return defenseLevel;
    }

    public int getMobility() {
        return mobilityLevel;
    }

    @Override
    public String toString() {
        return name +
                "(isOccupied: " + isOccupied + ")" +
                "(path: " + path + ")" +
                "(defenseLevel: " + defenseLevel + ")" +
                "(mobilityLevel: " + mobilityLevel + ")";
    }
}
