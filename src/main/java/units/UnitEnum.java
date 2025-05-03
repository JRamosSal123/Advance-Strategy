package units;

public enum UnitEnum {
    SOLDIER("Soldier", 5,10),
    TANK("Tank", 1,5),
    HUMVEE("Humvee", 1,5),
    MOTORBIKE("Motorbike", 1,5),
    ARTILLERY("Artillery",   1,5),
    MISSILELAUNCHER("Missile_Launcher",  1,5),
    STRUCTURE("Structure",  0,0);

    private final String name;
    private final int damage;
    private final int mobility;


    UnitEnum(String name, int damage, int mobility) {
        this.name = name;
        this.damage = damage;
        this.mobility = mobility;
    }

    public String getName() { return name; }
    public int getDamage() { return damage; }
    public int getMobility() { return mobility; }

    @Override
    public String toString() {
        return name +
                "(damage: " + damage + ")" +
                "(mobility: " + mobility + ")";
    }
}
