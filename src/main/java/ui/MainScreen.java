package ui;

import utils.ColorUtils;
import utils.RectangleUtils;
import utils.ScreenUtils;

import static com.raylib.Raylib.*;

public class MainScreen {

    private final ColorUtils backgroundTop = new ColorUtils(65, 65, 65);
    private final ColorUtils backgroundBottom = new ColorUtils(40, 40, 40);
    private final ColorUtils colorWhite = new ColorUtils(255, 255, 255);

    private final Button iaButton;
    private final Button p2pButton;
    private final Button configButton;

    public MainScreen() {
        String path = "assets/screen/main/";

        iaButton = new Button(
                new RectangleUtils(140, 112, 200, 200),
                new ColorUtils(213, 139, 9),
                new ColorUtils(166, 106, 0),
                LoadTexture(path + "IAButton.png"),
                colorWhite
        );

        p2pButton = new Button(
                new RectangleUtils(460, 112, 200, 200),
                new ColorUtils(19, 66, 141),
                new ColorUtils(9, 48, 110),
                LoadTexture(path + "P2PButton.png"),
                colorWhite
        );

        configButton = new Button(
                new RectangleUtils(739, 18, 40, 40),
                new ColorUtils(200, 200, 200),
                new ColorUtils(150, 150, 150),
                LoadTexture(path + "ConfigButton.png"),
                colorWhite
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
        ScreenUtils.startFrame(backgroundTop, backgroundBottom);

        iaButton.draw();
        DrawText("1 Player", 140, 320, 50, colorWhite.toRaylibColor());
        p2pButton.draw();
        DrawText("2 Player", 460, 320, 50, colorWhite.toRaylibColor());
        configButton.draw();

        ScreenUtils.endFrame();
    }
}


