package com.wakeup;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class LocActivitySimple extends Activity {
    final String myLog = "myLog";
    public static final String ID = "id";
    Ring ring;
    Vibration vibration;
    Light light;
    int alarmId;
    LocActivityHelper locActivityHelper;
    ResetAlarmState resetAlarmState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loc_activity_simple);

        alarmId  = this.getIntent().getIntExtra(ID, 999);
        Log.d(myLog, "LocActivitySimple onCreate alarmId = " + alarmId);


        vibration = new Vibration(this);
        Log.d(myLog, "LocActivitySimple 1");
        light = new Light();
        Log.d(myLog, "LocActivitySimple 2");
        locActivityHelper = new LocActivityHelper(this,alarmId);
        Log.d(myLog, "LocActivitySimple 3");
        resetAlarmState = new ResetAlarmState(this);
        Log.d(myLog, "LocActivitySimple 4");

        //отменяем интент будильника, так как он уже сработал
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Log.d(myLog, "LocActivitySimple 5");
        Intent AlarmReceiverIntent = new Intent(this, AlarmReceiver.class);
        Log.d(myLog, "LocActivitySimple 6");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, AlarmReceiverIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        Log.d(myLog, "LocActivitySimple 7");
        alarmManager.cancel(pendingIntent);
        Log.d(myLog, "LocActivitySimple 8");

        locActivityHelper.makeWakeActivityFromSleep();
        Log.d(myLog, "LocActivitySimple 9");
        ring = new Ring();
        Log.d(myLog, "LocActivitySimple 10");
        ring.start(this);
        Log.d(myLog, "LocActivitySimple 11");
        light.onLight();
        Log.d(myLog, "LocActivitySimple 12");
        vibration.onVibration();
        Log.d(myLog, "LocActivitySimple 13");
    }




    public void pushStopAlarmButton(View view) {
        Log.d(myLog, "LocActivity pushStopAlarmButton");
        int rezult = ring.stopSound();
        if (rezult == 1) {//сработал метод остановки аудио
            light.offLight();
            vibration.offVibration();
            //изменение активности будильника на ВЫКЛЮЧЕН
            resetAlarmState.changeAlarmWork(alarmId);
            // перезапуск всех будильников
            resetAlarmState.setComandToRemakeAlarms();
            locActivityHelper.goToShowContent();
            finish();
        }
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
