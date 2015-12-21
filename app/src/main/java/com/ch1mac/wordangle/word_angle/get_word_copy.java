package com.ch1mac.wordangle.word_angle;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.ClipboardManager.OnPrimaryClipChangedListener;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class get_word_copy extends Service {
    public static int count;
    public static Context con;
    public static get_word_copy get_word_copy;
    public static String prev_string = new String("fuckyou");
    ArrayList<PendingIntent> intentArray = new ArrayList<PendingIntent>();
    private OnPrimaryClipChangedListener listener = new OnPrimaryClipChangedListener() {
        public void onPrimaryClipChanged() {
            if(!MainActivity.copy_switch)
                return;
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            String cb = clipboard.getText().toString();
            int index = MainActivity.words_list.indexOf(cb);
            if ((cb.length() != 0) && (index < 0 ? true : false) && (!prev_string.equals(cb))) {
                process(cb);
            }
            else if(index!=-1 && !prev_string.equals(cb))
            {
                new MainActivity().toast(MainActivity.means_list.get(index),getApplicationContext());
            }
            prev_string=cb;
        }
    };

    @Override
    public void onCreate(){
        get_word_copy = com.ch1mac.wordangle.word_angle.get_word_copy.this;
        con = getApplicationContext();((ClipboardManager) getSystemService(CLIPBOARD_SERVICE)).addPrimaryClipChangedListener(listener);}

    @Override
    public IBinder onBind(Intent intent) {return null;}

    public void process(String str)
    {
        MainActivity.adapter.mDataSet.add(str);
        MainActivity.adapter.notifyDataSetChanged();
        new WordTranslate(con).TranslateWord(str);
        if(MainActivity.alarm_switch) {
            setalarm(10, MainActivity.words_list.size()); //10분
            setalarm(1444, MainActivity.words_list.size()); //1일
            setalarm(10080, MainActivity.words_list.size()); //1주
            setalarm(43200, MainActivity.words_list.size()); //1달
        }
        new MainActivity().WriteFile(con);
    }

    public void setalarm(int time, int index)
    {
        Intent intent = new Intent(get_word_copy,word_alarm.class);
        intent.putExtra("word",MainActivity.words_list.get(index-1));
        intent.putExtra("means",MainActivity.means_list.get(index-1));
        PendingIntent sender = PendingIntent.getBroadcast(get_word_copy, count, intent, 0);
        count++;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.MINUTE, time);

        AlarmManager am = (AlarmManager)con.getSystemService(ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);

        intentArray.add(sender);
    }
}