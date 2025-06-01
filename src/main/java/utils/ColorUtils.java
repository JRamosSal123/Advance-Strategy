package utils;

import com.raylib.Raylib.Color;

public class ColorUtils {
    private int red, green, blue, alpha;
    private final Color c = new Color();

    public ColorUtils(int red, int green, int blue) {
        this(red, green, blue, 255);
    }

    public ColorUtils(int red, int green, int blue, int alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public Color toRaylibColor() {
        /*byte r = (byte) Math.min(255, Math.max(0, this.red));
        byte g = (byte) Math.min(255, Math.max(0, this.green));
        byte b = (byte) Math.min(255, Math.max(0, this.blue));
        byte a = (byte) Math.min(255, Math.max(0, this.alpha));

        Color c = new Color();
        c.r(r);
        c.g(g);
        c.b(b);
        c.a(a);

        return c;*/
        c.r((byte) Math.min(255, Math.max(0, this.red)));
        c.g((byte) Math.min(255, Math.max(0, this.green)));
        c.b((byte) Math.min(255, Math.max(0, this.blue)));
        c.a((byte) Math.min(255, Math.max(0, this.alpha)));

        return c;
    }

    public int getRed() { return red; }
    public int getGreen() { return green; }
    public int getBlue() { return blue; }
    public int getAlpha() { return alpha; }

    public void setAlpha(int a) {
        this.alpha = a;
    }
}
