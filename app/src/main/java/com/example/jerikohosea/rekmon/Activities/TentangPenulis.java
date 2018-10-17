package com.example.jerikohosea.rekmon.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.jerikohosea.rekmon.R;

public class TentangPenulis extends AppCompatActivity {
    private String cookies;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tentang_penulis);

        preferences = getSharedPreferences("com.example.jerikohosea.rekmon", Context.MODE_PRIVATE);
        cookies = preferences.getString("aktor", null);

    }

    @Override
    public void onBackPressed() {
        if (cookies.equals("user")){
            startActivity(new Intent(getApplicationContext(), HomeUser.class));
            finish();
        }else if(cookies.equals("admin")){
            startActivity(new Intent(getApplicationContext(), HomeAdmin.class));
            finish();
        }
    }
}
