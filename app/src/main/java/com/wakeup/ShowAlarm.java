package com.wakeup;

import android.app.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import android.widget.TimePicker;
import android.widget.Toast;


public class ShowAlarm extends Activity {
    final String myLog = "myLog";
    private int needId=-1;//переменная, которая содержит Id необходимой записи для отображения
    TimePicker timeSetter;
    CheckBox everyDayCheckBox;
    EditText contextEnterField;
    Button leftButton;
    boolean isEdit = false;
    Button deleteButton;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_alarm);

        deleteButton = (Button)findViewById(R.id.buttonDeleteShowAlarm);
        timeSetter = (TimePicker)findViewById(R.id.timePickerShowAlarm);
        everyDayCheckBox = (CheckBox)findViewById(R.id.everydayCheckBoxShowAlarm);
        contextEnterField = (EditText)findViewById(R.id.contentEnterFieldShowAlarm);
        leftButton = (Button)findViewById(R.id.buttonLeftShowAlarm);
        db = new DatabaseHandler(this);//переменная для работы с БД

        detectKindOfActivity();
        }

    public void detectKindOfActivity(){
        if(getIntent().getExtras().getInt("needId") > -1 ) {
            isEdit = true;
            needId = getIntent().getExtras().getInt("needId");//определение значения переменной нужного нам Id
            //мы извлекаем значение из переданного Intent, извлекаем ключ "needId"
            takeAlarm(needId);//запуск метода, который берет нужную нам запись, в него передаем Id необходимой нам записи
        }else {
            deleteButton.setVisibility(View.INVISIBLE);
        }
    }

    public void takeAlarm(int needId){
        DatabaseHandler db = new DatabaseHandler(this);//переменная для работы с БД
        Alarm needAlarm = db.getAlarmById(needId);//создаем обьект типа "запись" и зполняем его данными из необходимой нам записи, взятой по Id
        timeSetter.setCurrentHour(needAlarm.get_hour());
        timeSetter.setCurrentMinute(needAlarm.get_minute());

        if(needAlarm.get_everyDay() == 1){
            everyDayCheckBox.setChecked(true);
        }else {everyDayCheckBox.setChecked(false);}

        contextEnterField.setText(needAlarm.get_content());

        leftButton.setText(R.string.save);
    }



    public void save_Create(View view) {//при нажатии на СОХРАНИТЬ ИЗМЕНЕНИЯ
        if(needId > -1){
            update();
        }else {
            save();
        }
    }


    public void update() {//при нажатии на СОХРАНИТЬ ИЗМЕНЕНИЯ
        int setTimeHours = timeSetter.getCurrentHour();
        int setTimeMinute = timeSetter.getCurrentMinute();
        boolean isEveryday = everyDayCheckBox.isChecked();
        int everyDay;
        String strContent = contextEnterField.getText().toString();
        int isActive = 1;
        int soundNumber = 1;
        if (isEveryday == true) {
            everyDay = 1;
        } else {
            everyDay = 0;
        }
        Log.d(myLog, "Повтор ежедневно = " + isEveryday);
        Log.d(myLog, "Установленное время ЧАСЫ = " + setTimeHours);
        Log.d(myLog, "Установленное время МИНУТЫ = " + setTimeMinute);
        db.updateAlarm(new Alarm(needId, setTimeHours, setTimeMinute, isActive, strContent, everyDay, soundNumber));

        Toast mytoast = Toast.makeText(getApplicationContext(),
               "Изменения сохранены" , Toast.LENGTH_SHORT);
        mytoast.show();

        setComandToRemakeAlarms();
        finish();
    }


    public void setComandToRemakeAlarms(){
        Intent setAlarmIntent = new Intent(this, AlarmService.class);
        setAlarmIntent.setAction(AlarmService.DOWHATNEED);
        this.startService(setAlarmIntent);
    }



    public void save() {
        int setTimeHours = timeSetter.getCurrentHour();
        int setTimeMinute = timeSetter.getCurrentMinute();
        boolean isEveryday = everyDayCheckBox.isChecked();
        int everyDay;
        String strContent = contextEnterField.getText().toString();
        int isActive = 1;
        int soundNumber = 1;
        if(isEveryday==true){everyDay = 1 ;}else {everyDay = 0;}

        Log.d(myLog, "Повтор ежедневно = " + isEveryday);
        Log.d(myLog, "Установленное время ЧАСЫ = " + setTimeHours);
        Log.d(myLog, "Установленное время МИНУТЫ = " + setTimeMinute);
        db.addAlarm(new Alarm(setTimeHours, setTimeMinute, isActive, strContent, everyDay, soundNumber));

        Toast mytoast = Toast.makeText(getApplicationContext(),
             R.string.Toast_alarm_created, Toast.LENGTH_SHORT);
        mytoast.show();

        setComandToRemakeAlarms();
        finish();//завершение активности
    }




    public void cancel(View view) {
        finish();
    }//кнопка ОТМЕНА





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_alarm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {

                case R.id.settings://вызов окна О ПРОГРАММЕ
                Intent preferenceIntent = new Intent(this,PreferenceUser.class);
                startActivity(preferenceIntent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }


    }

    public void delete(View view) {
        Alarm needNote = db.getAlarmById(needId);//создаем обьект ЗАПИСЬ и заполняем его значениями из записи взятой по нужному нам ID
        db.deleteAlarm(new Alarm(needNote.getID(), needNote.get_hour(), needNote.get_minute(), needNote.get_active(), needNote.get_content(),needNote.get_everyDay(), needNote.get_Sound()));//запускаем метод "удаление" и передаем обьект ЗАПИСЬ со всеми полями
        Toast mytoast = Toast.makeText(getApplicationContext(),
                "Запись успешно удалена", Toast.LENGTH_SHORT);
        mytoast.show();
        setComandToRemakeAlarms();
        finish();
    }




}
