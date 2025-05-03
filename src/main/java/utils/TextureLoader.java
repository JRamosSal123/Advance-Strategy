package utils;

import com.raylib.Raylib.Texture;

public interface TextureLoader {
    Texture load(String path);
}
