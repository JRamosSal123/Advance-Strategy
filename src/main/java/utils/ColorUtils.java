package utils;

import com.raylib.Raylib.Color;

public class ColorUtils {
    private int red, green, blue, alpha;

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
        // Verificar valores antes de la conversión a byte

        // Asegurar que los valores estén dentro del rango de 0 a 255
        byte r = (byte) Math.min(255, Math.max(0, this.red));
        byte g = (byte) Math.min(255, Math.max(0, this.green));
        byte b = (byte) Math.min(255, Math.max(0, this.blue));
        byte a = (byte) Math.min(255, Math.max(0, this.alpha));

        // Verificar los valores después de la corrección

        // Crear la instancia de Color
        Color c = new Color();
        c.r(r);
        c.g(g);
        c.b(b);
        c.a(a);

        return c;
    }

    public int getR() { return red; }
    public int getG() { return green; }
    public int getB() { return blue; }
    public int getA() { return alpha; }

    public void setAlpha(int a) {
        this.alpha = a;
    }
}
