package utils;

import com.raylib.Raylib.Rectangle;

public class RectangleUtils {
    private float positionX, positionY, width, height;

    public RectangleUtils(int positionX, int positionY, int width, int height) {
        this.positionX = (float) positionX;
        this.positionY = (float) positionY;
        this.width = (float) width;
        this.height = (float) height;
    }

    public Rectangle toRaylibRectangle() {
        Rectangle r = new Rectangle();
        r.x(this.positionX);
        r.y(this.positionY);
        r.width(this.width);
        r.height(this.height);
        return r;
    }

    public int getPositionX() {
        return (int) this.positionX;
    }
    public int getPositionY() {
        return (int) this.positionY;
    }
    public int getWidth() {
        return (int) this.width;
    }
    public int getHeight() {
        return (int) this.height;
    }

}
