package com.example.jerikohosea.rekmon.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.jerikohosea.rekmon.Adapters.ListDataAlternatif;
import com.example.jerikohosea.rekmon.R;

public class HomeUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user);
    }

    public void menuListData(View view){
        startActivity(new Intent(getApplicationContext(), ListDataAlternatif.class));
    }

    public void menuRekomendasi(View view){
        startActivity(new Intent(getApplicationContext(), InputNilaiPerbandingan.class));
    }

    public void menuAbout(View view){
        startActivity(new Intent(getApplicationContext(), TentangPenulis.class));
    }

    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        if (exit) {
            finish();
            finishAffinity();// finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }
}
