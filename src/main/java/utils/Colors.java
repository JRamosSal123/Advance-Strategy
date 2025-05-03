package utils;

public enum Colors {

    WHITE(new ColorUtils(255, 255, 255));

    private ColorUtils color;

    Colors(ColorUtils colorUtils) {
        this.color = color;
    }

    public ColorUtils getColor() {
        return color;
    }


}
