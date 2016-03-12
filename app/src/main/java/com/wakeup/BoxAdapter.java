package com.wakeup;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class BoxAdapter extends BaseAdapter implements View.OnClickListener {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Alarm> objects;
    final String myLog = "mylLog";

    BoxAdapter(Context context, ArrayList<Alarm> notes) {
        Log.d(myLog, "BoxAdapter");
        ctx = context;
        objects = notes;
        lInflater = (LayoutInflater) ctx
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
        Log.d(myLog, "BoxAdapter getItem 1 ");
        return objects.get(position);
    }


    // id по позиции
    @Override
    public long getItemId(int position) {
       Log.d(myLog, "BoxAdapter getItemId 1 ");
        return position;
    }

    // пункт списка
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       Log.d(myLog, "BoxAdapter getView 1 ");
        // используем созданные, но не используемые view
        View view = convertView;

        if (view == null) {
            view = lInflater.inflate(R.layout.tabitem, parent, false);
        }

        Alarm p = getNote(position);

        // заполняем View в пункте списка данными из товаров: наименование, цена
        // и картинка
        String minuteStr;
        if(p.get_minute()<10){
            minuteStr = ("0"+p.get_minute()).toString();}
        else {minuteStr = String.valueOf(p.get_minute());}
        ((TextView) view.findViewById(R.id.textime)).setText((p.get_hour()+ ":"

                + minuteStr).toString());

        ((TextView) view.findViewById(R.id.textEveryday)).setText(String.valueOf(p.getID()));

       /* if(p.get_everyDay() == 1) {
            ((TextView) view.findViewById(R.id.textEveryday)).setText("ежедневно");
        }else {((TextView) view.findViewById(R.id.textEveryday)).setText("");}
*/
        if(p.get_active() == 1) {
            ((ToggleButton) view.findViewById(R.id.buttonOffOn)).setChecked(true);
        }else {((ToggleButton) view.findViewById(R.id.buttonOffOn)).setChecked(false);

        }

        return view;

    }

    // товар по позиции
    Alarm getNote(int position) {
        Log.d(myLog, "BoxAdapter getNote 1 ");
        return ((Alarm) getItem(position));
    }

    @Override
    public void onClick(View v) {
        Log.d(myLog, "НАЖАЛ");
    }
}