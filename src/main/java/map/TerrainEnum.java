package map;

public enum TerrainEnum {
    GRASS("Grass", 0, 1),
    FOREST("Forest", 1, 2),
    MOUNTAIN("Mountain", 2, 3),
    STREET("Street", 2, 3),
    STREET_CURVE_DOWN_LEFT("Street_c_d_l", 2, 3),
    STREET_CURVE_DOWN_RIGHT("Street_c_d_r", 2, 3),
    STREET_CURVE_UP_LEFT("Street_c_u_l", 2, 3),
    STREET_CURVE_UP_RIGHT("Street_c_u_r", 2, 3),
    STREET_HORIZONTAL("Street_h", 2, 3),
    STREET_HORIZONTAL_DOWN("Street_h_d", 2, 3),
    STREET_HORIZONTAL_UP("Street_h_u", 2, 3),
    STREET_VERTICAL("Street_v", 2, 3),
    STREET_VERTICAL_RIGHT("Street_v_r", 2, 3),
    STREET_VERTICAL_LEFT("Street_v_l", 2, 3);

    private final String name;
    private boolean isOccupied;
    private final String path = "assets/tiles/";
    private final int defenseLevel;
    private final int movementCost;

    TerrainEnum(String name, int movementCost, int defenseBonus) {
        this.name = name;
        this.isOccupied = false;
        this.defenseLevel = defenseBonus;
        this.movementCost = movementCost;
    }

    public String getName() { return name; }
    public boolean getIsOccupied() { return isOccupied; }
    public String getPath() { return path; }
    public int getDefenseLevel() { return defenseLevel; }
    public int getMovementCost() { return movementCost; }

    @Override
    public String toString() {
        return name +
                "(isOccupied: " + isOccupied + ")" +
                "(path: " + path + ")" +
                "(defenseLevel: " + defenseLevel + ")" +
                "(mobilityLevel: " + movementCost + ")";
    }
}
