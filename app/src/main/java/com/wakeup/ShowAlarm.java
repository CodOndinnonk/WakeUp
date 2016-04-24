package com.wakeup;

import android.app.Activity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class ShowAlarm extends Activity {
    final String myLog = "myLog";
    private int needId=-1;//переменная, которая содержит Id необходимой записи для отображения
    TimePicker timeSetter;
    EditText contextEnterField;
    Button leftButton;
    boolean isEdit = false;
    Button deleteButton;
    DatabaseHandler db;
    ArrayList<Alarm> alarms = new ArrayList<Alarm>();
    ArrayList<Integer> repetDaysList;
    String repetDays = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_alarm);

        deleteButton = (Button)findViewById(R.id.buttonDeleteShowAlarm);
        timeSetter = (TimePicker)findViewById(R.id.timePickerShowAlarm);
        contextEnterField = (EditText)findViewById(R.id.contentEnterFieldShowAlarm);
        leftButton = (Button)findViewById(R.id.buttonLeftShowAlarm);
        db = new DatabaseHandler(this);//переменная для работы с БД
        repetDaysList = new ArrayList<Integer>();
        fillData();

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
        Alarm needAlarm = db.getAlarmById(needId);//создаем обьект типа "запись" и зполняем его данными из необходимой нам записи, взятой по Id
        timeSetter.setCurrentHour(needAlarm.get_hour());
        timeSetter.setCurrentMinute(needAlarm.get_minute());

        Log.d(myLog, "повторяющиеся дни = "+ needAlarm.get_repetDays());

        ArrayList<String> repetDaysListStrings = splitLine(needAlarm.get_repetDays());
        for(int i = 0;i < repetDaysListStrings.size(); i++ ){
            repetDaysList.add(Integer.decode(repetDaysListStrings.get(i)));
        }

        contextEnterField.setText(needAlarm.get_content());
        leftButton.setText(R.string.save);

        if (repetDaysList.contains(1)) {
            Log.d(myLog, "отмечаем кнопку 1");
        Button button1 = (Button)findViewById(R.id.button1);
            button1.setBackgroundColor(Color.GREEN);
        }
        if (repetDaysList.contains(2)) {
            Log.d(myLog, "отмечаем кнопку 2");
            Button button2 = (Button)findViewById(R.id.button2);
            button2.setBackgroundColor(Color.GREEN);
        }
        if (repetDaysList.contains(3)) {
            Log.d(myLog, "отмечаем кнопку 3");
            Button button3 = (Button)findViewById(R.id.button3);
            button3.setBackgroundColor(Color.GREEN);
        }
        if (repetDaysList.contains(4)) {
            Log.d(myLog, "отмечаем кнопку 4");
            Button button4 = (Button)findViewById(R.id.button4);
            button4.setBackgroundColor(Color.GREEN);
        }
        if (repetDaysList.contains(5)) {
            Log.d(myLog, "отмечаем кнопку 5");
            Button button5 = (Button)findViewById(R.id.button5);
            button5.setBackgroundColor(Color.GREEN);
        }
        if (repetDaysList.contains(6)) {
            Log.d(myLog, "отмечаем кнопку 6");
            Button button6 = (Button)findViewById(R.id.button6);
            button6.setBackgroundColor(Color.GREEN);
        }
        if (repetDaysList.contains(7)) {
            Log.d(myLog, "отмечаем кнопку 7");
            Button button7 = (Button)findViewById(R.id.button7);
            button7.setBackgroundColor(Color.GREEN);
        }


    }


    public ArrayList<String> splitLine(String lineForSplit){
        ArrayList<String> readyList = new ArrayList<String>();
        for (String splitWord : lineForSplit.split(" ")) {
            readyList.add(splitWord);
        }
        return readyList;
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

        String strContent = contextEnterField.getText().toString();
        int isActive = 1;
        int soundNumber = 1;

        // соритруем нащ список с днеями по возростанию
        Collections.sort(repetDaysList, new Comparator<Integer>() {
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });
        //создаем строку с днями
        for(int i=0; i<repetDaysList.size();i++){
            repetDays += repetDaysList.get(i).toString() + " ";
        }

        if(repetDaysList.size() == 0){
            repetDays = "0";
        }

        int resultOfExistance = checkForExistanceAlarm(new Alarm(needId, setTimeHours, setTimeMinute, isActive, strContent,
                soundNumber, repetDays));

        if(resultOfExistance == 0) {
            db.updateAlarm(new Alarm(needId, setTimeHours, setTimeMinute, isActive, strContent, soundNumber, repetDays));

            Toast mytoast = Toast.makeText(getApplicationContext(),
                    R.string.changesSaved, Toast.LENGTH_SHORT);
            mytoast.show();

            setComandToRemakeAlarms();
            finish();
        }else {
            Toast mytoast = Toast.makeText(getApplicationContext(),
                    R.string.alarmAlreadyExisted, Toast.LENGTH_SHORT);
            mytoast.show();
        }
    }


    public void setComandToRemakeAlarms(){
        Intent setAlarmIntent = new Intent(this, AlarmService.class);
        setAlarmIntent.setAction(AlarmService.DOWHATNEED);
        this.startService(setAlarmIntent);
    }



    public void save() {
        int setTimeHours = timeSetter.getCurrentHour();
        int setTimeMinute = timeSetter.getCurrentMinute();
        String strContent = contextEnterField.getText().toString();
        int isActive = 1;
        int soundNumber = 1;

        // соритруем нащ список с днеями по возростанию
        Collections.sort(repetDaysList, new Comparator<Integer>() {
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });
        //создаем строку с днями
        for(int i=0; i<repetDaysList.size();i++){

            repetDays += repetDaysList.get(i).toString() + " ";
        }

        if(repetDaysList.size() == 0){
            repetDays = "0";
        }

        int resultOfExistance = checkForExistanceAlarm(new Alarm(setTimeHours, setTimeMinute, isActive, strContent, soundNumber, repetDays));

        if(resultOfExistance == 0) {

            db.addAlarm(new Alarm(setTimeHours, setTimeMinute, isActive, strContent, soundNumber, repetDays));

            Toast mytoast = Toast.makeText(getApplicationContext(),
                    R.string.Toast_alarm_created, Toast.LENGTH_SHORT);
            mytoast.show();

            setComandToRemakeAlarms();
            finish();//завершение активности
        }else {
            Toast mytoast = Toast.makeText(getApplicationContext(),
                    R.string.alarmAlreadyExisted, Toast.LENGTH_SHORT);
            mytoast.show();
        }
    }

    void fillData() {
        List<Alarm> listGetAlarms = db.getAllAlarms();//создание списка обьектов типа "запись" и заполнения его значениями всех записей взятых из БД

        for (Alarm cn : listGetAlarms) {//проходим про каждому обьекту списка
            alarms.add(new Alarm(cn.getID(), cn.get_hour(), cn.get_minute(), cn.get_active(), cn.get_content(),
                     cn.get_Sound(), cn.get_repetDays()));
        }
    }


    public int checkForExistanceAlarm(Alarm creatingAlarm){//передаем сюда созданный только что будильник, для проверки сходства с созданными ранеее
        for (Alarm cn : alarms) {//проходим про каждому обьекту списка
                if(creatingAlarm.get_hour() == cn.get_hour()){
                    if(creatingAlarm.get_minute() == cn.get_minute()){
                        if(cn.getID() == creatingAlarm.getID()){
                        }else {
                            return 1;//отказ, так как будильник на это время уже существует
                        }
                    }
                }
             }
        return 0;//разрешено, будильника с данным временем нет
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
        db.deleteAlarm(new Alarm(needNote.getID(), needNote.get_hour(), needNote.get_minute(),
                needNote.get_active(), needNote.get_content(),
                needNote.get_Sound(), needNote.get_repetDays()));//запускаем метод "удаление" и передаем обьект ЗАПИСЬ со всеми полями
        Toast mytoast = Toast.makeText(getApplicationContext(),
                "Запись успешно удалена", Toast.LENGTH_SHORT);
        mytoast.show();
        setComandToRemakeAlarms();
        finish();
    }


    public void cl1(View view) {
        Integer day = 1;
        if(repetDaysList.contains(day)){
            view.setBackgroundColor(Color.TRANSPARENT);
            repetDaysList.remove(day);
        }else {
            repetDaysList.add(day);
            view.setBackgroundColor(Color.GREEN);
        }

    }

    public void cl2(View view) {
        Integer day = 2;
        if(repetDaysList.contains(day)){
            view.setBackgroundColor(Color.TRANSPARENT);
            repetDaysList.remove(day);
        }else {
            repetDaysList.add(day);
            view.setBackgroundColor(Color.GREEN);
        }
    }

    public void cl3(View view) {
        Integer day = 3;
        if(repetDaysList.contains(day)){
            view.setBackgroundColor(Color.TRANSPARENT);
            repetDaysList.remove(day);
        }else {
            repetDaysList.add(day);
            view.setBackgroundColor(Color.GREEN);
        }
    }

    public void cl4(View view) {
        Integer day = 4;
        if(repetDaysList.contains(day)){
            view.setBackgroundColor(Color.TRANSPARENT);
            repetDaysList.remove(day);
        }else {
            repetDaysList.add(day);
            view.setBackgroundColor(Color.GREEN);
        }
    }

    public void cl5(View view) {
        Integer day = 5;
        if(repetDaysList.contains(day)){
            view.setBackgroundColor(Color.TRANSPARENT);
            repetDaysList.remove(day);
        }else {
            repetDaysList.add(day);
            view.setBackgroundColor(Color.GREEN);
        }
    }

    public void cl6(View view) {
        Integer day = 6;
        if(repetDaysList.contains(day)){
            view.setBackgroundColor(Color.TRANSPARENT);
            repetDaysList.remove(day);
        }else {
            repetDaysList.add(day);
            view.setBackgroundColor(Color.GREEN);
        }
    }

    public void cl7(View view) {
        Integer day = 7;
        if(repetDaysList.contains(day)){
            view.setBackgroundColor(Color.TRANSPARENT);
            repetDaysList.remove(day);
        }else {
            repetDaysList.add(day);
            view.setBackgroundColor(Color.GREEN);
        }
    }



}
