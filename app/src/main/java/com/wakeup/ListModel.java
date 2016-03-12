package com.wakeup;

public class ListModel {
    private  String Time="";
    private  String everyDay = "";
    private  int Id=0;//поле со значением необходимого ID, используется при методе set
    private boolean active;

    /*********** Set Methods ******************/

    public void setTime(String Time)
    {
        this.Time = Time;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setEveryDay(String everyDay) {
        this.everyDay = everyDay;
    }

    public void setId(int Id)
    {
        this.Id = Id;
    }




    /*********** Get Methods ****************/

    public String getEveryDay() {
        return everyDay;
    }

    public boolean isActive() {
        return active;
    }

    public String getTime()
    {
        return this.Time;
    }


    public int getId()
    {
        return this.Id;
    }

   }