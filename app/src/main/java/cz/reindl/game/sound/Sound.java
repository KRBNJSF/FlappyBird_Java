package cz.reindl.game.sound;

import android.media.AudioAttributes;
import android.media.SoundPool;

public class Sound {

    private final SoundPool soundPool;
    private boolean isSoundLoaded;

    //SOUND EFFECTS
    public int flapSound, collideSound, scoreSound, highScoreSound, barrierCollideSound, scytheFlap, reviveSound;

    //SOUND SETTINGS
    public Sound() {
        AudioAttributes audioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build();
        SoundPool.Builder builder = new SoundPool.Builder();
        builder.setAudioAttributes(audioAttributes).setMaxStreams(5);
        soundPool = builder.build();
        soundPool.setOnLoadCompleteListener((soundPoolC, Id, status) -> this.isSoundLoaded = true);
    }

    public SoundPool getSoundPool() {
        return soundPool;
    }

    public boolean isSoundLoaded() {
        return isSoundLoaded;
    }

}