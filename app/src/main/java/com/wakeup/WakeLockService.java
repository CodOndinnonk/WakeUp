package com.wakeup;


    import android.app.Service;

    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.os.IBinder;
    import android.preference.PreferenceManager;
    import android.util.Log;



public class WakeLockService extends Service {
    SharedPreferences sharedPreferences;
    Intent dialogIntent;
    public static final String ID = "id";
    final String myLog = "myLog";
    String[] namesOfLocActivities;


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

            //берем список возможных вариантов названий экранов блокировки из Strings, для дальнейшей проверки и  определения нужного экрана
            namesOfLocActivities = getResources().getStringArray(R.array.entriesLoc);

            // получаем SharedPreferences, которое работает с файлом настроек
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            String kindOfScreenLoc = sharedPreferences.getString("listLocActivity", "non");

            Log.d(myLog,"WakeLockService kindOfScreenLoc = " + kindOfScreenLoc);
            if(kindOfScreenLoc.equals(namesOfLocActivities[0])) {//если выбран первый пункт - Простой экран
                dialogIntent = new Intent(this, LocActivitySimple.class);
            }
            if(kindOfScreenLoc.equals(namesOfLocActivities[1])) {//если выбран второй пункт - Математический экран
                dialogIntent = new Intent(this, LocActivityArithmetic.class);
            }

            //для запуска активити не из активити класса
            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            dialogIntent.putExtra(ID, alarmId);
            startActivity(dialogIntent);

            stopSelf();//останавливает работу данного сервиса, предотвращая повторное включение звонка после его отключения

        return Service.START_NOT_STICKY;
        }


        @Override
        public void onDestroy(){
            super.onDestroy();
        }

}

