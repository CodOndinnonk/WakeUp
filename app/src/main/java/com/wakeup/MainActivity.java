package com.wakeup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
    ListView lvMain;
    ArrayList<Alarm> listOfAlarms = new ArrayList<Alarm>();
    BoxAdapter boxAdapter;
    private static final int menu_edit = 1;
    private static final int menu_del = 2;
    final String myLog = "myLog1";
    final String WRITE_HOURS = "HOURS";
    final String WRITE_MINUTE = "MINUTE";
    DatabaseHandler db;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHandler(this);//переменная для работы с БД
        lvMain = (ListView)findViewById(R.id.listViewMainActivity);

        prepareList();

        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(myLog, " MainActivity onItemClick");
                Alarm alarm = boxAdapter.getAlarm(position);
                editAlarm(alarm.getID());
                }
        });

        registerForContextMenu(lvMain);
    }



    public void prepareList(){
        fillData();
        boxAdapter = new BoxAdapter(this, listOfAlarms);
        lvMain.setAdapter(boxAdapter);
    }

    // генерируем данные для адаптера
    void fillData() {
        List<Alarm> listGetAllAlarms = db.getAllAlarms();//создание списка обьектов типа "запись" и заполнения его значениями всех записей взятых из БД

        for (Alarm cn : listGetAllAlarms) {//проходим про каждому обьекту списка
            listOfAlarms.add(new Alarm(cn.getID(), cn.get_hour(), cn.get_minute(), cn.get_delayHour(), cn.get_delayMinute(), cn.get_active(), cn.get_content(),
                    cn.get_Sound(), cn.get_repetDays()));
        }
    }



    public void setComandToRemakeAlarms(){
        Intent setAlarmIntent = new Intent(this, AlarmService.class);
        setAlarmIntent.setAction(AlarmService.DOWHATNEED);
        this.startService(setAlarmIntent);
    }


    public void resetList(){
        listOfAlarms.clear();
        fillData();
        boxAdapter.notifyDataSetChanged();
    }



    @Override
    protected void onRestart() {//при запуске Активности(возобнавлении ее работы)
        super.onRestart();
        resetList();
    }


    @Override
    protected void onResume() {//при запуске Активности(возобнавлении ее работы)
        super.onResume();
       resetList();
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.delAll://вызов окна О ПРОГРАММЕ
                DatabaseHandler db = new DatabaseHandler(this);
                db.deleteAll();
                Intent AlarmServiceOffAllDel = new Intent(this, AlarmService.class);
                AlarmServiceOffAllDel.setAction(AlarmService.DOWHATNEED);
                this.startService(AlarmServiceOffAllDel);
                return true;

            case R.id.offAll://вызов окна О ПРОГРАММЕ
                Intent AlarmServiceOffAll = new Intent(this, AlarmService.class);
                AlarmServiceOffAll.setAction(AlarmService.DOWHATNEED);
                this.startService(AlarmServiceOffAll);
                return true;

            case R.id.settings://вызов окна О ПРОГРАММЕ
                Intent preferenceIntent = new Intent(this,PreferenceUser.class);
                startActivity(preferenceIntent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        Log.d(myLog, " MainActivity onCreateContextMenu");
        menu.add(0, menu_edit, 0, R.string.edit_note_text);
        menu.add(0, menu_del, 0, R.string.delete_note_text);
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // получаем инфу о пункте списка
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Alarm alarm = boxAdapter.getAlarm((int)boxAdapter.getItemId(acmi.position));

        switch (item.getItemId()) {
            case menu_edit://вызов окна О ПРОГРАММЕ
                editAlarm(alarm.getID());
                return true;

            case menu_del://вызов окна О ПРОГРАММЕ
                deleteAlarm(alarm.getID());
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }



    public void addAlarm(View view) {
        Intent goToAdd = new Intent(this, ShowAlarm.class);
        goToAdd.putExtra("needId", -1);//показывает, какой тип активити отображать, если больше -1, то это редактирование
        //а так это создание
        startActivity(goToAdd);

    }




    public void editAlarm(int id) {
            Intent goToAdd = new Intent(this, ShowAlarm.class);
            goToAdd.putExtra("needId", id  );
            startActivity(goToAdd);
    }

    public void deleteAlarm(int id) {
        Alarm needAlarm = db.getAlarmById(id);//создаем обьект ЗАПИСЬ и заполняем его значениями из записи взятой по нужному нам ID
        db.deleteAlarm(new Alarm(needAlarm.getID(), needAlarm.get_hour(),
                needAlarm.get_minute(), needAlarm.get_delayHour(), needAlarm.get_delayMinute(), needAlarm.get_active(), needAlarm.get_content(),
                needAlarm.get_Sound(), needAlarm.get_repetDays()));//запускаем метод "удаление" и передаем обьект ЗАПИСЬ со всеми полями
        Toast mytoast = Toast.makeText(getApplicationContext(),
                R.string.alarmDeleted, Toast.LENGTH_SHORT);
        mytoast.show();
        setComandToRemakeAlarms();
        resetList();
    }



}