package com.ch1mac.wordangle.word_angle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class SelectActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        TextView tv=(TextView)findViewById(R.id.t1);
        tv.setTypeface(Typeface.createFromAsset(getAssets(), "gogo.ttf"));
        tv=(TextView)findViewById(R.id.t2);
        tv.setTypeface(Typeface.createFromAsset(getAssets(), "gogo.ttf"));
        setContentView(R.layout.activity_select);
        findViewById(R.id.imgbutton1).setOnClickListener(mClickListener);
        Switch mySwitch = (Switch) findViewById(R.id.switch3);
        mySwitch.setOnCheckedChangeListener(mChangeListener);
        InputStream fis = null;
        try {
            fis = getApplicationContext().openFileInput("bool");
            InputStreamReader isr = new InputStreamReader(fis);
            char[] inputBuffer = new char[2048];
            int t = isr.read(inputBuffer);
            t = inputBuffer[0]-48;
            boolean bb = t > 0 ? true : false;
            MainActivity.alarm_switch=bb;
            mySwitch.setChecked(bb);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    Switch.OnCheckedChangeListener mChangeListener = new Switch.OnCheckedChangeListener(){
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            FileOutputStream fos = null;
            try {                fos = getApplicationContext().openFileOutput("bool", Context.MODE_PRIVATE);
                int tem =((b) ? 1 : 0);
                String a = new String();
                if(tem ==1)
                    a="1";
                else
                    a="0";
                fos.write(a.getBytes());
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            MainActivity.alarm_switch = b;
        }
    };
    Button.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(SelectActivity.this, MainActivity.class));
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
