package ui;

import utils.ColorEnum;
import utils.RectangleUtils;
import utils.ScreenUtils;

import static com.raylib.Raylib.*;

public class ConfigAIScreen {

    private final ButtonClicker easyButton;
    private final ButtonClicker mediumButton;
    private final ButtonClicker hardButton;
    private final Button connectButton;
    private final Button exitButton;

    private Vector2 mousePosition;
    private int difficultyLevel = 0;
    private boolean isFinished = false;

    private String difficultyMessage = "";
    private String statusMessageRow1 = "";
    private String statusMessageRow2 = "";

    public ConfigAIScreen() {
        easyButton = new ButtonClicker(
                new RectangleUtils(30,107,210,210),
                ColorEnum.GREEN_LIGHT.getColorUtils(),
                ColorEnum.GREEN_DARK.getColorUtils(),
                "EASY",
                50,
                ColorEnum.WHITE.getColorUtils()
        );

        mediumButton = new ButtonClicker(
                new RectangleUtils(290,107,210,210),
                ColorEnum.ORANGE_LIGHT.getColorUtils(),
                ColorEnum.ORANGE_DARK.getColorUtils(),
                "MEDIUM",
                50,
                ColorEnum.WHITE.getColorUtils()
        );

        hardButton = new ButtonClicker(
                new RectangleUtils(550,107,210,210),
                ColorEnum.RED_LIGHT.getColorUtils(),
                ColorEnum.RED_DARK.getColorUtils(),
                "HARD",
                50,
                ColorEnum.WHITE.getColorUtils()
        );

        connectButton = new Button(
                new RectangleUtils(600,385,130,60),
                ColorEnum.GREY_LIGHT.getColorUtils(),
                ColorEnum.GREY_DARK.getColorUtils(),
                "Connect",
                30,
                ColorEnum.WHITE.getColorUtils()
        );

        exitButton = new Button(
                new RectangleUtils(10,10,40,40),
                ColorEnum.BLUE_LIGHT.getColorUtils(),
                ColorEnum.BLUE_DARK.getColorUtils(),
                LoadTexture("assets/screen/arrow.png"),
                ColorEnum.WHITE.getColorUtils()
        );

    }

    public int runScreen() {
        while (!WindowShouldClose() && !isFinished) {
            mousePosition = GetMousePosition();
            handleDifficultySelection();
        }
        return difficultyLevel;
    }

    private void handleDifficultySelection() {
        easyButton.update(mousePosition);
        mediumButton.update(mousePosition);
        hardButton.update(mousePosition);
        connectButton.update(mousePosition);
        exitButton.update(mousePosition);

        if (easyButton.isClicked(mousePosition)) {
            difficultyLevel = 1;
            Write("Dificultad sencilla: El oponente hará movimientos básicos y predecibles.");

            mediumButton.resetClicked();
            hardButton.resetClicked();
        }
        if (mediumButton.isClicked(mousePosition)) {
            difficultyLevel = 3;
            Write("Dificultad media: El oponente reaccionará bien y jugará de forma equilibrada.");

            easyButton.resetClicked();
            hardButton.resetClicked();
        }
        if (hardButton.isClicked(mousePosition)) {
            difficultyLevel = 5;
            Write("Dificultad difícil: El oponente anticipará y aprovechará tus errores.");

            easyButton.resetClicked();
            mediumButton.resetClicked();
        }
        if (connectButton.isClicked(mousePosition) && difficultyLevel != 0) {
            isFinished = true;
        }
        if(exitButton.isClicked(mousePosition)) {
            difficultyLevel = 0;
            isFinished = true;
        }

        ScreenUtils.startFrame(ColorEnum.GREY_TOP.getColorUtils(),
                ColorEnum.GREY_BOTTOM.getColorUtils());
        DrawText("Seleccione la dificultad contra la que quiere jugar", 10, 60, 30, ColorEnum.WHITE.getColorUtils().toRaylibColor());

        easyButton.draw();
        mediumButton.draw();
        hardButton.draw();
        exitButton.draw();
        Draw();

        ScreenUtils.endFrame();
    }

    private void Draw(){
        if(difficultyLevel != 0) {
            connectButton.draw();
        }
        DrawText(difficultyMessage, 30, 380, 20, ColorEnum.WHITE.getColorUtils().toRaylibColor());
        DrawText(statusMessageRow1, 30, 400, 20, ColorEnum.WHITE.getColorUtils().toRaylibColor());
        DrawText(statusMessageRow2, 30, 420, 20, ColorEnum.WHITE.getColorUtils().toRaylibColor());
    }

    private void Write(String text){
        statusMessageRow1 = "";
        statusMessageRow2 = "";

        String[] parts = text.split(": ");
        difficultyMessage = parts[0] + ":";
        if( parts[1].length() > 42){
            String[] words = parts[1].split(" ");
            for(String t : words){
                if(statusMessageRow1.length() < 42){
                    statusMessageRow1 = statusMessageRow1.concat(t + " ");
                }
                else{
                    statusMessageRow2 = statusMessageRow2.concat(t + " ");
                }
            }
        }
        else{
            statusMessageRow1 = parts[1];
        }
    }

}
