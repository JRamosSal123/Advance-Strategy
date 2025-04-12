package utils;

import com.raylib.Raylib.*;
import java.util.HashMap;

public abstract class AnimationEntity implements Animatable {
    protected Texture[] frames;
    protected HashMap<AnimationState, Texture[]> animationsBase = new HashMap<>();
    protected HashMap<AnimationState, Texture[]> animationsColor = new HashMap<>();
    protected AnimationState state = AnimationState.IDLE_RIGHT;

    protected int currentFrame = 0;
    protected int frameCounter = 0;
    protected int frameDelay = 1000;


    public void setAnimationBase(AnimationState state, Texture[] frames) {
        animationsBase.put(state, frames);
    }
    public void setAnimationColor(AnimationState state, Texture[] frames) {
        animationsColor.put(state, frames);
    }

    public void setCurrentState(AnimationState state) {
        if (this.state != state) {
            this.state = state;
            currentFrame = 0;
            frameCounter = 0;
        }
    }

    public AnimationState getCurrentState() {
        return state;
    }

    public int getFrameDelay() {
        return frameDelay;
    }
    public void setFrameDelay(int delay) {
        this.frameDelay = delay;
    }

    @Override
    public void updateAnimation() {
        Texture[] frames = animationsBase.get(state);
        if (frames == null || frames.length == 0) return;

        frameCounter++;
        if (frameCounter >= frameDelay) {
            currentFrame = (currentFrame + 1) % frames.length;
            frameCounter = 0;
        }
    }

    @Override
    public Texture getTextureBase() {
        Texture[] frames = animationsBase.get(state);
        return (frames != null && frames.length > 0) ? frames[currentFrame] : null;
    }

    @Override
    public Texture getTextureColor() {
        Texture[] frames = animationsColor.get(state);
        return (frames != null && frames.length > 0) ? frames[currentFrame] : null;
    }
}
