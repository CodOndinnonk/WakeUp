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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loc_activity_simple);

        alarmId  = this.getIntent().getIntExtra(ID, 999);
        Log.d(myLog, "LocActivity onCreate alarmId = " + alarmId);

        vibration = new Vibration(this);
        light = new Light();
        locActivityHelper = new LocActivityHelper(this,alarmId);

        //отменяем интент будильника, так как он уже сработал
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent AlarmReceiverIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, AlarmReceiverIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);

        locActivityHelper.makeWakeActivityFromSleep();
        ring = new Ring();
        ring.start(this);
        light.onLight();
        vibration.onVibration();
    }




    public void pushStopAlarmButton(View view) {
        int rezult = ring.stopSound();
        if (rezult == 1) {//сработал метод остановки аудио
            light.offLight();
            vibration.offVibration();
            //изменение активности будильника на ВЫКЛЮЧЕН
            locActivityHelper.changeAlarmWork();
            // перезапуск всех будильников
            locActivityHelper.setComandToRemakeAlarms();
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
