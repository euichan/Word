package com.ch1mac.wordangle.word_angle;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

public class word_alarm extends BroadcastReceiver {
    public word_alarm() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ShowCustomToast(intent.getExtras().getString("word"),intent.getExtras().getString("means"),context);
    }

    public void ShowCustomToast(String word, String means, Context context)
    {
        View layout = (View) LayoutInflater.from(context).inflate(R.layout.custom_toast, null);
        TextView text = (TextView) layout.findViewById(R.id.textView1);
        text.setText(word);
        text = (TextView)layout.findViewById(R.id.textView2);
        text.setText(means);

        View tt =  layout.findViewById(R.id.custom_toast_layout_id);
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(tt);
        toast.show();


        AnimationSet animSet = new AnimationSet(true);
        Animation scaleZoom = AnimationUtils.loadAnimation(context, R.anim.abc_slide_in_top);
        animSet.addAnimation(scaleZoom);

        layout.findViewById(R.id.custom_toast_layout_id).setAnimation(animSet);
        animSet.start();
    }


}
