package com.ch1mac.wordangle.word_angle;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;


public class get_word_share extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get the received intent
        Intent receivedIntent = getIntent();
        //get the action
        String receivedAction = receivedIntent.getAction();
        //find out what we are dealing with
        String receivedType = receivedIntent.getType();

        //make sure it's an action and type we can handle
        if(receivedAction.equals(Intent.ACTION_SEND)){

            if(receivedType.startsWith("text/")){
                if(!MainActivity.share_switch)
                    return;
                String receivedText = receivedIntent.getStringExtra(Intent.EXTRA_TEXT);
                int index = MainActivity.words_list.indexOf(receivedText);
                if (receivedText != null && index < 0 ? true : false) {
                    new get_word_copy().process(receivedText);
                }
                else if(index!=-1)
                {
                    new MainActivity().toast(MainActivity.means_list.get(index),getApplicationContext());
                }
            }
        }
        else if(receivedAction.equals(Intent.ACTION_MAIN)){
        }
        finish();
    }


}
