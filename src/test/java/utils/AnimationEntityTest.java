package utils;

import com.raylib.Raylib.Texture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnimationEntityTest {

    static class AnimationEntityImpl extends AnimationEntity {}

    AnimationEntityImpl entity;

    Texture tex1 = new Texture(); // dummy textures
    Texture tex2 = new Texture();

    @BeforeEach
    void setUp() {
        entity = new AnimationEntityImpl();
    }

    @Test
    void testSetAndGetAnimations() {
        Texture[] framesBase = new Texture[]{tex1, tex2};
        Texture[] framesColor = new Texture[]{tex2, tex1};

        entity.setAnimationBase(AnimationState.IDLE_RIGHT, framesBase);
        entity.setAnimationColor(AnimationState.IDLE_RIGHT, framesColor);

        assertSame(framesBase, entity.animationsBase.get(AnimationState.IDLE_RIGHT));
        assertSame(framesColor, entity.animationsColor.get(AnimationState.IDLE_RIGHT));
    }

    @Test
    void testSetCurrentStateResetsFrames() {
        entity.setCurrentState(AnimationState.IDLE_RIGHT);
        entity.currentFrame = 1;
        entity.frameCounter = 5;

        entity.setCurrentState(AnimationState.LEFT);

        assertEquals(AnimationState.LEFT, entity.getCurrentState());
        assertEquals(0, entity.currentFrame);
        assertEquals(0, entity.frameCounter);
    }

    @Test
    void testUpdateAnimationCyclesFrames() {
        Texture[] frames = new Texture[]{tex1, tex2};
        entity.setAnimationBase(AnimationState.IDLE_RIGHT, frames);
        entity.setCurrentState(AnimationState.IDLE_RIGHT);
        entity.setFrameDelay(2);

        entity.frameCounter = 1;
        entity.currentFrame = 0;

        entity.updateAnimation(); // frameCounter -> 2, should update frame
        assertEquals(1, entity.currentFrame);
        assertEquals(0, entity.frameCounter);

        entity.updateAnimation(); // frameCounter -> 1
        assertEquals(1, entity.currentFrame);

        entity.updateAnimation(); // frameCounter -> 2, frame cycles back
        assertEquals(0, entity.currentFrame);
    }

    @Test
    void testGetTextureBaseAndColor() {
        Texture[] framesBase = new Texture[]{tex1, tex2};
        Texture[] framesColor = new Texture[]{tex2, tex1};
        entity.setAnimationBase(AnimationState.IDLE_RIGHT, framesBase);
        entity.setAnimationColor(AnimationState.IDLE_RIGHT, framesColor);
        entity.setCurrentState(AnimationState.IDLE_RIGHT);

        entity.currentFrame = 0;
        assertEquals(tex1, entity.getTextureBase());
        assertEquals(tex2, entity.getTextureColor());

        entity.currentFrame = 1;
        assertEquals(tex2, entity.getTextureBase());
        assertEquals(tex1, entity.getTextureColor());
    }

    @Test
    void testGetTextureBaseAndColorWhenNoFrames() {
        entity.setCurrentState(AnimationState.IDLE_RIGHT);
        assertNull(entity.getTextureBase());
        assertNull(entity.getTextureColor());
    }
}
