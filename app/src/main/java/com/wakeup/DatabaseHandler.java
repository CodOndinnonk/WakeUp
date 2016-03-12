package com.wakeup;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    final String myLog = "myLog";
    private static final int DATABASE_VERSION = 1;//текущая версия БД
    private static final String DATABASE_NAME = "Alarms_database.db";//название файла с БД
    private static final String TABLE_NOTES = "Alarms";//название таблицы
    private static final String KEY_ID = "id";//название поля id
    private static final String KEY_HOUR = "hour";//название поля date
    private static final String KEY_MINUTE = "minute";//название поля date
    private static final String KEY_ACTIVE = "active";//название поля date
    private static final String KEY_CONTENT = "content";//название поля date
    private static final String KEY_EVERYDAY = "everyday";//название поля keyword
    private static final String KEY_SOUND = "sound";//название поля keyword




    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        //создание строки, содержащей команда для создания БД
        String CREATE_NOTES_TABLE = "CREATE TABLE " + TABLE_NOTES + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"  + KEY_HOUR + " INTEGER," + KEY_MINUTE + " INTEGER,"
                + KEY_ACTIVE + " INTEGER," + KEY_CONTENT + " TEXT,"  + KEY_EVERYDAY + " INTEGER," + KEY_SOUND+ " INTEGER"
                + ")";
        db.execSQL(CREATE_NOTES_TABLE);
    }






    //при обновлении таблицы
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(db);
    }




    public void addAlarm(Alarm alarm) {//добавление записи
        SQLiteDatabase db = this.getWritableDatabase();//формат работы с БД
        ContentValues values = new ContentValues();//создание переменной, позволяющей создать шаблот "записи" и заполниять его для добавления в БД
        values.put(KEY_HOUR, alarm.get_hour());
        values.put(KEY_MINUTE, alarm.get_minute());//заполнение поля информацией, извлеченной из соответствующего поля записи, переданной из другой активности
        values.put(KEY_ACTIVE, alarm.get_active());//заполнение поля информацией, извлеченной из соответствующего поля записи, переданной из другой активности
        values.put(KEY_CONTENT, alarm.get_content());//заполнение поля информацией, извлеченной из соответствующего поля записи, переданной из другой активности
        values.put(KEY_EVERYDAY, alarm.get_everyDay());
        values.put(KEY_SOUND, alarm.get_Sound());
        db.insert(TABLE_NOTES, null, values);//добавление в таблицу щаблона, заполненного ранее
        db.close();//закрытие БД
    }




    public Alarm getAlarmById(int Id) {//берем запись по Id, необходимое нам Id передается с другой активности
        SQLiteDatabase db = this.getReadableDatabase();//формат работы с БД
        //переменная, хранящая найденую запись
        Cursor cursor = db.query(TABLE_NOTES, new String[] { KEY_ID, KEY_HOUR,
                        KEY_MINUTE, KEY_ACTIVE, KEY_CONTENT, KEY_EVERYDAY, KEY_SOUND },  KEY_ID + "=?",
                new String[] { String.valueOf(Id) }, null, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
        }
//создание обьекта ЗАПИСЬ и заполняем его данными из найденной ранее записи
        Alarm alarm = new Alarm(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getInt(3), cursor.getString(4), cursor.getInt(5),
                cursor.getInt(6));
        return alarm;//возвращаум активности, которая запрашивала обьект с заполненными полями(тоесть найденую запись)
    }




    public List<Alarm> getAllAlarms() {//метод, берущий все записи из БД и создающий из них список
        List<Alarm> contactList = new ArrayList<Alarm>();//создание списка обьектов типа "запись"
        String selectQuery = "SELECT  * FROM " + TABLE_NOTES ;//строка, описывающая команду извленчения записей
        SQLiteDatabase db = this.getWritableDatabase();//формат работы с БД
        Cursor cursor = db.rawQuery(selectQuery, null); //переменная, хранящая все записи
        if (cursor.moveToFirst()) {
            do {//делаем,
                Alarm alarm = new Alarm();//создание обьекта ЗАПИСЬ
                alarm.setID(Integer.parseInt(cursor.getString(0)));//заполнение поля, данными взятыми из БД
                alarm.set_hour(cursor.getInt(1));//заполнение поля, данными взятыми из БД
                alarm.set_minute(cursor.getInt(2));//заполнение поля, данными взятыми из БД
                alarm.set_active(cursor.getInt(3));//заполнение поля, данными взятыми из БД
                alarm.set_content(cursor.getString(4));//заполнение поля, данными взятыми из БД
                alarm.set_everyDay(cursor.getInt(5));
                alarm.set_Sound(cursor.getInt(6));
                contactList.add(alarm);//добавление обьекта в список
            } while (cursor.moveToNext());// пока есть следующая запись
        }
        return contactList;//возвращаум активности, которая запрашивала, список со всеми записиями(обьектами)
    }






    public int updateAlarm(Alarm alarm) {//метод обновления записи, методу передается обьект ЗАПИСЬ с ID той записи, которую надо изменить на переданную
        SQLiteDatabase db = this.getWritableDatabase();//формат работы с БД
        ContentValues values = new ContentValues();
        values.put(KEY_HOUR, alarm.get_hour());
        values.put(KEY_MINUTE, alarm.get_minute());//заполнение поля информацией, извлеченной из соответствующего поля записи, переданной из другой активности
        values.put(KEY_ACTIVE, alarm.get_active());//заполнение поля информацией, извлеченной из соответствующего поля записи, переданной из другой активности
        values.put(KEY_CONTENT, alarm.get_content());//заполнение поля информацией, извлеченной из соответствующего поля записи, переданной из другой активности
        values.put(KEY_EVERYDAY, alarm.get_everyDay());
        values.put(KEY_SOUND, alarm.get_Sound());
        return db.update(TABLE_NOTES, values, KEY_ID + " = ?",
                new String[] { String.valueOf(alarm.getID()) });
    }








    public void deleteAlarm(Alarm alarm) {//удаление записи, передаем обьект ЗАПИСЬ с неуобходимыми ID
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTES, KEY_ID + " = ?", new String[] { String.valueOf(alarm.getID()) });
        db.close();
    }










    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTES, null, null);
        db.close();
    }







    public int getAlarmsCount() {
        int nomber;
        String countQuery = "SELECT  * FROM " + TABLE_NOTES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        nomber=cursor.getCount();
        cursor.close();
        return nomber;
    }
}
