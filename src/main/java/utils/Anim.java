package utils;

import static com.raylib.Colors.*;
import static com.raylib.Raylib.*;


public class Anim {

    static final int MAX_FRAME_DELAY = 20;
    static final int MIN_FRAME_DELAY = 1;
    static final int TOTAL_FRAMES = 3; // Número de imágenes: frame_0.png ... frame_5.png

    public static void main() {

        int screenWidth = 800;
        int screenHeight = 450;

        InitWindow(screenWidth, screenHeight, "raylib-java [textures] - sprite animation");

        // Cargar frames como texturas
        Texture[] frames = new Texture[TOTAL_FRAMES];
        for (int i = 0; i < TOTAL_FRAMES; i++) {
            frames[i] = LoadTexture("src/assets/sprites/soldier"+i+".png");
        }

        int currentFrame = 0;
        int frameCounter = 0;
        int frameDelay = 8;

        SetTargetFPS(60);

        while (!WindowShouldClose()) {
            // Actualizar lógica de animación
            frameCounter++;
            if (frameCounter >= frameDelay) {
                currentFrame = (currentFrame + 1) % TOTAL_FRAMES;
                frameCounter = 0;
            }

            // Dibujo
            BeginDrawing();
            ClearBackground(RAYWHITE);

            DrawText("Sprite Animation Example", 20, 20, 20, DARKGRAY);
            DrawText("Current Frame: " + currentFrame, 20, 50, 20, GRAY);
            DrawText("Frame Delay: " + frameDelay, 20, 80, 20, GRAY);
            DrawText("Use LEFT/RIGHT to change speed", 20, 110, 20, MAROON);

            // Dibujar el frame actual centrado
            Texture tex = frames[currentFrame];
            DrawTexture(tex, screenWidth / 2 - tex.width() / 2, screenHeight / 2 - tex.height() / 2, WHITE);

            EndDrawing();
        }

        // Liberar recursos
        for (Texture tex : frames) {
            UnloadTexture(tex);
        }

        CloseWindow();
    }

}
