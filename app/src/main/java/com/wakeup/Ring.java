package com.wakeup;


import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.util.Log;


import java.io.IOException;

public class Ring {
    private SoundPool mSoundPool;
    private AssetManager mAssetManager;
    private int mRingtonSound;
    private float volume;
    final String myLog = "myLog";

    public void start(Context context){
        mSoundPool = new SoundPool(1, AudioManager.STREAM_NOTIFICATION, 0);
        mAssetManager = context.getAssets();
        // получим идентификаторы
        mRingtonSound = loadSound("b.mp3");
        volume = (float) 0.2;

                // SLEEP 1 SECONDS HERE ...
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                playSound(mRingtonSound, volume);
            }
        }, 500);
    }


        private void playSound(int sound,float volume) {
        if (sound > 0) {
            float leftVolume = volume ;
            float rightVolume = volume ;
            int priority = 1;
            int is_loop = 1;//1-зациклено, 0- без повторения
            float normal_playback_rate = 1f;

            mSoundPool.play(sound, leftVolume, rightVolume, priority, is_loop,
                    normal_playback_rate);
            mSoundPool.setVolume(sound, volume, volume);
        }
    }


    private int loadSound(String fileName) {
        AssetFileDescriptor afd = null;
        try {
            afd = mAssetManager.openFd(fileName);
        } catch (IOException e) {
            Log.d(myLog, "LocActivity loadSound ОШИБКА ЗАГРУЗКИ ФАЙЛА ");
            e.printStackTrace();
            return -1;
        }
        return mSoundPool.load(afd, 1);
    }


    public int stopSound() {
        mSoundPool.stop(mRingtonSound);
        return 1 ;
    }

    public void pauseSound(){
        mSoundPool.pause(mRingtonSound);
    }

    public void resumeSound(){
        mSoundPool.resume(mRingtonSound);
    }

}
