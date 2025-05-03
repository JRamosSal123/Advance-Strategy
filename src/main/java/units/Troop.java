package units;

import com.raylib.Raylib;
import com.raylib.Raylib.Vector2;
import utils.AnimationState;
import utils.ColorUtils;

import java.util.ArrayList;
import java.util.List;

public class Troop extends Unit {

    //private int id;

    private Vector2 destination;
    private List<Vector2> posibleMovement = new ArrayList<>();
    private List<Vector2> posibleAttack = new ArrayList<>();
    private boolean isLastDirectionRight = true;
    //private final float speed = 100f;

    public Troop(UnitEnum unit,int life, int x, int y) {
        super(unit, life, x, y);
        this.destination = new Vector2();
        this.destination.x(position.x());
        this.destination.y(position.y());
        checkArea();
    }

    //public UnitEnum getUnitEnum() {return unitEnum;}
    public Vector2 getDestination() {return destination;}
    public void setDestinationX(int x) {
        this.destination.x(x);
    }
    public void setDestinationY(int y) {
        this.destination.y(y);
    }
    public void setDestination(int x, int y) {
        setDestinationX(x);
        setDestinationY(y);
    }
    public List<Vector2> getPosibleMovement() {
        return posibleMovement;
    }
    public void setPosibleMovement(List<Vector2> posibleMovement) {
        this.posibleMovement = posibleMovement;
    }

    public List<Vector2> getPosibleAttack(){
        return posibleAttack;
    }

    @Override
    public void draw(ColorUtils color, int life) {
        updateAnimation();
        Raylib.DrawTexture(getTextureBase(), (int)getPosition().x(), (int)getPosition().y(), new ColorUtils(255,255,255).toRaylibColor());
        Raylib.DrawTexture(getTextureColor(), (int)getPosition().x(), (int)getPosition().y(), color.toRaylibColor());
        Raylib.DrawRectangle((int)getPosition().x(), (int)getPosition().y(),10,10,new ColorUtils(0,0,0).toRaylibColor());
        Raylib.DrawText(Integer.toString(life), (int)getPosition().x(), (int)getPosition().y(), 5, new ColorUtils(255,255,255).toRaylibColor());

    }

    public boolean move(int x, int y) {
        boolean finish = false;
        Vector2 v = new Vector2();
        v.x(x);
        v.y(y);
        /*for(Vector2 p : posibleMovement) {
            if (p.x() == x && p.y() == y) {
                setDestination(x,y);
            }
        }*/
        if(validMovement(x,y)){
            setDestination(x,y);
        }

        if (getPosition().x() < getDestination().x()) {
            setPositionX( (int)getPosition().x() + 1 );
            setCurrentState(AnimationState.RIGHT);
            this.isLastDirectionRight = true;
            return finish;
        }

        if (getPosition().y() < getDestination().y()) {
            setPositionY( (int)getPosition().y() + 1 );
            setCurrentState(AnimationState.DOWN);
            return finish;
        }

        if (getPosition().x() > getDestination().x()) {
            setPositionX( (int)getPosition().x() - 1 );
            setCurrentState(AnimationState.LEFT);
            this.isLastDirectionRight = false;
            return finish;
        }

        if (getPosition().y() > getDestination().y()) {
            setPositionY( (int)getPosition().y() - 1 );
            setCurrentState(AnimationState.UP);
            return finish;
        }

        if(getPosition().x() == getDestination().x() && getPosition().y() == getDestination().y()) {
            finish = true;
            checkArea();
            if(getCurrentState() == AnimationState.RIGHT){
                setCurrentState(AnimationState.IDLE_RIGHT);
            }
            if(getCurrentState() == AnimationState.LEFT){
                setCurrentState(AnimationState.IDLE_LEFT);
            }
            if(getCurrentState() == AnimationState.UP || getCurrentState() == AnimationState.DOWN){
                setCurrentState(isLastDirectionRight ? AnimationState.IDLE_RIGHT : AnimationState.IDLE_LEFT);
            }
        }
        return finish;
    }

