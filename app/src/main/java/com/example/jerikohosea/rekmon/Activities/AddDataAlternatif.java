package com.example.jerikohosea.rekmon.Activities;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.io.File;
import java.io.IOException;
import java.util.List;

import id.zelory.compressor.Compressor;

public class AddDataAlternatif extends AppCompatActivity implements View.OnClickListener ,OnMapReadyCallback{
    private GoogleMap mMap;
    Marker marker;

    TempatRental tempat;
    String titleMarker ="Tempat Rental";
    LatLng defLoc = new LatLng(-34, 151);
    String DownloadUri;
    boolean isAdd = true;
    double percent,latitude,longitude;
    boolean isUploadSuccess;
    Uri imagefile;
    StorageReference storageReference;
    FirebaseStorage storage;
    StorageMetadata metadata;
    private DatabaseReference mDatabase;
    private EditText Nama, Harga, Popularitas, Rating, NomorTelp, Deskripsi,SearchLoc;
    private ImageView FotoProf;
    private Button AddData, AddImage, SearchLocation, Reset;
    final int RESULT_LOAD_IMAGE = 245;
    private File actualSizeFile,compressedFile;
    ImageView transparentImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data_alternatif);

        FotoProf = (ImageView) findViewById(R.id.fotoProf);
        AddImage = (Button) findViewById(R.id.buttonImg);
        AddData = (Button) findViewById(R.id.buttonAdd);
        Reset=(Button)findViewById(R.id.reset);
        SearchLocation = (Button) findViewById(R.id.buttonSearchLoc);
        Nama = (EditText) findViewById(R.id.namaTempatRental);
        SearchLoc = (EditText) findViewById(R.id.searchLoc);
        Harga = (EditText) findViewById(R.id.hargaRental);
        Popularitas = (EditText) findViewById(R.id.popularitasTempatRental);
        Rating = (EditText) findViewById(R.id.ratingTempatRental);
        NomorTelp = (EditText) findViewById(R.id.noTelp);
        Deskripsi = (EditText) findViewById(R.id.deskripsi);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        metadata = new StorageMetadata.Builder().setContentType("image/jpeg").build();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        tempat= Parcels.unwrap(intent.getParcelableExtra("TempatDetail"));

        if (tempat!=null){ //jika page detail dari tombol edit
            latitude=0;
            longitude=0;
            Nama.setHint(tempat.getNama());
            Harga.setHint(String.valueOf(tempat.getHarga()));
            Popularitas.setHint(String.valueOf(tempat.getPopuleritas()));
            Rating.setHint(String.valueOf(tempat.getRating()));
            NomorTelp.setHint(tempat.getNoTelp());
            Deskripsi.setHint(tempat.getDeskripsi());
            defLoc=new LatLng(tempat.getLat(),tempat.getLon());
            titleMarker =tempat.getNama();
            TextView judulToolbar=(TextView)findViewById(R.id.judulToolbar);
            judulToolbar.setText("Edit Data Alternatif");
            AddData.setText("Save Data Alternatif");
            Picasso.with(getApplicationContext())
                    .load(tempat.getImageUrl())
                    .fit()
                    .centerCrop()
                    .into(FotoProf);

        }else{
            Reset.setVisibility(View.GONE);
        }



        final ScrollView scroll = (ScrollView) findViewById(R.id.main_scrollview);
        ImageView transparent = (ImageView)findViewById(R.id.imagetrans);

        transparent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        scroll.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        scroll.requestDisallowInterceptTouchEvent(false);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        scroll.requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:
                        return true;
                }
            }
        });

        AddData.setOnClickListener(this);
        AddImage.setOnClickListener(this);
        SearchLocation.setOnClickListener(this);

    }

    private void addImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "choose picture"), RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data && null != data.getData()){
            imagefile = data.getData();
            actualSizeFile=new File((imagefile.getPath()));
            try {
                compressedFile=new Compressor(this).compressToFile(actualSizeFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //imagefile=Uri.fromFile(compressedFile);
            Picasso.with(getApplicationContext())
                    .load(imagefile)
                    .fit()
                    .centerCrop()
                    .into(FotoProf);
        }
    }

    public String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void addData(){
        Toast.makeText(AddDataAlternatif.this, "Add Data terklik", Toast.LENGTH_SHORT).show();
        String nama, harga, popularitas, rating, nomorTelp, deskripsi;

        if (tempat!=null){
            mDatabase = FirebaseDatabase.getInstance().getReference();
            if (Nama.length()>0){
                mDatabase.child("Data Alternatif").child(String.valueOf(tempat.getId())).child("nama").setValue(Nama.getText().toString());
                tempat.setNama(Nama.getText().toString());
            }
            if (Harga.length()>0){
                mDatabase.child("Data Alternatif").child(String.valueOf(tempat.getId())).child("harga").setValue(Integer.parseInt(Harga.getText().toString()));
                tempat.setHarga(Integer.parseInt(Harga.getText().toString()));
            }
            if (Popularitas.length()>0){
                mDatabase.child("Data Alternatif").child(String.valueOf(tempat.getId())).child("populeritas").setValue(Integer.parseInt(Popularitas.getText().toString()));
                tempat.setPopuleritas(Integer.parseInt(Popularitas.getText().toString()));
            }
            if (Rating.length()>0){
                mDatabase.child("Data Alternatif").child(String.valueOf(tempat.getId())).child("rating").setValue(Double.parseDouble(Rating.getText().toString()));
                tempat.setRating(Double.parseDouble(Rating.getText().toString()));
            }
            if (NomorTelp.length()>0){
                mDatabase.child("Data Alternatif").child(String.valueOf(tempat.getId())).child("noTelp").setValue(NomorTelp.getText().toString());
                tempat.setNoTelp(NomorTelp.getText().toString());
            }
            if (Deskripsi.length()>0){
                mDatabase.child("Data Alternatif").child(String.valueOf(tempat.getId())).child("deskripsi").setValue(Deskripsi.getText().toString());
                tempat.setDeskripsi(Deskripsi.getText().toString());
            }
            if ((latitude==0)&&(longitude==0)){

            }else{
                mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("Data Alternatif").child(String.valueOf(tempat.getId())).child("lat").setValue(latitude);
                tempat.setLat(latitude);
                mDatabase.child("Data Alternatif").child(String.valueOf(tempat.getId())).child("lon").setValue(longitude);
                tempat.setLon(longitude);
            }
            if (imagefile!=null){
                uploadFile(Integer.valueOf(String.valueOf(tempat.getId())));
            }else{
                Toast.makeText(AddDataAlternatif.this, "Data Telah Dirubah",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), DetailTempatRentalActivity.class);
                intent.putExtra("Tempat", Parcels.wrap(tempat));
                startActivity(intent);
            }

        }else{
            nama=Nama.getText().toString();
            harga=Harga.getText().toString();
            popularitas=Popularitas.getText().toString();
            rating=Rating.getText().toString();
            nomorTelp=NomorTelp.getText().toString();
            deskripsi=Deskripsi.getText().toString();
            if((Nama.length()>0)&&(Harga.length()>0)&&(Popularitas.length()>0)&&(Rating.length()>0)&&(NomorTelp.length()>0)&&(Deskripsi.length()>0)&&(imagefile!=null)){
                final TempatRental tempatRent=new TempatRental();
                tempatRent.setLat(latitude);
                tempatRent.setLon(longitude);
                tempatRent.setNoTelp(nomorTelp);
                tempatRent.setNama(nama);
                tempatRent.setHarga(Integer.parseInt(harga));
                tempatRent.setPopuleritas(Integer.parseInt(popularitas));
                tempatRent.setRating(Double.parseDouble(rating));
                tempatRent.setDeskripsi(deskripsi);
                mDatabase = FirebaseDatabase.getInstance().getReference();
//                Toast.makeText(AddDataAlternatif.this, "Add data masuk else",
//                        Toast.LENGTH_SHORT).show();
                //mDatabase.setValue(null);

                mDatabase.child("Data Alternatif").orderByChild("id").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            String  keyStr=data.getKey();

                            int keyInt=(Integer.parseInt(keyStr))+1;
                            tempatRent.setId(keyInt);
                            mDatabase.child("Data Alternatif").child(String.valueOf(keyInt)).setValue(tempatRent);
//                            Toast.makeText(AddDataAlternatif.this, String.valueOf(keyInt),
//                                    Toast.LENGTH_SHORT).show();
                            uploadFile(keyInt);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w("TAG", "Failed to read value.", error.toException());
                    }
                });
            }else{
                Toast.makeText(AddDataAlternatif.this, "Data belum lengkap",
                        Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void uploadFile(final int id){
        if(imagefile != null){
            final ProgressDialog progress = new ProgressDialog(AddDataAlternatif.this);
            progress.setTitle("Upload Image");
            progress.show();

            StorageReference ref = storageReference.child("Images/"
                    + System.currentTimeMillis() + "." + getFileExtension(imagefile));

            ref.putFile(imagefile)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progress.dismiss();
                            isUploadSuccess = true;
                            //noinspection VisibleForTests,ConstantConditions
                            DownloadUri = taskSnapshot.getDownloadUrl().toString();
                            if(isAdd) {

                                mDatabase.child("Data Alternatif").child(String.valueOf(id)).child("imageUrl").setValue(DownloadUri);

                                Toast.makeText(AddDataAlternatif.this, "Data Berhasil Ditambahkan",
                                        Toast.LENGTH_SHORT).show();
                                if (tempat!=null){
                                    tempat.setImageUrl(DownloadUri);
                                    Intent intent = new Intent(getApplicationContext(), DetailTempatRentalActivity.class);
                                    intent.putExtra("Tempat", Parcels.wrap(tempat));
                                    startActivity(intent);
                                }else{
                                    Intent intent = new Intent(AddDataAlternatif.this, ListDataAlternatif.class);
                                    startActivity(intent);
                                }

                            }
                            new DatabaseReference.CompletionListener(){

                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    isAdd = false;
                                    Toast.makeText(AddDataAlternatif.this,
                                            "ADD DATA ERROR " + databaseError,
                                            Toast.LENGTH_LONG)
                                            .show();
                                }
                            };
//                            Toast.makeText(AddDataAlternatif.this,
//                                    "ADD DATA SUCCESS",
//                                    Toast.LENGTH_LONG)
//                                    .show();
                            //finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddDataAlternatif.this,
                                    "Error"+e,
                                    Toast.LENGTH_LONG)
                                    .show();
                            progress.dismiss();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //noinspection VisibleForTests
                            percent = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progress.setMessage("Upload" + ((int)percent) + " %");
                        }
                    });
        }else{
            Toast.makeText(AddDataAlternatif.this, "Image File is Not Found or Error", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonAdd:
                addData();
                break;
            case R.id.buttonImg:
                addImage();
                break;
            case R.id.buttonSearchLoc:
                onMapSearch();
                break;
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap=googleMap;

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latlng) {
                // TODO Auto-generated method stub

                if (marker != null) {
                    marker.remove();
                }
                setMarkerGMaps(latlng.latitude,latlng.longitude);

            }
        });
        // Add a marker in Sydney and move the camera

        mMap.addMarker(new MarkerOptions().position(defLoc).title(titleMarker));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(defLoc));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(defLoc, 15.0f));
        latitude = defLoc.latitude;
        longitude = defLoc.longitude;
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        if (mMap != null) {


            mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {

                @Override
                public boolean onMyLocationButtonClick() {

                    Location loc = mMap.getMyLocation();
                    setMarkerGMaps(loc.getLatitude(),loc.getLongitude());
                    return true;
                }

            });

        }
    }

    public void onMapSearch() {
        String location = SearchLoc.getText().toString();
        List<Address> addressList = null;

        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addressList.size()>0){
                Address address = addressList.get(0);
                setMarkerGMaps(address.getLatitude(),address.getLongitude());
            }else{
                Toast.makeText(AddDataAlternatif.this, "Alamat Tidak Ditemukan atau Kurang Lengkap",
                        Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void resetLoc(View view) {
        setMarkerGMaps(tempat.getLat(),tempat.getLon());
    }

    public void setMarkerGMaps(double lat, double lon){
        mMap.clear();
        LatLng latLng = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions().position(latLng).title(titleMarker));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15.0f));
        latitude = lat;
        longitude = lon;
    }

    @Override
    public void onBackPressed() {
        if (tempat!=null){
            Intent intent = new Intent(getApplicationContext(), DetailTempatRentalActivity.class);
            intent.putExtra("Tempat", Parcels.wrap(tempat));
            startActivity(intent);
        }else{
            startActivity(new Intent(getApplicationContext(), HomeAdmin.class));
            finish();
        }

    }

    public void back(View view){
        if (tempat!=null){
            Intent intent = new Intent(getApplicationContext(), DetailTempatRentalActivity.class);
            intent.putExtra("Tempat", Parcels.wrap(tempat));
            startActivity(intent);
        }else{
            startActivity(new Intent(getApplicationContext(), HomeAdmin.class));
            finish();
        }

    }
}
