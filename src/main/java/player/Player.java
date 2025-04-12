package player;


import core.Game;
import units.Unit;
import units.UnitEnum;
import utils.AnimationState;
import java.util.ArrayList;
import java.util.List;
import com.raylib.Raylib.Color;
import com.raylib.Raylib;
import com.raylib.Raylib.Vector2;
import static com.raylib.Raylib.LoadTexture;

public class Player {
    private Game game;
    private List<Unit> units = new ArrayList<>();
    private final Color color;

    public Player(Color color) {
        this.color = color;
    }

    public List<Unit> getUnits() {
        return units;
    }

    public Color getColor() {
        return color;
    }

    public void drawUnits(List<Unit> units) {
        for (Unit unit : units) {
            unit.drawUnit(getColor());
        }
    }

    public void loadUnits() {
        for(int i = 5; i<8;i++){
            Unit unit = new Unit(i+1, UnitEnum.SOLDIER, 10, i*32, 192);
            loadTexturesBase(unit);
            loadTexturesBase(unit);
            unit.setCurrentState(AnimationState.IDLE_RIGHT); // por defecto
            units.add(unit);
        }
        for (int i = 0; i < units.size(); i++) {
            units.get(i).setID(i);
            i++;
        }
    }

    public void loadUnits1() {
        for(int i = 0; i<4;i++){
            Unit unit = new Unit(i+1, UnitEnum.SOLDIER, 10, 576, i*64);
            loadTexturesBase(unit);
            loadTexturesBase(unit);
            unit.setCurrentState(AnimationState.IDLE_LEFT); // por defecto
            units.add(unit);
        }
        for (int i = 0; i < units.size(); i++) {
            units.get(i).setID(i);
            i++;
        }
    }

    public void loadTexturesBase(Unit unit) {
        for (AnimationState state : AnimationState.values()) {
            Raylib.Texture[] framesBase = new Raylib.Texture[3];
            Raylib.Texture[] framesColor = new Raylib.Texture[3];
            for (int i = 0; i < 3; i++) {
                String path = "assets/sprites/" + unit.getUnitEnum().getName()
                        + "/" + state.name().toLowerCase()
                        + "/" + state.name().toLowerCase() +  "_" + i;

                framesBase[i] = LoadTexture(path + ".png");
                framesColor[i] = LoadTexture(path + "_G.png");
            }
            unit.setAnimationBase(state, framesBase);
            unit.setAnimationColor(state, framesColor);
        }
    }

    public Unit searchUnit(int x, int y) {
        for (Unit unit : units) {

            Raylib.Vector2 pos = new Vector2();
            pos.x(x);
            pos.y(y);

            if (unit.getPosition().x() == pos.x() && unit.getPosition().y() == pos.y()) {
                return unit;
            }
        }
        return null;
    }
}
