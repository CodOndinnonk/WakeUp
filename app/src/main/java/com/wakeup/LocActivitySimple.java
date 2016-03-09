package com.wakeup;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;



import java.io.IOException;


public class LocActivitySimple extends Activity   {
    final String myLog = "myLog";
    public static final String ID = "id";
    TextView fieldForContent;
    Ring ring;
    Vibration vibration;
    Light light;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loc_activity_simple);
        fieldForContent = (TextView)findViewById(R.id.showInfoFieldLocActivitySimple);

        vibration = new Vibration(this);
        light = new Light();

        //включение экрана
        Activity activity = this;
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        int alarmId  = activity.getIntent().getIntExtra(ID, 999);
        Log.d(myLog, "LocActivity onCreate alarmId = " + alarmId);

        setContent(alarmId);


        //отменяем интент будильника, так как он уже сработал
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent AlarmReceiverIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, AlarmReceiverIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);


        ring = new Ring();
        ring.start(this);
        light.onLight();
        vibration.onVibration();
    }



    public void setContent(int needId){
        DatabaseHandler db = new DatabaseHandler(this);//переменная для работы с БД
        Alarm needAlarm = db.getAlarmById(needId);//создаем обьект типа "запись" и зполняем его данными из необходимой нам записи, взятой по Id
        fieldForContent.setText(needAlarm.get_content());//заполняем поле информациее, взятой из сохраненной ранее запис
    }



    public void stopAlarm(View view) {
        int rezult = ring.stopSound();
        if(rezult == 1){//сработал метод остановки аудио
            light.offLight();
            vibration.offVibration();
            finish();
        }
    }



    private static long back_pressed;
    @Override
    public void onBackPressed() {

    }



}
