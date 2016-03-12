package com.wakeup;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmSetter extends BroadcastReceiver {
    final String myLog = "myLog";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(myLog, "AlarmSetter onReceive 1 ");
        Intent service = new Intent(context, AlarmService.class);
        service.setAction(AlarmService.DOWHATNEED);
        context.startService(service);
    }

}
