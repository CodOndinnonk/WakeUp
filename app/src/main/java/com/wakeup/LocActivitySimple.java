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


public class LocActivitySimple extends Activity implements CommonForLocActivities   {
    final String myLog = "myLog";
    public static final String ID = "id";
    Ring ring;
    Vibration vibration;
    Light light;
    int alarmId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loc_activity_simple);

        vibration = new Vibration(this);
        light = new Light();

        alarmId  = this.getIntent().getIntExtra(ID, 999);
        Log.d(myLog, "LocActivity onCreate alarmId = " + alarmId);


        //отменяем интент будильника, так как он уже сработал
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent AlarmReceiverIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, AlarmReceiverIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);

        makeWakeActivityFromSleep();
        ring = new Ring();
        ring.start(this);
        light.onLight();
        vibration.onVibration();
    }

    public void makeWakeActivityFromSleep(){
        //включение экрана
        Activity activity = this;
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


    public void pushStopAlarmButton(View view) {
        stopAlarm();
    }
    @Override
    public void stopAlarm() {
        int rezult = ring.stopSound();
        if(rezult == 1){//сработал метод остановки аудио
            light.offLight();
            vibration.offVibration();
            //изменение активности будильника на ВЫКЛЮЧЕН
            changeAlarmWork(alarmId);
            // перезапуск всех будильников
            setComandToRemakeAlarms();
            goToShowContent();
            finish();
        }
    }


    public void setComandToRemakeAlarms(){
        Intent setAlarmIntent = new Intent(this, AlarmService.class);
        setAlarmIntent.setAction(AlarmService.DOWHATNEED);
        this.startService(setAlarmIntent);
    }

    public void goToShowContent(){
        Intent goToShowContent = new Intent(this,ShowContent.class);
        goToShowContent.putExtra(ID, alarmId);
        startActivity(goToShowContent);
    }

    public void changeAlarmWork(int id){
        DatabaseHandler db = new DatabaseHandler(this);//переменная для работы с БД
        Alarm needAlarm = db.getAlarmById(id);
        db.updateAlarm(new Alarm(needAlarm.getID(), needAlarm.get_hour(), needAlarm.get_minute(), 0, needAlarm.get_content(), needAlarm.get_everyDay(), needAlarm.get_Sound()));
    }


    @Override
    public void onBackPressed() {//нажатие физической кнопки НАЗАД

    }

    @Override
    protected void onUserLeaveHint() {//нажатие физической кнопки ДОМОЙ
        super.onUserLeaveHint();
        Log.d(myLog, "нажали кнопку HOME ");

    }



}
