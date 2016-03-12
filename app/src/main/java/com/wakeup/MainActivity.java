package com.wakeup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
    ListView lvMain;
    ArrayList<Alarm> listOfAlarms = new ArrayList<Alarm>();
    BoxAdapter boxAdapter;
    private static final int menu_edit = 1;
    private static final int menu_del = 2;
    final String myLog = "myLog";
    final String WRITE_HOURS = "HOURS";
    final String WRITE_MINUTE = "MINUTE";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvMain = (ListView)findViewById(R.id.listViewMainActivity);
        prepareList();
        registerForContextMenu(lvMain);

        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Alarm note = boxAdapter.getNote(position);
                Intent gotoShowNote = new Intent(MainActivity.this, ShowAlarm.class);//создание интента
                gotoShowNote.putExtra("needId", note.getID());//передаем в следующий интент параметр, хранящий номер Id нужной нам записи
                startActivity(gotoShowNote);//запуск интента
            }
        });
    }



    public void prepareList(){
        fillData();
        boxAdapter = new BoxAdapter(this, listOfAlarms);
        lvMain.setAdapter(boxAdapter);
    }

    // генерируем данные для адаптера
    void fillData() {
        DatabaseHandler db = new DatabaseHandler(this);//переменная для работы с БД
        List<Alarm> listGetAllAlarms = db.getAllAlarms();//создание списка обьектов типа "запись" и заполнения его значениями всех записей взятых из БД

        for (Alarm cn : listGetAllAlarms) {//проходим про каждому обьекту списка
            listOfAlarms.add(new Alarm(cn.getID(), cn.get_hour(), cn.get_minute(), cn.get_active(), cn.get_content(),
                    cn.get_everyDay(), cn.get_Sound()));
        }
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
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, menu_edit, 0, R.string.edit_note_text);
        menu.add(0, menu_del, 0, R.string.delete_note_text);
    }




    public void addAlarm(View view) {
        Intent goToAdd = new Intent(this, ShowAlarm.class);
        goToAdd.putExtra("needId", -1);//показывает, какой тип активити отображать, если больше -1, то это редактирование
        //а так это создание
        startActivity(goToAdd);
    }







    public void offAlarm(View view) {
        Intent myOff = new Intent(this, AlarmService.class);
        myOff.setAction(AlarmService.DOWHATNEED);
        this.startService(myOff);
    }


   /*
    public void edit(View view) {Log.d(myLog, "MainActivity edit 1 ");
        int getIdInput = Integer.valueOf(putID.getText().toString());
        Log.d(myLog, "getIdInput =  " + getIdInput);
        Log.d(myLog, " lastAlarmId =  " + lastAlarmId);
        if(getIdInput-1 < lastAlarmId){
            Log.d(myLog, "MainActivity edit 2 ");
            Intent goToAdd = new Intent(this, ShowAlarm.class);
            goToAdd.putExtra("needId", getIdInput  );
            startActivity(goToAdd);
            Log.d(myLog, "MainActivity edit 3 ");
        }else {
            Log.d(myLog, "MainActivity edit 4 ");
            Toast mytoast = Toast.makeText(getApplicationContext(),
                    "Введен не существующий будильник ", Toast.LENGTH_LONG);
            mytoast.show();
        }

    }


    public void startAlarm(View view) {
        Log.d(myLog, "MainActivity startAlarm 1 ");

        int setTimeHours = setTime.getCurrentHour();
        int setTimeMinute = setTime.getCurrentMinute();

        Log.d(myLog, "Установленное время ЧАСЫ = " + setTimeHours);
        Log.d(myLog, "Установленное время МИНУТЫ = " + setTimeMinute);

        SharedPreferences.Editor editor = mSettings.edit();// вызов обьекта editor для измненения параметров настроек
        editor.putInt(WRITE_HOURS, setTimeHours);
        editor.putInt(WRITE_MINUTE, setTimeMinute);
        editor.apply();// метод, сохраняющий добавленные данные

        Intent my = new Intent(this, AlarmService.class);
        my.setAction(AlarmService.CREATE);
        this.startService(my);

    }


*/


}
