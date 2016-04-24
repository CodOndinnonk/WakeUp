package com.wakeup;
import java.util.ArrayList;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import android.widget.Switch;
import android.widget.TextView;


public class BoxAdapter extends BaseAdapter  {
    Context context;
    LayoutInflater lInflater;
    ArrayList<Alarm> objects;
    final String myLog = "myLog";


    ResetAlarmState resetAlarmState;

    BoxAdapter(Context contextGet, ArrayList<Alarm> alarms) {
        context = contextGet;
        objects = alarms;

        resetAlarmState = new ResetAlarmState(context);
        lInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }

    // кол-во элементов
    @Override
    public int getCount() {
        return objects.size();
    }

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }

    // пункт списка
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(myLog, "getView");
        // используем созданные, но не используемые view
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.tabitem, parent, false);
        }

        Alarm alarmByPosition = getAlarm(position);

        // заполняем View в пункте списка данными из будильника
        if (alarmByPosition.get_minute() < 10) {
            ((TextView) view.findViewById(R.id.textTimeTabitem)).setText(alarmByPosition.get_hour() + ":0" + alarmByPosition.get_minute());
        } else {
            ((TextView) view.findViewById(R.id.textTimeTabitem)).setText(alarmByPosition.get_hour() + ":" + alarmByPosition.get_minute());
        }
        if (alarmByPosition.get_active() == 1) {
            ((Switch) view.findViewById(R.id.buttonOffOnTabitem)).setChecked(true);
            ((Switch) view.findViewById(R.id.buttonOffOnTabitem)).setTextColor(Color.GREEN);
        } else {
            ((Switch) view.findViewById(R.id.buttonOffOnTabitem)).setChecked(false);
            ((Switch) view.findViewById(R.id.buttonOffOnTabitem)).setTextColor(Color.RED);
        }

        Switch offNoButton = (Switch) view.findViewById(R.id.buttonOffOnTabitem);
        // присваиваем кнопке обработчик
        offNoButton.setOnClickListener(myButtonClickList);
        // пишем позицию
        offNoButton.setTag(position);

        return view;
    }

    // товар по позиции
    Alarm getAlarm(int position) {
        return ((Alarm) getItem(position));
    }


    View.OnClickListener myButtonClickList = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d(myLog, "ID нажатого будильника " + getAlarm(Integer.valueOf(view.getTag().toString())).getID());
            Alarm needAlarm = getAlarm(Integer.valueOf(view.getTag().toString()));
            if (needAlarm.get_active() == 1) {
                needAlarm.set_active(0);
            } else {
                needAlarm.set_active(1);
            }
            resetAlarmState.changeAlarmWork(needAlarm);
            resetAlarmState.setComandToRemakeAlarms();
        }
    };


}

