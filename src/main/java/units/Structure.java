package units;

import com.raylib.Raylib.Vector2;
import utils.ColorUtils;

import static com.raylib.Raylib.*;

public class Structure extends Unit {

    public Structure(UnitEnum unit, int life, int x, int y) {
        super(unit, life, x, y);
    }

    @Override
    public int getLife() {
        return super.getLife();
    }
    @Override
    public void setLife(int life) {
        super.setLife(life);
    }

    @Override
    public Vector2 getPosition() {
        return super.getPosition();
    }
    @Override
    public void setPosition(int x, int y) {
        super.setPosition(x, y);
    }

    @Override
    public void draw(ColorUtils color, int life) {
        DrawTexture(getTextureBase(), (int)getPosition().x(), (int)getPosition().y(), new ColorUtils(255,255,255).toRaylibColor());
        DrawTexture(getTextureColor(), (int)getPosition().x(), (int)getPosition().y(), color.toRaylibColor());
        DrawRectangle((int)getPosition().x(), (int)getPosition().y(),10,10,new ColorUtils(0,0,0).toRaylibColor());
        DrawText(Integer.toString(life), (int)getPosition().x(), (int)getPosition().y(), 5, new ColorUtils(255,255,255).toRaylibColor());
    }
}

