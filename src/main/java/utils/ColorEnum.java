package utils;

public enum ColorEnum {

    WHITE(255, 255, 255),
    BLACK(0, 0, 0),

    GREY_TOP(65, 65, 65),
    GREY_BOTTOM(40, 40, 40),

    GREY_LIGHT(200, 200, 200),
    GREY_DARK(150, 150, 150),

    BLUE_LIGHT(19, 66, 141),
    BLUE_DARK(9, 48, 110),

    GREEN_LIGHT(0, 171, 65),
    GREEN_DARK(0, 134, 49),

    ORANGE_LIGHT(213, 139, 9),
    ORANGE_DARK(166, 106, 0),

    RED_LIGHT(255, 77, 77),
    RED_DARK(255, 25, 25),

    AREA(102,191,225,150),
    ENEMY(255,0,0,150);

    private final ColorUtils colorUtils;

    ColorEnum(int r, int g, int b) {
        this.colorUtils = new ColorUtils(r, g, b);
    }

    ColorEnum(int r, int g, int b, int alpha) {
        this.colorUtils = new ColorUtils(r, g, b, alpha);
    }

    public ColorUtils getColorUtils() {
        return colorUtils;
    }
}
