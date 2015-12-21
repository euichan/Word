package com.ch1mac.wordangle.word_angle;


import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import org.json.JSONObject;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class WordTranslate {
    Context m_ctx;
    MainActivity m = new MainActivity();

    public WordTranslate(Context _ctx)
    {
        m_ctx = _ctx;
    }

    public String JSONArr2String(JSONArray jsonArray) throws JSONException
    {
        String result="";
        int i;
        for (i = 0; i < jsonArray.length()-1; i++) {
            result += (String) jsonArray.getString(i)+", ";
        }
        result += (String) jsonArray.getString(i);
        return result;
    }

    private void SendWordToServer(String word, String means) throws JSONException
    {
        urllib u = new urllib(m_ctx);
        //u.POST("http://wordangle.ch1mac.com/words/",word,means);
        u.POST("http://wordangle.ch1mac.com/words/",word,means);
    }

    public void GetWords() {
        urllib u = new urllib(m_ctx);
        JSONObject jsonObject = null;
        JSONArray jsonArray = null;
        try {
            jsonObject = new JSONObject(u.urlopen("http://wordangle.ch1mac.com/words/?format=json"));
            jsonArray = jsonObject.getJSONArray("results");
            String str = JSONArr2String(jsonArray);
            m.toast(str, m_ctx);

        } catch (NetworkOnMainThreadException e) {
            Log.d("net", "error");
            m.toast("network error", m_ctx);
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void TranslateWord(String word) {
        urllib u = new urllib(m_ctx);
        JSONObject jsonObject = null;
        JSONArray jsonArray = null;
        try {
            jsonObject = new JSONObject(u.urlopen("http://tooltip.dic.naver.com/tooltip.nhn?wordString=" + word + "&languageCode=4&nlp=false"));
            jsonArray = jsonObject.getJSONArray("mean");
            String means = JSONArr2String(jsonArray);
            MainActivity.words.put(make_json(word,jsonArray));
            MainActivity.words_list.add(word);
            MainActivity.means_list.add(means);
            m.toast(means,m_ctx);
            //SendWordToServer(word, means);

        } catch (NetworkOnMainThreadException e) {
            Log.d("net", "error");
            m.toast("network error", m_ctx);
            e.printStackTrace();
        } catch (JSONException e) {
            m.toast(word+"를 네이버 사전에서 찾을 수 없습니다",m_ctx);
            e.printStackTrace();
        }
    }
    JSONObject make_json(String word,JSONArray trans) throws JSONException {
        Calendar calendar = Calendar.getInstance();
        long now = calendar.getTimeInMillis();
        String str = calendar.getTime().toString();

        JSONObject temp = new JSONObject();
        temp.put("word",word);
        temp.put("means",trans);
        temp.put("time",str);
        return temp;
    }
}
