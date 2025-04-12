package utils;

import com.raylib.Raylib.Texture;

public interface Animatable {
    public void updateAnimation();
    public Texture getTextureBase();
    public Texture getTextureColor();
}
