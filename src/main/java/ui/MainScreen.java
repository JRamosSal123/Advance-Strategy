package ui;

import utils.ColorEnum;
import utils.RectangleUtils;
import utils.ScreenUtils;
import static com.raylib.Raylib.*;

public class MainScreen {

    private final Button iaButton;
    private final Button p2pButton;
    private final Button configButton;

    public MainScreen() {
        String path = "assets/screen/";

        iaButton = new Button(
                new RectangleUtils(140, 112, 200, 200),
                ColorEnum.ORANGE_LIGHT.getColorUtils(),
                ColorEnum.ORANGE_DARK.getColorUtils(),
                LoadTexture(path + "IAButton.png"),
                ColorEnum.WHITE.getColorUtils()
        );

        p2pButton = new Button(
                new RectangleUtils(460, 112, 200, 200),
                ColorEnum.BLUE_LIGHT.getColorUtils(),
                ColorEnum.BLUE_DARK.getColorUtils(),
                LoadTexture(path + "P2PButton.png"),
                ColorEnum.WHITE.getColorUtils()
        );

        configButton = new Button(
                new RectangleUtils(739, 18, 40, 40),
                ColorEnum.GREY_LIGHT.getColorUtils(),
                ColorEnum.GREY_DARK.getColorUtils(),
                LoadTexture(path + "ConfigButton.png"),
                ColorEnum.WHITE.getColorUtils()
        );
    }

    public Screen run() {
        while (!WindowShouldClose()) {
            Vector2 mousePos = GetMousePosition();

            updateButtons(mousePos);

            if (IsMouseButtonReleased(0)) {
                if (iaButton.isHovered()){
                    return Screen.DIFFICULTY_IA;
                }
                if (p2pButton.isHovered()){
                    return Screen.CONFIG_P2P;
                }
            }

            draw();
        }
        return null;
    }

    private void updateButtons(Vector2 mousePos) {
        iaButton.update(mousePos);
        p2pButton.update(mousePos);
        configButton.update(mousePos);
    }

    private void draw() {
        ScreenUtils.startFrame(ColorEnum.GREY_TOP.getColorUtils(), ColorEnum.GREY_BOTTOM.getColorUtils());

        iaButton.draw();
        DrawText("1 Player", 140, 320, 50, ColorEnum.WHITE.getColorUtils().toRaylibColor());
        p2pButton.draw();
        DrawText("2 Player", 460, 320, 50, ColorEnum.WHITE.getColorUtils().toRaylibColor());
        configButton.draw();

        ScreenUtils.endFrame();
    }
}


