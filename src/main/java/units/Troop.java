package units;

import com.raylib.Raylib;
import com.raylib.Raylib.Vector2;
import core.Main;
import map.Board;
import utils.AnimationState;
import utils.ColorEnum;
import utils.ColorUtils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Troop extends Unit {

    private Vector2 destination;
    private List<Vector2> posibleMovement = new ArrayList<>();
    private List<Vector2> posibleAttack = new ArrayList<>();
    private boolean isLastDirectionRight = true;

    public Troop(int id, UnitEnum unit,int life, int x, int y) {
        super(id, unit, life, x, y);
        this.destination = new Vector2();
        this.destination.x(position.x());
        this.destination.y(position.y());
    }

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
        Raylib.DrawTexture(getTextureBase(), (int)getPosition().x(), (int)getPosition().y(), ColorEnum.WHITE.getColorUtils().toRaylibColor());
        Raylib.DrawTexture(getTextureColor(), (int)getPosition().x(), (int)getPosition().y(), color.toRaylibColor());
        Raylib.DrawRectangle((int)getPosition().x(), (int)getPosition().y(),10,10, ColorEnum.BLACK.getColorUtils().toRaylibColor());
        Raylib.DrawText(Integer.toString(life), (int)getPosition().x(), (int)getPosition().y(), 5, ColorEnum.WHITE.getColorUtils().toRaylibColor());
    }

    @Override
    public Unit cloneUnit() {
        Troop cloned = null;
        if(this.getLife() > 0) {
            cloned = new Troop(this.getId(), this.getUnitEnum(), this.getLife(),
                    (int) this.getPosition().x(), (int) this.getPosition().y());
        }
        cloned.setCurrentState(this.getCurrentState());

        return cloned;
    }

    public boolean move(int x, int y, List<Unit> allUnits, Board board) {
        boolean finish = false;
        Vector2 v = new Vector2();
        v.x(x);
        v.y(y);

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
            checkArea(allUnits, board);
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

    public void attack(Unit target, int defense) {
        if (target == null) return;
        int damage = getUnitEnum().getDamage() - defense;
        if(damage > 0) target.setLife(target.getLife() - damage);

    }

    @Override
    public String toString() {
        return "Unit [ life: " + life + " " + getUnitEnum().toString() + "]";
    }

    public void drawArea(ColorUtils colorArea, ColorUtils colorEnemy) {
        for(Vector2 v : posibleMovement) {
            if(v != null) {
                Raylib.DrawRectangle((int)v.x(),(int)v.y(),32,32,colorArea.toRaylibColor());
            }
        }
        for(Vector2 v: posibleAttack) {
            if(v != null) {
                Raylib.DrawRectangle((int)v.x(),(int)v.y(),32,32,colorEnemy.toRaylibColor());
            }
        }
    }

    public void checkArea(List<Unit> allUnits, Board board) {
        int startX = (int) getPosition().x();
        int startY = (int) getPosition().y();
        int maxCost = getUnitEnum().getMobility();

        posibleMovement.clear();

        int[][] costMap = new int[25][15];
        for (int i = 0; i < 25; i++) {
            for (int j = 0; j < 15; j++) {
                costMap[i][j] = Integer.MAX_VALUE;
            }
        }

        class Node {
            final int x;
            final int y;
            final int cost;
            Node(int x, int y, int cost) {
                this.x = x;
                this.y = y;
                this.cost = cost;
            }
        }

        java.util.Queue<Node> queue = new java.util.LinkedList<>();
        queue.add(new Node(startX, startY, 0));
        costMap[startX / 32][startY / 32] = 0;

        int[][] directions = {
                { 0, -32 }, { 0, 32 },
                { -32, 0 }, { 32, 0 }
        };

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            if (current.cost > 0) {
                Vector2 v = new Vector2();
                v.x(current.x);
                v.y(current.y);
                posibleMovement.add(v);
            }

            for (int[] dir : directions) {
                int nx = current.x + dir[0];
                int ny = current.y + dir[1];

                int gridX = nx / 32;
                int gridY = ny / 32;

                if (gridX < 0 || gridX >= 25 || gridY < 0 || gridY >= 15) continue;

                int newCost;
                newCost = current.cost + board.getTerrain(ny, nx).getTerrainEnum().getMovementCost();

                if (newCost <= maxCost && newCost < costMap[gridX][gridY] && !isObstacle(nx, ny, allUnits)) {
                    costMap[gridX][gridY] = newCost;
                    queue.add(new Node(nx, ny, newCost));
                }
            }
        }
        clearPosibleMovement();
    }

    private boolean isObstacle(int x, int y, List<Unit> allUnits) {
        for (Unit u : allUnits) {
            if (u == this) continue;
            if ((int) u.getPosition().x() == x && (int) u.getPosition().y() == y) {
                return true;
            }
        }
        return false;
    }

    public void removeVectorPosibleMovement(List<Unit> allUnits) {
        posibleMovement.removeIf(pos -> {
            if (pos.x() < 0 || pos.x() >= 800 || pos.y() < 0 || pos.y() >= 480) {
                return true;
            }
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

    public void clearPosibleMovement() {
        for (int i = 0; i < posibleMovement.size(); i++) {
            for (int j = i + 1; j < posibleMovement.size(); j++) {
                if (posibleMovement.get(i).x() == posibleMovement.get(j).x() && posibleMovement.get(i).y() == posibleMovement.get(j).y()) {
                    posibleMovement.remove(j);
                    j--;
                }
            }
        }
    }
}
