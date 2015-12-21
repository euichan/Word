package com.ch1mac.wordangle.word_angle;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nhaarman.listviewanimations.appearance.simple.SwingLeftInAnimationAdapter;
import com.hudomju.swipe.SwipeToDismissTouchListener;
import com.hudomju.swipe.adapter.ListViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.widget.Toast.LENGTH_SHORT;


public class MainActivity extends Activity implements TextToSpeech.OnInitListener {
    static public ArrayAdapter<String> m_adapter;
    static public ArrayList<String> words_list = new ArrayList<String>();
    static public ArrayList<String> means_list = new ArrayList<String>();
    static public boolean copy_switch;
    static public boolean share_switch;
    static public boolean alarm_switch;
    JSONObject json = new JSONObject();
    public static JSONObject obj;
    static public JSONArray words = new JSONArray();
    static public FileInputStream fis;
    static public  MyBaseAdapter adapter;
    TextToSpeech tts;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        copy_switch = true;
        share_switch = true;
        share_switch = true;
        setContentView(R.layout.activity_main);
        startService(new Intent(this, get_word_copy.class));
        tts = new TextToSpeech(this, this);
        adapter = new MyBaseAdapter(getApplicationContext());
        init((ListView) findViewById(R.id.id_list));
    }


    public void init(ListView listView) {

        SwingLeftInAnimationAdapter animationAdapter = new SwingLeftInAnimationAdapter(adapter);
        animationAdapter.setAbsListView(listView);
        listView.setAdapter(animationAdapter);
        final SwipeToDismissTouchListener<ListViewAdapter> touchListener =
                new SwipeToDismissTouchListener<>(
                        new ListViewAdapter(listView),
                        new SwipeToDismissTouchListener.DismissCallbacks<ListViewAdapter>() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListViewAdapter view, int position) {
                                adapter.remove(position);
                                ArrayList<String> list = new ArrayList<String>();
                                int len = words.length();
                                if (words != null) {
                                    for (int i=0;i<len;i++){
                                        try {
                                            list.add(words.get(i).toString());
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    list.remove(position);
                                    words = new JSONArray(list);
                                    WriteFile(getApplicationContext());
                                }
                            }
                        });
        listView.setOnTouchListener(touchListener);
        // Setting this scroll listener is required to ensure that during ListView scrolling,
        // we don't look for swipes.
        listView.setOnScrollListener((AbsListView.OnScrollListener) touchListener.makeScrollListener());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (touchListener.existPendingDismisses()) {
                    touchListener.undoPendingDismiss();
                } else {
                    speech(words_list.get(position));
                    Toast.makeText(MainActivity.this, means_list.get(position), LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {
            tts.setLanguage(Locale.US);
        }
    }

    static class MyBaseAdapter extends BaseAdapter {

        private static final int SIZE = 100;

        public List<String> mDataSet = new ArrayList<>();

        MyBaseAdapter(Context con) {
            try {
                fis =  con.openFileInput("word");
                InputStreamReader isr = new InputStreamReader(fis);
                StringBuilder sb = new StringBuilder();
                char[] inputBuffer = new char[2048];
                int l;
                while ((l = isr.read(inputBuffer)) != -1)
                    sb.append(inputBuffer, 0, l);
                MainActivity.obj = new JSONObject(sb.toString().replace("\"{","{").replace("}\"","}").replace("\\\"","\""));
                MainActivity.words = obj.getJSONArray("words");
                for(int i=0;i<words.length();i++)
                {
                    JSONObject word = words.getJSONObject(i);
                    String get_word = word.getString("word");

                    mDataSet.add(get_word);
                    words_list.add(get_word);
                    means_list.add(new WordTranslate(con).JSONArr2String(word.getJSONArray("means")));
                }
                fis.close();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getCount() {
            return mDataSet.size();
        }

        @Override
        public String getItem(int position) {
            return mDataSet.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void remove(int position) {
            mDataSet.remove(position);
            notifyDataSetChanged();
        }

        class ViewHolder {
            TextView dataTextView;
            ViewHolder(View view) {
                dataTextView = ((TextView) view.findViewById(R.id.txt_data));
                view.setTag(this);
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder = convertView == null
                    ? new ViewHolder(convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_list_item, parent, false))
                    : (ViewHolder) convertView.getTag();

            viewHolder.dataTextView.setText(mDataSet.get(position));
            return convertView;
        }
    }







    public void toast(String str,Context con)
    {
        Log.d("fuck", str);
        Toast.makeText(con, str, Toast.LENGTH_LONG).show();
    }
    public void WriteFile(Context con)
    {
        try {
            json.put("words",words);
            FileOutputStream fos = con.openFileOutput("word", Context.MODE_PRIVATE);
            fos.write(json.toString().getBytes());
            fos.close();
            Log.d("json", json.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void speech(String str) {
        tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);
    }

}
