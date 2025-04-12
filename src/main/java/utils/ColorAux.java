package utils;

import com.raylib.Raylib;

public class ColorAux {
    private byte red, green, blue, alpha;

    public ColorAux(int red, int green, int blue, int alpha) {
        this.red = (byte) red;
        this.green = (byte) green;
        this.blue = (byte) blue;
        this.alpha = (byte) alpha;
    }

    public Raylib.Color toRaylibColor() {
        Raylib.Color c = new Raylib.Color();
        c.r(this.red);
        c.g(this.green);
        c.b(this.blue);
        c.a(this.alpha);
        return c;
    }

    public int getR() { return (int)red; }
    public int getG() { return (int)green; }
    public int getB() { return (int)blue; }
    public int getA() { return (int)alpha; }
}
