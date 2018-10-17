package com.example.jerikohosea.rekmon.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.jerikohosea.rekmon.Activities.AddDataAlternatif;
import com.example.jerikohosea.rekmon.Activities.HomeAdmin;
import com.example.jerikohosea.rekmon.Activities.HomeUser;
import com.example.jerikohosea.rekmon.R;
import com.example.jerikohosea.rekmon.ServiceAndClassObject.TempatRental;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListDataAlternatif extends AppCompatActivity implements View.OnClickListener{

    DataAlternatifViewHolder RekomenAdapter;
    private String cookies;
    private SharedPreferences preferences;
    RecyclerView recycler;
    ArrayList<TempatRental> ArrayTempatRental = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_data_alternatif);

        preferences = getSharedPreferences("com.example.jerikohosea.rekmon", Context.MODE_PRIVATE);
        cookies = preferences.getString("aktor", null);


        recycler=(RecyclerView)findViewById(R.id.recyclerDataAlternatif);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        RekomenAdapter =new DataAlternatifViewHolder(this);
        recycler.setAdapter(RekomenAdapter);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayTempatRental.clear();
                Log.d("COba","hahahahaha");
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot data : dataSnapshot.child("Data Alternatif").getChildren()) {
                    if (data.getValue(TempatRental.class).getId()!=0){
                        ArrayTempatRental.add(data.getValue(TempatRental.class));
                    }
                }

                for (int i = 0; i < ArrayTempatRental.size(); i++) {
                    Log.d("Array dari db",ArrayTempatRental.get(i).getNama());
                }

                RekomenAdapter.setDataBinding(ArrayTempatRental);
                //ArrayTempatRental.clear();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });

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

    public void back(View view) {
        if (cookies.equals("user")){
            startActivity(new Intent(getApplicationContext(), HomeUser.class));
            finish();
        }else if(cookies.equals("admin")){
            startActivity(new Intent(getApplicationContext(), HomeAdmin.class));
            finish();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case (R.id.btnFloat):
                Intent intent = new Intent(ListDataAlternatif.this, AddDataAlternatif.class);
                startActivity(intent);
                break;
        }
    }
}
