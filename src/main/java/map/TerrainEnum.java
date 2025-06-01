package map;

public enum TerrainEnum {
    GRASS("Grass", 1, 0),
    FOREST("Forest", 2, 1),
    MOUNTAIN("Mountain", 3, 2),
    STREET("Street", 1, 0),
    STREET_CURVE_DOWN_LEFT("Street_c_d_l", 1, 0),
    STREET_CURVE_DOWN_RIGHT("Street_c_d_r", 1, 0),
    STREET_CURVE_UP_LEFT("Street_c_u_l", 1, 0),
    STREET_CURVE_UP_RIGHT("Street_c_u_r", 1, 0),
    STREET_HORIZONTAL("Street_h", 1, 0),
    STREET_HORIZONTAL_DOWN("Street_h_d", 1, 0),
    STREET_HORIZONTAL_UP("Street_h_u", 1, 0),
    STREET_VERTICAL("Street_v", 1, 0),
    STREET_VERTICAL_RIGHT("Street_v_r", 1, 0),
    STREET_VERTICAL_LEFT("Street_v_l", 1, 0);

    private final String name;
    private final String path = "assets/tiles/";
    private final int defenseLevel;
    private final int movementCost;

    TerrainEnum(String name, int movementCost, int defenseBonus) {
        this.name = name;
        this.defenseLevel = defenseBonus;
        this.movementCost = movementCost;
    }

    public String getName() { return name; }
    public String getPath() { return path; }
    public int getDefenseLevel() { return defenseLevel; }
    public int getMovementCost() { return movementCost; }

    @Override
    public String toString() {
        return name +
                "(path: " + path + ")" +
                "(defenseLevel: " + defenseLevel + ")" +
                "(mobilityLevel: " + movementCost + ")";
    }
}
