package com.wakeup;


public class Alarm {
    int _id;
    int _hour;
    int _minute;
    int _active;
    String _content;
    int _everyDay;
    int _sound;

    public Alarm(){
    }

    public Alarm(int id, int hour, int minute,  int active, String content, int everyDay, int sound){

        this._id = id;
        this._hour = hour;
        this._minute = minute;
        this._active = active;
        this._content = content;
        this._everyDay = everyDay;
        this._sound = sound;

    }

    public Alarm(int hour, int minute,  int active, String content, int everyDay, int sound){

        this._hour = hour;
        this._minute = minute;
        this._active = active;
        this._content = content;
        this._everyDay = everyDay;
        this._sound = sound;

    }



    public int getID(){
        return this._id;
    }

    public void setID(int id){
        this._id = id;
    }

    public void set_hour(int _hour) {
        this._hour = _hour;
    }

    public void set_minute(int _minute) {
        this._minute = _minute;
    }

    public void set_active(int _active) {
        this._active = _active;
    }

    public void set_content(String _content) {
        this._content = _content;
    }

    public void set_everyDay(int _everyDay) {
        this._everyDay = _everyDay;
    }

    public void set_Sound(int sound) {
        this._sound = sound;
    }

    public int get_everyDay() {
        return _everyDay;
    }

    public int get_Sound() {
        return _sound;
    }

    public int get_hour() {
        return _hour;
    }

    public int get_minute() {
        return _minute;
    }

    public int get_active() {
        return _active;
    }

    public String get_content() {
        return _content;
    }




}