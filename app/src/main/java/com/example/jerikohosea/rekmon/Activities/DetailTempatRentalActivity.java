package com.example.jerikohosea.rekmon.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jerikohosea.rekmon.Adapters.ListDataAlternatif;
import com.example.jerikohosea.rekmon.R;
import com.example.jerikohosea.rekmon.ServiceAndClassObject.TempatRental;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

public class DetailTempatRentalActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TempatRental tempat;
    private TextView nama,deskripsi, rating, popularitas,noTelp;
    private Button edit, delete;
    private RatingBar ratingBar;
    private ImageView foto,user;
    private SharedPreferences preferences;
    private String cookies;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        preferences = getSharedPreferences("com.example.jerikohosea.rekmon", Context.MODE_PRIVATE);
        cookies = preferences.getString("aktor", null);

        Toast.makeText(DetailTempatRentalActivity.this,cookies, Toast.LENGTH_SHORT).show();



        Intent intent = getIntent();
        tempat = Parcels.unwrap(intent.getParcelableExtra("Tempat"));

        edit=(Button)findViewById(R.id.editData);
        delete=(Button)findViewById(R.id.deleteData);
        user=(ImageView)findViewById(R.id.imageView);
        popularitas=(TextView)findViewById(R.id.popularitas);
        rating=(TextView)findViewById(R.id.rating);
        nama=(TextView)findViewById(R.id.nama);
        deskripsi=(TextView)findViewById(R.id.deskripsi);
        ratingBar=(RatingBar)findViewById(R.id.ratingBar);
        foto=(ImageView)findViewById(R.id.fotoProf);
        noTelp=(TextView)findViewById(R.id.noTelp);

        if (cookies.equals("user")){
            edit.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
        }
        user.setImageDrawable(getResources().getDrawable(R.drawable.user));
        popularitas.setText(String.valueOf(tempat.getPopuleritas()));
        rating.setText(String.valueOf(tempat.getRating()));
        ratingBar.setRating(Float.parseFloat(String.valueOf(tempat.getRating())));
        nama.setText(tempat.getNama());
        deskripsi.setText(tempat.getDeskripsi());
        Picasso.with(getApplicationContext())
                .load(tempat.getImageUrl())
                .fit()
                .centerCrop()
                .into(foto);
        noTelp.setText(tempat.getNoTelp());


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng myPos = new LatLng(tempat.getLat(), tempat.getLon());
        mMap.addMarker(new MarkerOptions().position(myPos).title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(myPos));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom((myPos), 15.0f));
    }


    public void deleteData(View view){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Data Alternatif").child(String.valueOf(tempat.getId())).removeValue();
        Toast.makeText(DetailTempatRentalActivity.this, "Data Telah Dihapus", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), ListDataAlternatif.class));
        finish();
    }

    public void editData(View view) {
        Intent intent = new Intent(this, AddDataAlternatif.class);
        intent.putExtra("TempatDetail", Parcels.wrap(tempat));
        this.startActivity(intent);

    }

    public void back(View view) {
        if (cookies.equals("user")){
            finish();
        }else if(cookies.equals("admin")){
            startActivity(new Intent(getApplicationContext(), ListDataAlternatif.class));
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if (cookies.equals("user")){
            finish();
        }else if(cookies.equals("admin")){
            startActivity(new Intent(getApplicationContext(), ListDataAlternatif.class));
            finish();
        }
    }

    public void onCall(View view) {
        if (Build.VERSION.SDK_INT < 23) {
            phoneCall();
        }else {

            if (ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

                phoneCall();
            }else {
                final String[] PERMISSIONS_STORAGE = {android.Manifest.permission.CALL_PHONE};
                //Asking request Permissions
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 9);
            }
        }
    }

    private void phoneCall(){
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:"+tempat.getNoTelp()));
            this.startActivity(callIntent);
        }else{
            Toast.makeText(this, "You don't assign permission.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        boolean permissionGranted = false;
        switch(requestCode){
            case 9:
                permissionGranted = grantResults[0]== PackageManager.PERMISSION_GRANTED;
                break;
        }
        if(permissionGranted){
            phoneCall();
        }else {
            Toast.makeText(this, "You don't assign permission.", Toast.LENGTH_SHORT).show();
        }
    }
}
