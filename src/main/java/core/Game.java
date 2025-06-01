package core;

import P2P.GameP2P;
import com.raylib.Raylib;
import map.Board;
import player.Player;
import ui.Button;
import units.Troop;
import units.Unit;
import utils.ColorEnum;
import utils.ColorUtils;
import utils.RectangleUtils;
import utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.raylib.Raylib.*;
import static com.raylib.Raylib.DrawRectangleRounded;

public abstract class Game {
    protected Board board;
    protected Player player;
    protected Player opponent;
    protected Raylib.Vector2 positionMouseUnit;
    protected Troop unitSelect;
    protected Troop unitMove;
    protected boolean isMyTurn;

    protected ConcurrentLinkedQueue<String> messageQueue;

    protected Troop enemyUnit;
    protected int toX = -1;
    protected int toY = -1;

    protected final Button next;
    protected final Button finished;

    protected boolean isPause = false;
    protected boolean isFinished = false;
    protected boolean isWin = false;
    protected boolean isLose = false;
    protected boolean isValid = false;

    protected List<Unit> unitsUsed = new ArrayList<>();
    protected List<Unit> allUnits = new ArrayList<>();

    protected Game() {
        this.next = new Button(
                new RectangleUtils(730,420,60,50),
                ColorEnum.GREY_LIGHT.getColorUtils(),
                ColorEnum.GREY_DARK.getColorUtils(),
                "Next",
                20,
                ColorEnum.BLACK.getColorUtils()
        );
        this.finished = new Button(
                new RectangleUtils(300,300,220,70),
                ColorEnum.GREY_LIGHT.getColorUtils(),
                ColorEnum.GREY_DARK.getColorUtils(),
                "Finished",
                50,
                ColorEnum.BLACK.getColorUtils()
        );
    }

    protected void initGame(){
        board = new Board(this);
        player = new Player(new ColorUtils(0, 228, 48), Raylib::LoadTexture);
        player.loadUnitsPlayer();
        opponent = new Player(new ColorUtils(211, 176, 131), Raylib::LoadTexture);
        opponent.loadUnitsOpponent();
        unitSelect = null;
        unitMove = null;
        allUnits.addAll(player.getUnits());
        allUnits.addAll(opponent.getUnits());

        for(Unit t : player.getUnits()){
            if(t instanceof Troop){
                ((Troop) t).checkArea(allUnits, board);
            }
        }
        for(Unit t : opponent.getUnits()){
            if(t instanceof Troop){
                ((Troop) t).checkArea(allUnits, board);
            }
        }
    }

    protected void clearUnits() {
        player.clearDeathUnit();
        opponent.clearDeathUnit();
        allUnits.clear();
        allUnits.addAll(player.getUnits());
        allUnits.addAll(opponent.getUnits());
    }

    protected Music setupMusic(){
        Music music = Raylib.LoadMusicStream(Main.getMusicPath());
        PlayAudioStream(music.stream());
        SetMusicVolume(music,0.5F);
        return music;
    }

    protected void DrawFrame(){
        board.drawMap();
        player.drawUnits(player.getUnits());
        opponent.drawUnits(opponent.getUnits());

        if(unitSelect == null && unitMove == null && isMyTurn){
            next.update(GetMousePosition());
            next.draw();
        }
        if(isFinished){
            finished.update(GetMousePosition());
            finished.draw();
        }

        if (unitSelect != null){
            unitSelect.drawArea(ColorEnum.AREA.getColorUtils(), ColorEnum.ENEMY.getColorUtils());

            RectangleUtils rectContain = new RectangleUtils(5,5,70,50);
            DrawRectangleRounded(rectContain.toRaylibRectangle(), 0.5F, 100, ColorEnum.GREY_DARK.getColorUtils().toRaylibColor());
            RectangleUtils rect = new RectangleUtils(10,10,60,40);
            DrawRectangleRounded(rect.toRaylibRectangle(), 0.5F, 100, ColorEnum.GREY_LIGHT.getColorUtils().toRaylibColor());

            DrawText(unitSelect.getUnitEnum().getName(),12,15, 10, ColorEnum.BLACK.getColorUtils().toRaylibColor());
            DrawText("Life: " + unitSelect.getLife(),12,25, 10, ColorEnum.BLACK.getColorUtils().toRaylibColor());
            DrawText("Attack: " + unitSelect.getUnitEnum().getDamage(),12,35, 10, ColorEnum.BLACK.getColorUtils().toRaylibColor());
        }

        if(isWin) ScreenUtils.drawOverlay("WIN");
        if(isLose) ScreenUtils.drawOverlay("LOSE");
        if(!isWin && !isLose && isFinished) ScreenUtils.drawOverlay("Rival Abandoned");
        if(isPause) ScreenUtils.drawOverlay("PAUSE");

        EndDrawing();

    }

    protected void unload(Music music){
        UnloadMusicStream(music);
    }

    protected int normaliceCord(int cord){
        return (cord/32)*32;
    }


}
