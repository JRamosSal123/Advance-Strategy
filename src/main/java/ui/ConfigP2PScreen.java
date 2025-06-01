package ui;

import utils.ColorEnum;
import utils.RectangleUtils;
import utils.ScreenUtils;

import java.util.Objects;

import static com.raylib.Raylib.*;

public class ConfigP2PScreen {
    //private final ColorUtils backgroundTop = new ColorUtils(65, 65, 65);
    //private final ColorUtils backgroundBottom = new ColorUtils(40, 40, 40);
    //private final ColorUtils colorWhite = new ColorUtils(255, 255, 255);
    //private final ColorUtils greyTop = new ColorUtils(65, 65, 65);
    //private final ColorUtils greyBottom = new ColorUtils(40, 40, 40);
    //private final ColorUtils greyLight = new ColorUtils(200, 200, 200);
    //private final ColorUtils greyDark = new ColorUtils(150, 150, 150);
    //private final ColorUtils blueLight = new ColorUtils(19, 66, 141);
    //private final ColorUtils blueDark = new ColorUtils(9, 48, 110);

    private final Button hostButton;
    private final Button clientButton;
    private final Button connectButton;
    private final Button exitButton;

    private final RectangleUtils rectIpField = new RectangleUtils(320, 50, 370, 45);
    private final RectangleUtils rectPortField = new RectangleUtils(320, 145, 370, 45);

    private Vector2 mousePosition;
    private int serverRole = -1;
    private boolean isEditingIp = false;
    private boolean isEditingPort = false;
    private String inputIpAddress = "";
    private String inputPort = "";
    private boolean isFinished = false;

    private String statusMessage = "";

