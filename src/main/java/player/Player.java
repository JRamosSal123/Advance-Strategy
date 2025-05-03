package player;

import units.Structure;
import units.Troop;
import units.Unit;
import units.UnitEnum;
import utils.AnimationState;
import java.util.ArrayList;
import java.util.List;

import com.raylib.Raylib;
import com.raylib.Raylib.Vector2;
import utils.ColorUtils;
import utils.TextureLoader;

import static com.raylib.Raylib.LoadTexture;

public class Player {
    private List<Unit> units = new ArrayList<>();
    private final ColorUtils color;
    private final TextureLoader loader;

    public Player(ColorUtils color, TextureLoader loader) {
        this.color = color;
        this.loader = loader;
    }

    public List<Unit> getUnits() {
        return units;
    }

    public ColorUtils getColor() {
        return color;
    }

    public void drawUnits(List<Unit> units) {
        for (Unit unit : units) {
            unit.draw(getColor(), unit.getLife());
        }
    }

    public void loadUnitsPlayer() {
        Troop unit1 = new Troop(UnitEnum.SOLDIER, 10, 3*32, 13*32);
        loadTexturesBase(unit1);
        unit1.setCurrentState(AnimationState.IDLE_RIGHT);
        units.add(unit1);

        Troop unit2 = new Troop(UnitEnum.SOLDIER, 10, 4*32, 12*32);
        loadTexturesBase(unit2);
        unit2.setCurrentState(AnimationState.IDLE_RIGHT);
        units.add(unit2);

        Troop unit3 = new Troop(UnitEnum.SOLDIER, 10, 5*32, 13*32);
        loadTexturesBase(unit3);
        unit3.setCurrentState(AnimationState.IDLE_RIGHT);
        units.add(unit3);

        Structure structure = new Structure(UnitEnum.STRUCTURE, 10, 4*32,13*32);
        loadTexturesBase(structure);
        structure.setCurrentState(AnimationState.UP);
        units.add(structure);
    }

    public void loadUnitsOpponent() {
        Troop unit1 = new Troop(UnitEnum.SOLDIER, 10, 17*32, 32);
        loadTexturesBase(unit1);
        unit1.setCurrentState(AnimationState.IDLE_RIGHT);
        units.add(unit1);

        Troop unit2 = new Troop(UnitEnum.SOLDIER, 10, 18*32, 2*32);
        loadTexturesBase(unit2);
        unit2.setCurrentState(AnimationState.IDLE_RIGHT);
        units.add(unit2);

        Troop unit3 = new Troop(UnitEnum.SOLDIER, 10, 19*32, 32);
        loadTexturesBase(unit3);
        unit3.setCurrentState(AnimationState.IDLE_RIGHT);
        units.add(unit3);

        Structure structure = new Structure(UnitEnum.STRUCTURE, 10, 18*32,32);
        loadTexturesBase(structure);
        structure.setCurrentState(AnimationState.UP); // por defecto
        units.add(structure);
    }

    public void loadTexturesBase(Unit unit) {
        for (AnimationState state : AnimationState.values()) {
            Raylib.Texture[] framesBase = new Raylib.Texture[3];
            Raylib.Texture[] framesColor = new Raylib.Texture[3];
            for (int i = 0; i < 3; i++) {
                String path = "assets/sprites/" + unit.getUnitEnum().getName()
                        + "/" + state.name().toLowerCase()
                        + "/" + state.name().toLowerCase() +  "_" + i;

                framesBase[i] = loader.load(path + ".png");
                framesColor[i] = loader.load(path + "_G.png");
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

    public void clearDeathUnit() {
        units.removeIf(unit -> unit.getLife() <= 0);
    }

    public boolean isLoser() {
        for (Unit unit : units) {
            if (unit instanceof Structure) {
                return false;
            }
        }
        return true;
    }
}
