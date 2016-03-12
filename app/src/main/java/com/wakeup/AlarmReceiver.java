package com.wakeup;



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    public static final String ID = "id";
    final String myLog = "myLog";




    @Override
    public void onReceive(Context context, Intent intent) {
        int alarmId  = intent.getIntExtra(ID, 999);
        Log.d(myLog,"AlarmReceiver onReceive alarmId = " + alarmId);

        Intent wakeLockServiceIntent = new Intent(context, WakeLockService.class);
        wakeLockServiceIntent.putExtra(ID, alarmId);
        context.startService(wakeLockServiceIntent);
      }







}