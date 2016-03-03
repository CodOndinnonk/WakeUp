package com.wakeup;


    import android.app.Service;
    import android.content.Context;
    import android.content.Intent;
    import android.os.IBinder;
    import android.util.Log;

public class WakeLockService extends Service {

    public static final String ID = "id";
    final String myLog = "myLog";


    private Context mContext;
    /*extends method (non-Javadoc)
     * @see android.app.Service#onBind(android.content.Intent)
     */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }



        @Override
        public int onStartCommand(Intent intent,int flags , int startId){
            int alarmId  = intent.getIntExtra(ID, 999);
            Log.d(myLog,"WakeLockService onStartCommand alarmId = " + alarmId);
            Intent dialogIntent = new Intent(this, LocActivitySimple.class);
            //для запуска активити не из активити класса
            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            dialogIntent.putExtra(ID, alarmId);
            startActivity(dialogIntent);

            stopSelf();//останавливает работу данного сервиса, предотвращая повторное включение звонка после его отключения

        return Service.START_NOT_STICKY;
        }


        @Override
        public void onDestroy()
        {super.onDestroy();
        }

}

