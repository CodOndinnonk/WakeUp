package com.wakeup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ShowContent extends Activity {
    final String myLog = "myLog";
    public static final String ID = "id";
    public static final String IS_CONTENT = "isContent";
    public static final String IS_PROVERB = "isProverb";
    int alarmId;
    TextView showContent;
    TextView showProverb;
    LinearLayout topFieldWithContent;
    LinearLayout bottomFieldWithContent;
    DatabaseHandler db;
    Alarm needAlarm;
    boolean isContent;
    boolean isProverb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_content);

        alarmId  = this.getIntent().getIntExtra(ID, 999);
        isContent = this.getIntent().getBooleanExtra(IS_CONTENT,false);
        isProverb = this.getIntent().getBooleanExtra(IS_PROVERB,false);
        showContent = (TextView)findViewById(R.id.showContentFieldShowContent);
        showProverb = (TextView)findViewById(R.id.showProverdShowContent);
        topFieldWithContent = (LinearLayout)findViewById(R.id.topSpaceShowContent);
        bottomFieldWithContent = (LinearLayout)findViewById(R.id.downSpaceShowContent);
        db = new DatabaseHandler(this);//переменная для работы с БД

        takeAlarm(alarmId);

        makeWakeActivityFromSleep();
        setContent();
        setProverb();
    }

    public void makeWakeActivityFromSleep(){
        //включение экрана
        Activity activity = this;
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


    public void setContent(){
        if(isContent) {
            showContent.setText(needAlarm.get_content());//заполняем поле информациее, взятой из сохраненной ранее запис
        }else {
            topFieldWithContent.setVisibility(View.INVISIBLE);
        }
    }




    public void takeAlarm(int needId){
        needAlarm = db.getAlarmById(needId);//создаем обьект типа "запись" и зполняем его данными из необходимой нам записи, взятой по Id
    }


    public void setProverb(){
        if(isProverb) {
            String textForTest = "В следующем примере мы снижаем громкость медиапроигрывателя, когда он временно теряет аудиофокус, " +
                    "а затем возвращает громкость на прежний уровень,когда фокус возвращается.";
            showProverb.setText(textForTest);
        }else {
            bottomFieldWithContent.setVisibility(View.INVISIBLE);
        }

    }


    public void closeAlarm(View view) {
        //закрытие программы
        Intent quit = new Intent(Intent.ACTION_MAIN);
        quit.addCategory(Intent.CATEGORY_HOME);
        quit.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(quit);
    finish();
    }



    public void shareProverb(View view) {
    }
}
