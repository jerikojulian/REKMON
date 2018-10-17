    package com.example.jerikohosea.rekmon.Activities;

    import android.app.Activity;
    import android.content.Context;
    import android.content.Intent;
    import android.content.pm.PackageManager;
    import android.location.Location;
    import android.location.LocationListener;
    import android.location.LocationManager;
    import android.os.Bundle;
    import android.support.v4.app.ActivityCompat;
    import android.support.v4.content.ContextCompat;
    import android.support.v7.app.AppCompatActivity;
    import android.support.v7.widget.Toolbar;
    import android.util.Log;
    import android.view.View;
    import android.widget.Button;
    import android.widget.CompoundButton;
    import android.widget.EditText;
    import android.widget.RelativeLayout;
    import android.widget.ScrollView;
    import android.widget.SeekBar;
    import android.widget.Switch;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.example.jerikohosea.rekmon.R;
    import com.example.jerikohosea.rekmon.ServiceAndClassObject.CobaHitung;
    import com.example.jerikohosea.rekmon.ServiceAndClassObject.TempatRental;
    import com.google.android.gms.common.ConnectionResult;
    import com.google.android.gms.common.GooglePlayServicesUtil;
    import com.google.android.gms.maps.GoogleMap;
    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.Query;
    import com.google.firebase.database.ValueEventListener;

    import java.util.ArrayList;

    public class InputNilaiPerbandingan extends AppCompatActivity {

        ArrayList<TempatRental> ArrayTempatRental = new ArrayList<>();
        Activity activity;
        TextView lat, lon;
        EditText filterharga,filterjarak,filterpopuler,filterrating;
        private GoogleMap mGoogleMap;
        SeekBar mySeekBar,mySeekBar2,mySeekBar3,mySeekBar4;
        private double nilaiSeek1=1.0,nilaiSeek2=1.0,nilaiSeek3=1.0,nilaiSeek4=1.0;
        private double [][] matriks_berpasangan;
        private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;
        private static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 12;
        public static final int LOCATION_UPDATE_MIN_DISTANCE = 10;
        public static final int LOCATION_UPDATE_MIN_TIME = 5000;
        double longitude=0;
        double latitude=0;
        double a12,a13,a14,a23,a24,a34;
        double a21,a31,a41,a32,a42,a43;
        double filter[]={Integer.MIN_VALUE,Integer.MIN_VALUE,Integer.MIN_VALUE,Integer.MIN_VALUE};
        boolean filterTukar[]={false,false,false,false};
        Switch switch1, switch2, switch3, switch4;

        private LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                Log.d("Long",Double.toString(longitude));
                Log.d("Lat",Double.toString(latitude));
            }

            public void onStatusChanged(String provider, int status, Bundle extras){
            }

            public void onProviderEnabled(String provider){
            }

            public void onProviderDisabled(String provider){
            }
        };

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_input_nilai_perbandingan);


            activity=this;
            filterharga=(EditText)findViewById(R.id.filterHarga);
            filterjarak=(EditText)findViewById(R.id.filterJarak);
            filterpopuler=(EditText)findViewById(R.id.filterPopuleritas);
            filterrating=(EditText)findViewById(R.id.filterRating);
            switch1=(Switch)findViewById(R.id.switch1);
            switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        filterTukar[0]=true;
                        switch1.setText("MAX");
                        Toast.makeText(getApplicationContext(), "Switch 1 MAX",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        filterTukar[0]=false;
                        switch1.setText("MIN");
                        Toast.makeText(getApplicationContext(), "Switch 1 MIN",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
            switch2=(Switch)findViewById(R.id.switch2);
            switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        filterTukar[1]=true;
                        switch2.setText("MAX");
                        Toast.makeText(getApplicationContext(), "Switch 2 MAX",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        filterTukar[1]=false;
                        switch2.setText("MIN");
                        Toast.makeText(getApplicationContext(), "Switch 2 MIN",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
            switch3=(Switch)findViewById(R.id.switch3);
            switch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        filterTukar[2]=true;
                        switch3.setText("MAX");
                        Toast.makeText(getApplicationContext(), "Switch 3 MAX",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        filterTukar[2]=false;
                        switch3.setText("MIN");
                        Toast.makeText(getApplicationContext(), "Switch 3 MIN",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
            switch4=(Switch)findViewById(R.id.switch4);
            switch4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        filterTukar[3]=true;
                        switch4.setText("MAX");
                        Toast.makeText(getApplicationContext(), "Switch 4 MAX",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        filterTukar[3]=false;
                        switch4.setText("MIN");
                        Toast.makeText(getApplicationContext(), "Switch 4 MIN",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
            seek1();
            seek2();
            seek3();
            seek4();

            //harga, rating, populeritas, jarak
            matriks_berpasangan =new double[][]
                            {{1 ,a12,a13,a14},
                            {a21,1  ,a23,a24},
                            {a31,a32,1  ,a34},
                            {a41,a42,a43,1  }};


        }

        public void seek1(){
            mySeekBar=(SeekBar)findViewById(R.id.mySeekBar);
            mySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                int progressChanged = 0;
                Toast toast=Toast.makeText(getApplicationContext(), "Bobot Harga: " + progressChanged+"/9",
                        Toast.LENGTH_SHORT);
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    progressChanged = progress+1;
                    toast.cancel();
                    toast=Toast.makeText(getApplicationContext(), "Bobot Harga: " + progressChanged+"/9",
                            Toast.LENGTH_SHORT);

                    toast.show();
                }

                public void onStartTrackingTouch(SeekBar seekBar) {

                    // TODO Auto-generated method stub
                }

                public void onStopTrackingTouch(SeekBar seekBar) {
                    nilaiSeek1=progressChanged;
//                    Toast.makeText(getApplicationContext(), "Bobot Harga: " + progressChanged+"/9",
//                            Toast.LENGTH_SHORT).show();
                }
            });
        }

        public void seek2(){
            mySeekBar2=(SeekBar)findViewById(R.id.mySeekBar2);
            mySeekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                int progressChanged = 0;
                Toast toast=Toast.makeText(getApplicationContext(), "Bobot Jarak: " + progressChanged+"/9",
                        Toast.LENGTH_SHORT);
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    progressChanged = progress+1;
                    toast.cancel();
                    toast=Toast.makeText(getApplicationContext(), "Bobot Jarak: " + progressChanged+"/9",
                            Toast.LENGTH_SHORT);

                    toast.show();
                }

                public void onStartTrackingTouch(SeekBar seekBar) {

                    // TODO Auto-generated method stub
                }

                public void onStopTrackingTouch(SeekBar seekBar) {
                    nilaiSeek2=progressChanged;
//                    Toast.makeText(getApplicationContext(), "Bobot Jarak: " + progressChanged+"/9",
//                            Toast.LENGTH_SHORT).show();
                }
            });
        }

        public void seek3(){
            mySeekBar3=(SeekBar)findViewById(R.id.mySeekBar3);
            mySeekBar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                int progressChanged = 0;
                Toast toast=Toast.makeText(getApplicationContext(), "Bobot Populeritas: " + progressChanged+"/9",
                        Toast.LENGTH_SHORT);
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    progressChanged = progress+1;
                    toast.cancel();
                    toast=Toast.makeText(getApplicationContext(), "Bobot Populeritas: " + progressChanged+"/9",
                            Toast.LENGTH_SHORT);

                    toast.show();
                }

                public void onStartTrackingTouch(SeekBar seekBar) {

                    // TODO Auto-generated method stub
                }

                public void onStopTrackingTouch(SeekBar seekBar) {
                    nilaiSeek3=progressChanged;
                }
            });
        }

        public void seek4(){
            mySeekBar4=(SeekBar)findViewById(R.id.mySeekBar4);
            mySeekBar4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                int progressChanged = 0;
                Toast toast=Toast.makeText(getApplicationContext(), "Bobot Rating: " + progressChanged+"/9",
                        Toast.LENGTH_SHORT);
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    progressChanged = progress+1;
                    toast.cancel();
                    toast=Toast.makeText(getApplicationContext(), "Bobot Rating: " + progressChanged+"/9",
                            Toast.LENGTH_SHORT);

                    toast.show();
                }

                public void onStartTrackingTouch(SeekBar seekBar) {

                    // TODO Auto-generated method stub
                }

                public void onStopTrackingTouch(SeekBar seekBar) {
                      nilaiSeek4=progressChanged;
//                    Toast.makeText(getApplicationContext(), "Bobot Rating: " + progressChanged+"/9",
//                            Toast.LENGTH_SHORT).show();
                }
            });
        }

        public void setFilter(View view){
            if ((nilaiSeek1==1)&&(nilaiSeek2==1)&&(nilaiSeek3==1)&&(nilaiSeek4==1)){
                myPosition(view);
            }

            for (int i = 0; i < 4; i++) {
                filter[i]=Integer.MIN_VALUE;
            }
            switch1.setChecked(false);
            switch2.setChecked(false);
            switch3.setChecked(false);
            switch4.setChecked(false);
            filterrating.setVisibility(View.GONE);
            filterpopuler.setVisibility(View.GONE);
            filterjarak.setVisibility(View.GONE);
            filterharga.setVisibility(View.GONE);
            Button proses=(Button)findViewById(R.id.buttonProses);
            proses.setVisibility(View.GONE);

            if ((nilaiSeek1>1)||(nilaiSeek2>1)||(nilaiSeek3>1)||(nilaiSeek4>1)){
                proses.setVisibility(View.VISIBLE);
                if (nilaiSeek1>1){
                    filterharga.setVisibility(View.VISIBLE);
                    switch1.setVisibility(View.VISIBLE);
                }
                if (nilaiSeek2>1){
                    filterjarak.setVisibility(View.VISIBLE);
                    switch2.setVisibility(View.VISIBLE);
                }
                if (nilaiSeek3>1){
                    filterpopuler.setVisibility(View.VISIBLE);
                    switch3.setVisibility(View.VISIBLE);
                }
                if (nilaiSeek4>1){
                    filterrating.setVisibility(View.VISIBLE);
                    switch4.setVisibility(View.VISIBLE);
                }

                proses.requestFocus();
            }


        }

        public void myPosition(View view){

            if (filterharga.length()>0){
                filter[0]=Double.valueOf(filterharga.getText().toString());
            }
            if (filterjarak.length()>0){
                filter[1]=Double.valueOf(filterjarak.getText().toString());
            }
            if (filterpopuler.length()>0){
                filter[2]=Double.valueOf(filterpopuler.getText().toString());
            }
            if (filterrating.length()>0){
                filter[3]=Double.valueOf(filterrating.getText().toString());
            }
            /*ProgressDialog progress = new ProgressDialog(view.getContext());
            progress.setTitle("Get Posistion");
            progress.setMessage("Get Position");
            progress.show();*/
            ScrollView layoutform=(ScrollView) findViewById(R.id.scrollUtama);
            layoutform.setVisibility(View.GONE);
            Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
            toolbar.setVisibility(View.GONE);
            RelativeLayout loadingPanel=(RelativeLayout) findViewById(R.id.loadingPanel);

            loadingPanel.setLayoutParams(new RelativeLayout.LayoutParams(getWindowManager().getDefaultDisplay().getWidth(),getWindowManager().getDefaultDisplay().getHeight()));
            loadingPanel.setVisibility(View.VISIBLE);

            a12=nilaiSeek1/nilaiSeek4;
            a13=nilaiSeek1/nilaiSeek3;
            a14=nilaiSeek1/nilaiSeek2;
            a23=nilaiSeek4/nilaiSeek3;
            a24=nilaiSeek4/nilaiSeek2;
            a34=nilaiSeek3/nilaiSeek2;

            a21=nilaiSeek4/nilaiSeek1;
            a31=nilaiSeek3/nilaiSeek1;
            a41=nilaiSeek2/nilaiSeek1;
            a32=nilaiSeek3/nilaiSeek4;
            a42=nilaiSeek2/nilaiSeek4;
            a43=nilaiSeek2/nilaiSeek3;

            matriks_berpasangan =new double[][]
                            {{1 ,a12,a13,a14},
                            {a21,1  ,a23,a24},
                            {a31,a32,1  ,a34},
                            {a41,a42,a43,1  }};
            //harga rating populer jarak
            //rating
            //populer
            //jarak


            getMyPosition();
            sendDataAlternatifToAHPTOPSIS();


        }

        private void getMyPosition(){


            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION )) {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_ACCESS_FINE_LOCATION);
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_ACCESS_FINE_LOCATION);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                // Permission has already been granted
            }


            LocationManager lm = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

            int googlePlayStatus = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
            if (googlePlayStatus != ConnectionResult.SUCCESS) {
                GooglePlayServicesUtil.getErrorDialog(googlePlayStatus, this, -1).show();
                this.finish();
            } else {
                if (mGoogleMap != null) {
                    mGoogleMap.setMyLocationEnabled(true);
                    mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
                    mGoogleMap.getUiSettings().setAllGesturesEnabled(true);
                }
            }

            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, locationListener);
            Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            longitude = location.getLongitude();
            latitude = location.getLatitude();

            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 10, locationListener);
        }



        @Override
        public void onBackPressed() {
            startActivity(new Intent(getApplicationContext(), HomeUser.class));
            finish();

        }

        public void back(View view) {
            startActivity(new Intent(getApplicationContext(), HomeUser.class));
            finish();
        }



        @Override
        public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
            switch (requestCode) {
                case MY_PERMISSION_ACCESS_COARSE_LOCATION: {
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                        // permission was granted, yay! Do the
                        // contacts-related task you need to do.
                    } else {
                        Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                        // permission denied, boo! Disable the
                        // functionality that depends on this permission.
                    }
                    return;
                }

                // other 'case' lines to check for other
                // permissions this app might request.
            }
        }


        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            // Check which request we're responding to

            if (resultCode == RESULT_OK) {
                Integer dataPos = data.getExtras().getInt("menu");

            }

        }

        public void sendDataAlternatifToAHPTOPSIS(){
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            Query query=ref.child("Data Alternatif");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d("COba","hahahahaha");
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        if (data.getValue(TempatRental.class).getId()!=0){
                            ArrayTempatRental.add(data.getValue(TempatRental.class));
                        }
                    }



                    //TempatRental value = dataSnapshot.getValue(TempatRental.class);
                    //cek aja
                    //Log.d("TAG", "Value is: " + ArrayTempatRental.get(0).getRating());
                    //progress.dismiss();
                    /*AHP_TOPSIS ahptopsis=new AHP_TOPSIS(activity,ArrayTempatRental,latitude,longitude,matriks_berpasangan);
                    int[] urutan=ahptopsis.getUrutan();
                    Intent intent = new Intent(getApplicationContext(), ListHasilRekomen.class);
                    intent.putExtra("urutan",urutan);
                    finish();
                    startActivity(intent);*/
                    CobaHitung cobaHitung=new CobaHitung(activity,ArrayTempatRental,latitude,longitude,matriks_berpasangan,filter,filterTukar);

                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("TAG", "Failed to read value.", error.toException());
                }
            });

        }


    }
