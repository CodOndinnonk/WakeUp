package com.wakeup;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.hardware.Camera;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class LocActivityArithmetic extends Activity {
    final String myLog = "myLog";
    public static final String ID = "id";
    Ring ring;
    TextView showTaskField;
    TextView enterAnswerField;
    Button btn1, btn2, btn3, btn4, btn5,  btn6, btn7, btn8, btn9, btn0, btnDel;
    String task, rezult, rightAnswer;
    Random random = new Random();
    int alarmId;
    Vibration vibration;
    Light light;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loc_activity_arithmetic);

        showTaskField = (TextView)findViewById(R.id.showTaskFieldLocActivityArithmetic);
        enterAnswerField = (TextView)findViewById(R.id.fieldForAnswerLocActivityArithmetic);
        btn0 = (Button)findViewById(R.id.button0ActivityArithmetic);
        btn1 = (Button)findViewById(R.id.button1ActivityArithmetic);
        btn2 = (Button)findViewById(R.id.button2ActivityArithmetic);
        btn3 = (Button)findViewById(R.id.button3ActivityArithmetic);
        btn4 = (Button)findViewById(R.id.button4ActivityArithmetic);
        btn5 = (Button)findViewById(R.id.button5ActivityArithmetic);
        btn6 = (Button)findViewById(R.id.button6ActivityArithmetic);
        btn7 = (Button)findViewById(R.id.button7ActivityArithmetic);
        btn8 = (Button)findViewById(R.id.button8ActivityArithmetic);
        btn9 = (Button)findViewById(R.id.button9ActivityArithmetic);
        btnDel = (Button)findViewById(R.id.buttondeleteActivityArithmetic);

        vibration = new Vibration(this);
        light = new Light();
        rezult = "";

        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operateWithAnswer(new String("0"));
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operateWithAnswer(new String("1"));
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operateWithAnswer(new String("2"));
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operateWithAnswer(new String("3"));
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operateWithAnswer(new String("4"));
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operateWithAnswer(new String("5"));
            }
        });
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operateWithAnswer(new String("6"));
            }
        });
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operateWithAnswer(new String("7"));
            }
        });
        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operateWithAnswer(new String("8"));
            }
        });
        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operateWithAnswer(new String("9"));
            }
        });
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operateWithAnswer(new String("del"));
            }
        });


        //включение экрана
        Activity activity = this;
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        alarmId  = activity.getIntent().getIntExtra(ID, 999);


        //отменяем интент будильника, так как он уже сработал
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent AlarmReceiverIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, AlarmReceiverIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);



        setTask(100);
        ring = new Ring();
        ring.start(this);
        light.onLight();
        vibration.onVibration();
    }

    public void changeAlarmWork(int id){
        DatabaseHandler db = new DatabaseHandler(this);//переменная для работы с БД
        Alarm needAlarm = db.getAlarmById(id);
        db.updateAlarm(new Alarm(needAlarm.getID(), needAlarm.get_hour(), needAlarm.get_minute(), 0, needAlarm.get_content(), needAlarm.get_everyDay(), needAlarm.get_Sound()));
    }


    public void operateWithAnswer(String selectedButton){
        if(selectedButton.equals("del")){
            if(rezult.length()>0) {
                Log.d(myLog,"delete");
                rezult = rezult.substring(0, rezult.length() - 1);//удаляем последний символ
            }
        }else{
            rezult += selectedButton;
        }
            enterAnswerField.setText(rezult);
         if(rezult.equals(rightAnswer)){
            stopAlarm();
        }

    }

    public void setTask(int maxNumber){//число будет генерироваться от 0, до maxNumber
        int number1 = random.nextInt(maxNumber);
        int number2 = random.nextInt(maxNumber);
        int sign = random.nextInt(2);//генерация числа для определения знака операции

        if(sign==1){
            task = String.valueOf(number1) + " + " + String.valueOf(number2) + " = ";
            rightAnswer = String.valueOf(number1 + number2);
        }
        if(sign==0){
            if(number1 >= number2) {
                task = String.valueOf(number1) + " - " + String.valueOf(number2) + " = ";
                rightAnswer = String.valueOf(number1 - number2);
            }else {
                task = String.valueOf(number2) + " - " + String.valueOf(number1) + " = ";
                rightAnswer = String.valueOf(number2 - number1);
            }
        }
        showTaskField.setText(task);
    }







    public void stopAlarm() {
        int rezult = ring.stopSound();
        if(rezult == 1){//сработал метод остановки аудио
            light.offLight();
            vibration.offVibration();
            //изменение активности будильника на ВЫКЛЮЧЕН
            changeAlarmWork(alarmId);
            // перезапуск всех будильников
            setComandToRemakeAlarms();

            finish();
        }
    }

    public void setComandToRemakeAlarms(){
        Intent setAlarmIntent = new Intent(this, AlarmService.class);
        setAlarmIntent.setAction(AlarmService.DOWHATNEED);
        this.startService(setAlarmIntent);
    }



    private static long back_pressed;
    @Override
    public void onBackPressed() {

    }




}
