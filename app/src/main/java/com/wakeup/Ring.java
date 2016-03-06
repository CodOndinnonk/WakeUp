package com.wakeup;


import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import java.io.IOException;

public class Ring {
    private SoundPool mSoundPool;
    private AssetManager mAssetManager;
    private int mRingtonSound;
    final String myLog = "myLog";

    public void start(Context context){
        mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        mAssetManager = context.getAssets();
        // получим идентификаторы
        mRingtonSound = loadSound("b.mp3");

        // SLEEP 1 SECONDS HERE ...
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                playSound(mRingtonSound);
            }
        }, 500);

    }



    private void playSound(int sound) {
        if (sound > 0) {
            mSoundPool.play(sound, 1, 1, 1, -1, 1);
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
        // SLEEP 1 SECONDS HERE ...
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

            }
        }, 2000);
        return 1 ;
    }

}
