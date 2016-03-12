package com.wakeup;


import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AlarmService extends IntentService {


    public static final String DOWHATNEED = "DOWHATNEED";
    final static String myLog = "myLog";
    private IntentFilter matcher;
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String TIME_HOUR = "timeHour";
    public static final String TIME_MINUTE = "timeMinute";
    public static final String TONE = "alarmTone";
    public static final String ACTION_ALARM_CHANGED = "android.intent.action.ALARM_CHANGED";
    ArrayList<Alarm> alarms = new ArrayList<Alarm>();

    public AlarmService() {
        super(null);
        matcher = new IntentFilter();
        matcher.addAction(DOWHATNEED);

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        if (matcher.matchAction(action)) {//если пеереданное значение совпадает с создать или отменить
            execute(action);
        }
    }


    private void execute(String action) {
        fillData();
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        long time = 0;
        for (Alarm alarm : alarms) {
            Intent intentForAlarmreceiver = new Intent(this, AlarmReceiver.class);
            intentForAlarmreceiver.putExtra(ID, alarm.getID());
            intentForAlarmreceiver.putExtra(TIME_HOUR, alarm.get_hour());
            intentForAlarmreceiver.putExtra(TIME_MINUTE, alarm.get_minute());

            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int) alarm.getID(), intentForAlarmreceiver,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, alarm.get_hour());
            calendar.set(Calendar.MINUTE, alarm.get_minute());
            calendar.set(Calendar.SECOND, 00);

            if (DOWHATNEED.equals(action)) {//вариант, когда программа смотрин на флаг активности будильника в БД
                if (alarm.get_active() == 1) {//если стоит АКТИВЕН
                    if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
                        time = calendar.getTimeInMillis() + 86400000;// ставим будильник на следующие сутки, если время уже прошло
                    } else {
                        time = calendar.getTimeInMillis();
                    }
                    alarmManager.cancel(pendingIntent);//отключаем будильник
                    alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);//ставим его заного
                } else {//если стоит флаг НЕ АКТИВЕН
                    alarmManager.cancel(pendingIntent);//отключаем будильник
                }
                Log.d(myLog, "Время срабатывания DOWHATNEED = " + time);
            }
        }
    }



    void fillData() {
        DatabaseHandler db = new DatabaseHandler(this);//переменная для работы с БД
        List<Alarm> listGetAlarms = db.getAllAlarms();//создание списка обьектов типа "запись" и заполнения его значениями всех записей взятых из БД

        for (Alarm cn : listGetAlarms) {//проходим про каждому обьекту списка
            alarms.add(new Alarm(cn.getID(), cn.get_hour(), cn.get_minute(), cn.get_active(), cn.get_content(),
                    cn.get_everyDay(), cn.get_Sound()));
        }

    }







}
