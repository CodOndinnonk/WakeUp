package com.wakeup;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;



import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Locale;


public class LocActivitySimple extends Activity   {
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
        setContentView(R.layout.activity_loc);
        camera = Camera.open();
        fieldForContent = (TextView)findViewById(R.id.showInfoFieldLocActivitySimple);
        mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        mAssetManager = getAssets();
        // получим идентификаторы
        mRingtonSound = loadSound("b.mp3");

        //включение экрана
        Activity activity = this;
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        int alarmId  = activity.getIntent().getIntExtra(ID, 999);
        Log.d(myLog, "LocActivity onCreate alarmId = " + alarmId);

        setContent(alarmId);

        onLight();
        //отменяем интент будильника, так как он уже сработал
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent AlarmReceiverIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, AlarmReceiverIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);

         // SLEEP 1 SECONDS HERE ...
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                playSound(mRingtonSound);
            }
        }, 500);


    }



    public void setContent(int needId){
        DatabaseHandler db = new DatabaseHandler(this);//переменная для работы с БД
        Alarm needAlarm = db.getAlarmById(needId);//создаем обьект типа "запись" и зполняем его данными из необходимой нам записи, взятой по Id
        fieldForContent.setText(needAlarm.get_content());//заполняем поле информациее, взятой из сохраненной ранее запис
    }

    public void onLight(){
        Camera.Parameters parameters = camera.getParameters();
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(parameters);
        camera.startPreview();
    }
    public void offLight(){
        Camera.Parameters parameters = camera.getParameters();
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camera.setParameters(parameters);
        camera.startPreview();
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


    public void StopSound(View view) {
        mSoundPool.stop(mRingtonSound);
        active = false;
        // SLEEP 1 SECONDS HERE ...
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                finish();
            }
        }, 2000);
        offLight();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_loc, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private static long back_pressed;
    @Override
    public void onBackPressed() {

    }








}
