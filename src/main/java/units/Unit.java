package units;

import com.raylib.Raylib.Vector2;
import utils.AnimationEntity;
import utils.ColorUtils;

public abstract class Unit extends AnimationEntity{

    protected int life;
    protected Vector2 position;
    private final UnitEnum unitEnum;

    public Unit(UnitEnum unit,int life, int x, int y) {
        this.unitEnum = unit;
        this.life = life;
        this.position = new Vector2();
        this.position.x(x);
        this.position.y(y);
    }

    public int getLife() {
        return life;
    }
    public void setLife(int life) {
        this.life = life;
    }

    public Vector2 getPosition() {return position;}
    public void setPositionX(int x) {
        this.position.x(x);
    }
    public void setPositionY(int y) {
        this.position.y(y);
    }
    public void setPosition(int x, int y) {
        setPositionX(x);
        setPositionY(y);
    }

    public UnitEnum getUnitEnum() {return unitEnum;}

    public abstract void draw(ColorUtils color, int life);
}


