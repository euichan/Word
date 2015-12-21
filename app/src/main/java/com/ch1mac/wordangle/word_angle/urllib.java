package com.ch1mac.wordangle.word_angle;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by user on 2015-07-10.
 */
public class urllib extends AsyncTask<String, String, String> {
    Context m_ctx;
    public urllib(Context _ctx)
    {
        m_ctx = _ctx;
    }

    @Override
    protected String doInBackground(String... arg)
    {
        String response = "";
        if (arg.length == 2)
        {
            switch(arg[0])
            {
                case "isNetConnect":
                    return isNetConnect();
                case "urlopen":
                    return urlopen(arg[1]);
                default:
                    return "mistake";

            }
        }
        else
        {
            return "false";
        }
    }
    public String isNetConnect()
    {
        try {
            ConnectivityManager conMgr;
            conMgr = (ConnectivityManager)m_ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

            if (netInfo != null && netInfo.isConnected())
                return "true";
        } catch (Exception e) {
            Log.d("tag", "Connection state error");
        }
        return "false";
    }

    public void POST(String strUrl,String word, String means)
    {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            URL url = new URL(strUrl);
            HttpURLConnection conn;
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(0);
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("word", word);
            conn.setRequestProperty("mean", means);
            conn.setDoInput(true);
            conn.connect();
            int responseCode = conn.getResponseCode();
            MainActivity m = new MainActivity();
            m.toast(responseCode+"",m_ctx);
            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String urlopen(String strUrl) {
        String line, result = new String();
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            URL url = new URL(strUrl);
            HttpURLConnection conn;
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(0);
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();

            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            while((line = reader.readLine()) != null) {
                result += line + '\n';
                if( result.length() > 2000 ) break;
            }
            reader.close();
            conn.disconnect();
        }
        catch(Exception e) {
            Log.d("tag", "HttpURLConnection error");
        }
        return result;
    }


}
