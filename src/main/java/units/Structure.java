package units;

import com.raylib.Raylib.Vector2;
import utils.ColorEnum;
import utils.ColorUtils;

import static com.raylib.Raylib.*;

public class Structure extends Unit {

    public Structure(int id, UnitEnum unit, int life, int x, int y) {
        super(id, unit, life, x, y);
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
        DrawTexture(getTextureBase(), (int)getPosition().x(), (int)getPosition().y(), ColorEnum.WHITE.getColorUtils().toRaylibColor());
        DrawTexture(getTextureColor(), (int)getPosition().x(), (int)getPosition().y(), color.toRaylibColor());
        DrawRectangle((int)getPosition().x(), (int)getPosition().y(),10,10, ColorEnum.BLACK.getColorUtils().toRaylibColor());
        DrawText(Integer.toString(life), (int)getPosition().x(), (int)getPosition().y(), 5, ColorEnum.WHITE.getColorUtils().toRaylibColor());
    }

    @Override
    public Unit cloneUnit() {
        Structure cloned = new Structure(this.getId(), this.getUnitEnum(), this.getLife(),
                (int) this.getPosition().x(), (int) this.getPosition().y());

        cloned.setCurrentState(this.getCurrentState());
        return cloned;
    }

}

