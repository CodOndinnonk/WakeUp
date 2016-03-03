package com.wakeup;

import android.app.Activity;
import android.content.res.AssetManager;
import android.hardware.Camera;
import android.media.SoundPool;
import android.os.Bundle;
import android.widget.TextView;

public class LocActivityArithmetic extends Activity {
    private Camera camera;
    final String myLog = "myLog";
    public static final String ID = "id";
    private SoundPool mSoundPool;
    private AssetManager mAssetManager;
    private int mRingtonSound;
    TextView fieldForContent;
    boolean active = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loc_activity_arithmetic);
    }
}
