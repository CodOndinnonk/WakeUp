package com.wakeup;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    LocActivityHelper locActivityHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loc_activity_arithmetic);

        alarmId  = this.getIntent().getIntExtra(ID, 999);

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
        locActivityHelper = new LocActivityHelper(this,alarmId);
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

        //отменяем интент будильника, так как он уже сработал
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent AlarmReceiverIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, AlarmReceiverIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);

        locActivityHelper.makeWakeActivityFromSleep();
        setTask(100);
        ring = new Ring();
        ring.start(this);
        light.onLight();
        vibration.onVibration();
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
    }




}