    public ConfigP2PScreen() {
        hostButton = new Button(
            new RectangleUtils(135,107,210,210),
            ColorEnum.BLUE_LIGHT.getColorUtils(),
            ColorEnum.BLUE_DARK.getColorUtils(),
            "HOST",
            50,
            ColorEnum.WHITE.getColorUtils()
        );

        clientButton = new Button(
            new RectangleUtils(455,107,210,210),
            ColorEnum.BLUE_LIGHT.getColorUtils(),
            ColorEnum.BLUE_DARK.getColorUtils(),
            "CLIENT",
            50,
            ColorEnum.WHITE.getColorUtils()
        );

        connectButton = new Button(
            new RectangleUtils(395,385,130,60),
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

    public String getIp() {
        return inputIpAddress;
    }

    public int getPort() {
        return inputPort.isEmpty() ? -1 : Integer.parseInt(inputPort);
    }

    public void runScreen() {
        while (!WindowShouldClose() && !isFinished) {
            mousePosition = GetMousePosition();

            if (serverRole == -1) {
                handleRoleSelection();
            } else if (serverRole == 0) {
                inputIpAndPort();
                if (isFinished) isFinished = validateIpAndPort();
            } else if (serverRole == 1) {
                inputPort();
                if (isFinished) isFinished = validatePort();
            }
        }

        if (!validateIp()) inputIpAddress = "";
        if (!validatePort()) inputPort = "";

        if (!WindowShouldClose() && isFinished) {

            ScreenUtils.startFrame(ColorEnum.GREY_TOP.getColorUtils(), ColorEnum.GREY_BOTTOM.getColorUtils());
            DrawText("Esperando que el contrincante se conecte", 40, 210, 30, ColorEnum.WHITE.getColorUtils().toRaylibColor());
            ScreenUtils.endFrame();
        }
    }

    private void handleRoleSelection() {
        hostButton.update(mousePosition);
        clientButton.update(mousePosition);
        exitButton.update(mousePosition);

        if (hostButton.isClicked(mousePosition) && serverRole == -1) {
            serverRole = 1;
            statusMessage = "Elija un puerto válido";
        }
        if (clientButton.isClicked(mousePosition) && serverRole == -1) {
            serverRole = 0;
            statusMessage = "Introduzca la IP del compañero y el puerto";
        }
        if(exitButton.isClicked(mousePosition) && serverRole == -1) {
            isFinished = true;
        }

        ScreenUtils.startFrame(ColorEnum.GREY_TOP.getColorUtils(), ColorEnum.GREY_BOTTOM.getColorUtils());
        DrawText("Seleccione el rol con el que quiere conectarse", 40, 350, 30, ColorEnum.WHITE.getColorUtils().toRaylibColor());

        hostButton.draw();
        clientButton.draw();
        exitButton.draw();

        ScreenUtils.endFrame();
    }

    private void inputPort() {
        connectButton.update(mousePosition);
        exitButton.update(mousePosition);

        if (mousePosition != null && IsMouseButtonPressed(0)) {
            isEditingPort = ScreenUtils.isMouseOver(rectPortField, mousePosition);
            if (connectButton.isClicked(mousePosition)) {
                isFinished = true;
            }
            if (exitButton.isClicked(mousePosition)) {
                serverRole = -1;
                inputPort = "";
            }
        }
        int key = GetKeyPressed();
        readPortInput(key);

        ScreenUtils.startFrame(ColorEnum.GREY_TOP.getColorUtils(), ColorEnum.GREY_BOTTOM.getColorUtils());
        drawPortInput();
        ScreenUtils.endFrame();
    }

    private void inputIpAndPort() {
        connectButton.update(mousePosition);
        exitButton.update(mousePosition);

        if (mousePosition != null && IsMouseButtonPressed(0)) {
            if (ScreenUtils.isMouseOver(rectIpField, mousePosition)) {
                isEditingIp = true;
                isEditingPort = false;
            } else if (ScreenUtils.isMouseOver(rectPortField, mousePosition)) {
                isEditingIp = false;
                isEditingPort = true;
            } else {
                isEditingIp = false;
                isEditingPort = false;
            }

            if (connectButton.isClicked(mousePosition)) {
                isFinished = true;
            }
            if (exitButton.isClicked(mousePosition)) {
                serverRole = -1;
                inputPort = "";
                inputIpAddress = "";
            }
        }

        int key = GetKeyPressed();
        readIpInput(key);
        readPortInput(key);

        ScreenUtils.startFrame(ColorEnum.GREY_TOP.getColorUtils(), ColorEnum.GREY_BOTTOM.getColorUtils());
        drawIpAndPortInput();
        ScreenUtils.endFrame();
    }

    private void readIpInput(int key) {
        if (key > 319 && key < 330) key -= 272;

        if (isEditingIp && (Character.isDigit(key) || key == '.' || key == 259)) {
            if (key == 259 && !inputIpAddress.isEmpty()) {
                inputIpAddress = inputIpAddress.substring(0, inputIpAddress.length() - 1);
            } else if (inputIpAddress.length() < 15 && key != 259) {
                inputIpAddress += (char) key;
            }
        }
    }

    private void readPortInput(int key) {
        if (key > 319 && key < 330) key -= 272;

        if (isEditingPort && (Character.isDigit(key) || key == 259)) {
            if (key == 259 && !inputPort.isEmpty()) {
                inputPort = inputPort.substring(0, inputPort.length() - 1);
            } else if (inputPort.length() < 5 && key != 259) {
                inputPort += (char) key;
            }
        }
    }

    private boolean validateIpAndPort() {
        return serverRole == 1 || (validateIp() && validatePort());
    }

    private boolean validateIp() {

        if (Objects.equals(inputIpAddress, "")) {
            statusMessage = "Por favor rellene la dirección IP";
            return false;
        }
        String[] parts = inputIpAddress.split("\\.");
        if (parts.length != 4) {
            statusMessage = "Formato de IP incorrecto";
            return false;
        }
        for (String part : parts) {
            int n = Integer.parseInt(part);
            if (n <= 0 || n > 255) {
                statusMessage = "Los valores de IP deben estar entre [1-255]";
                return false;
            }
        }
        return true;
    }

    private boolean validatePort() {
        if (inputPort.isEmpty()) {
            statusMessage = "Por favor rellene el puerto";
            return false;
        }
        int n = Integer.parseInt(inputPort);
        if (n <= 1023 || n > 65535) {
            statusMessage = "El puerto debe estar entre [1024-65535]";
            return false;
        }
        return true;
    }

    private void drawPortInput() {
        if(isEditingPort){
            DrawRectangleRounded(rectPortField.toRaylibRectangle(), 0.5f, 100, ColorEnum.GREY_DARK.getColorUtils().toRaylibColor());
        }
        else {
            DrawRectangleRounded(rectPortField.toRaylibRectangle(), 0.5f, 100, ColorEnum.GREY_LIGHT.getColorUtils().toRaylibColor());
        }

        DrawText("Puerto:", 100, 145, 50, ColorEnum.WHITE.getColorUtils().toRaylibColor());
        DrawText(inputPort, 335, 145, 50, ColorEnum.WHITE.getColorUtils().toRaylibColor());

        DrawText(statusMessage, 30, 270, 20, ColorEnum.WHITE.getColorUtils().toRaylibColor());

        connectButton.draw();
        exitButton.draw();

    }

    private void drawIpAndPortInput() {
        if(isEditingIp){
            DrawRectangleRounded(rectIpField.toRaylibRectangle(), 0.5f, 100, ColorEnum.GREY_DARK.getColorUtils().toRaylibColor());
        }
        else {
            DrawRectangleRounded(rectIpField.toRaylibRectangle(), 0.5f, 100, ColorEnum.GREY_LIGHT.getColorUtils().toRaylibColor());
        }

        DrawText("IP:", 100, 50, 50, ColorEnum.WHITE.getColorUtils().toRaylibColor());
        DrawText(inputIpAddress, 335, 50, 50, ColorEnum.WHITE.getColorUtils().toRaylibColor());

        if(isEditingPort){
            DrawRectangleRounded(rectPortField.toRaylibRectangle(), 0.5f, 100, ColorEnum.GREY_DARK.getColorUtils().toRaylibColor());
        }
        else {
            DrawRectangleRounded(rectPortField.toRaylibRectangle(), 0.5f, 100, ColorEnum.GREY_LIGHT.getColorUtils().toRaylibColor());
        }

        DrawText("Puerto:", 100, 145, 50, ColorEnum.WHITE.getColorUtils().toRaylibColor());
        DrawText(inputPort, 335, 145, 50, ColorEnum.WHITE.getColorUtils().toRaylibColor());

        DrawText(statusMessage, 30, 270, 20, ColorEnum.WHITE.getColorUtils().toRaylibColor());

        connectButton.draw();
        exitButton.draw();

    }

}
