package com.example.jerikohosea.rekmon.Adapters;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.jerikohosea.rekmon.Activities.InputNilaiPerbandingan;
import com.example.jerikohosea.rekmon.R;
import com.example.jerikohosea.rekmon.ServiceAndClassObject.TempatRental;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListHasilRekomen extends AppCompatActivity {

    HasilRekomenViewHolder RekomenAdapter;
    ArrayList<TempatRental> ArrayTempatRental = new ArrayList<>();
    RecyclerView recycler;
    private int[]urutan;
    private double[]urutanJarak;
    private boolean[]filterTukar;
    private double[]filter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reycler_hasil_rekomen);

        Intent intent=getIntent();
        urutan=intent.getIntArrayExtra("urutan");
        urutanJarak=intent.getDoubleArrayExtra("urutan jarak");
        filter=intent.getDoubleArrayExtra("filter");
        filterTukar=intent.getBooleanArrayExtra("filter tukar");
        recycler=(RecyclerView)findViewById(R.id.recyclerHasilRekomen);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        RekomenAdapter =new HasilRekomenViewHolder(this);
        recycler.setAdapter(RekomenAdapter);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayTempatRental.clear();
                Log.d("COba","hahahahaha");
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot data : dataSnapshot.child("Data Alternatif").getChildren()) {
                        ArrayTempatRental.add(data.getValue(TempatRental.class));

                }


                for (int i = 0; i < 3; i++) {
                    Log.d("Urutan pertama dari db",Integer.toString(urutan[i]));
                }


                RekomenAdapter.setDataBinding(ArrayTempatRental,urutan,urutanJarak,filter, filterTukar);
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
        startActivity(new Intent(getApplicationContext(), InputNilaiPerbandingan.class));
        finish();

    }



}
