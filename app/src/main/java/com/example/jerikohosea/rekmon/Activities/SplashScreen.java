package com.example.jerikohosea.rekmon.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Window;

import com.example.jerikohosea.rekmon.R;


public class SplashScreen extends AppCompatActivity {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    final Handler handler = new Handler();

    Runnable MyRunnable=new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(getApplicationContext(), HomeUser.class));
            //cookies user
            preferences = getSharedPreferences("com.example.jerikohosea.rekmon", Context.MODE_PRIVATE);
            String cookies="user";

            editor = preferences.edit();
            editor.putString("aktor",cookies);
            editor.apply();

            finish();
        }
    };
    private int count =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash_screen);
        preferences = getSharedPreferences("com.example.jerikohosea.rekmon", Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString("urutan1","0");
        editor.putString("urutan2","0");
        editor.putString("urutan3","0");
        editor.apply();
        handler.postDelayed(MyRunnable, 3000L); //3000 L = 3 detik
    }


    @Override
    public boolean onKeyDown(int keycode, KeyEvent e) {
        switch(keycode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                count++;
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (count==2){
                    //startActivity(new Intent(getApplicationContext(), ListDataAlternatif.class));
                    startActivity(new Intent(getApplicationContext(), HomeAdmin.class));
                    //cookies admin
                    preferences = getSharedPreferences("com.example.jerikohosea.rekmon", Context.MODE_PRIVATE);
                    String cookies="admin";
                    editor = preferences.edit();
                    editor.putString("aktor",cookies);
                    editor.apply();

                    handler.removeCallbacks(MyRunnable);
                    finish();
                }
                return true;
        }

        return super.onKeyDown(keycode, e);
    }
}
