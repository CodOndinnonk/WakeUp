package com.wakeup;


import android.content.Context;
import android.os.Vibrator;


public class Vibration {//клас для вибрации
    Vibrator vibrator;
    long[] t1 = {1000,5000,1000,5000};//массив интервалов, первое число - время ожидпния в милисекундах, второе - кол-во времени вибрации

    public Vibration(Context context){
        vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void onVibration() {
        //задается длительность вибрации
        vibrator.vibrate(t1,0);//первое - масив временных интервалов для срабатывания, второе - с какого индекса масива идет повторение
      }

    public void offVibration() {
        vibrator.cancel();
    }


}