    /*
    public boolean move(int x, int y) {
        for (Vector2 p : posibleMovement) {
            if (p.x() == x && p.y() == y) {
                setDestination(x, y);
                return true; // movimiento iniciado
            }
        }
        return false; // destino no válido
    }

    public void updatePosition(float deltaTime) {
        float x = position.x();
        float y = position.y();
        float dx = destination.x() - x;
        float dy = destination.y() - y;
        float distance = (float)Math.sqrt(dx * dx + dy * dy);

        if (distance < 1.0f) {
            position.x(destination.x());
            position.y(destination.y());
            updateIdleState();
            checkArea();
            return;
        }

        // Dirección normalizada
        float dirX = dx / distance;
        float dirY = dy / distance;

        // Movimiento proporcional al tiempo
        float moveX = dirX * speed * deltaTime;
        float moveY = dirY * speed * deltaTime;

        // Evitar pasar el destino
        if (Math.abs(moveX) > Math.abs(dx)) moveX = dx;
        if (Math.abs(moveY) > Math.abs(dy)) moveY = dy;

        // Actualizar posición
        position.x(x + moveX);
        position.y(y + moveY);

        // Estado de animación
        if (Math.abs(dx) > Math.abs(dy)) {
            setCurrentState(dx > 0 ? AnimationState.RIGHT : AnimationState.LEFT);
            isLastDirectionRight = dx > 0;
        } else {
            setCurrentState(dy > 0 ? AnimationState.DOWN : AnimationState.UP);
        }
    }

    public boolean hasReachedDestination() {
        return position.x() == destination.x() && position.y() == destination.y();
    }

    private void updateIdleState() {
        switch (getCurrentState()) {
            case RIGHT -> setCurrentState(AnimationState.IDLE_RIGHT);
            case LEFT -> setCurrentState(AnimationState.IDLE_LEFT);
            case UP, DOWN -> {
                if (isLastDirectionRight) setCurrentState(AnimationState.IDLE_RIGHT);
                else setCurrentState(AnimationState.IDLE_LEFT);
            }
        }
    }
*/

    public void attack(Unit target, int defense) {
        if (target == null) return;
        int damage = getUnitEnum().getDamage() - defense;
        if(damage > 0) target.setLife(target.getLife() - damage);

    }

    //public void destroy() {this.life = 0;}

    @Override
    public String toString() {
        return "Unit [ life: " + life + " " + getUnitEnum().toString() + "]";
    }

    public void drawArea(ColorUtils color, ColorUtils colorEnemy) {
        for(Vector2 v : posibleMovement) {
            if(v != null) {
                Raylib.DrawRectangle((int)v.x(),(int)v.y(),32,32,color.toRaylibColor());
            }
        }
        for(Vector2 v: posibleAttack) {
            if(v != null) {
                Raylib.DrawRectangle((int)v.x(),(int)v.y(),32,32,colorEnemy.toRaylibColor());
            }
        }
    }

    public void checkArea() {

        int centroX = (int)getPosition().x();
        int centroY = (int)getPosition().y();
        int radio = getUnitEnum().getMobility();
        posibleMovement.clear();

        for(int i=-radio;i<radio+radio;i++){
            for(int j=-radio;j<radio+radio;j++){
                int magnitud = Math.abs(i) + Math.abs(j);
                if((magnitud < radio || magnitud == radio) && magnitud != 0){
                    Vector2 v = new Vector2();
                    v.x(i*32+centroX);
                    v.y(j*32+centroY);
                    posibleMovement.add(v);
                }
            }
        }
    }

    public void removeVectorPosibleMovement(List<Unit> allUnits) {
        posibleMovement.removeIf(pos -> {
            for (Unit u : allUnits) {
                if (u == this) continue;
                if (u.getPosition().x() == pos.x() && u.getPosition().y() == pos.y()) {
                    return true;
                }
            }
            return false;
        });
    }

    public void removeVectorAttack(List<Unit> allUnits) {
        adjacent();
        posibleAttack.removeIf(pos -> {
            for (Unit u : allUnits) {
                if (u == this) continue;
                if (u.getPosition().x() == pos.x() && u.getPosition().y() == pos.y()) {
                    return false;
                }
            }
            return true;
        });
    }

    public boolean adjacent() {
        int centroX = (int)getPosition().x();
        int centroY = (int)getPosition().y();
        int radio = 1;
        posibleAttack.clear();

        for(int i=-radio;i<radio+radio;i++){
            for(int j=-radio;j<radio+radio;j++){
                int magnitud = Math.abs(i) + Math.abs(j);
                if((magnitud < radio || magnitud == radio) && magnitud != 0){
                    Vector2 v = new Vector2();
                    int x = i*32+centroX;
                    int y = j*32+centroY;
                    v.x(x);
                    v.y(y);
                    posibleAttack.add(v);
                }
            }
        }
        return posibleAttack.isEmpty();
    }

    public boolean validMovement(int x, int y) {
        for(Vector2 p : posibleMovement) {
            if (p.x() == x && p.y() == y) {
                return true;
            }
        }
        return false;
    }
}
