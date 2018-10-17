package com.example.jerikohosea.rekmon.ServiceAndClassObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.example.jerikohosea.rekmon.MetodeSPK.AHP_TOPSIS;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class CobaHitung {
    private double [][] matriks_keputusan;
    double atributDataAlter[][];
    ArrayList<TempatRental> ArrayTempatRental = new ArrayList<>();
    ArrayList<HitungJarak> ArrayAsync = new ArrayList<>();
    private int alternatif = 6;//gak bener
    private double []jarak=new double[10];
    private double [][] matriks_berpasangan;
    private static final int kriteria = 5;
    Activity activity;
    int counter=0;
    double []filter;
    boolean []filterTukar;

    public CobaHitung(Activity activity, ArrayList ArrayTempatRentalModel, double latitude, double longitude, double [][] matriks_berpasangan,double[] filter,boolean[]filterTukar){
        this.filter=filter;
        this.filterTukar=filterTukar;
        this.activity=activity;
        this.matriks_berpasangan=matriks_berpasangan;
        this.ArrayTempatRental=ArrayTempatRentalModel;
        atributDataAlter=new double[ArrayTempatRental.size()][6];
        for (int i = 0; i < ArrayTempatRental.size(); i++) {
            atributDataAlter[i][0]=ArrayTempatRental.get(i).getHarga();
            atributDataAlter[i][1]=ArrayTempatRental.get(i).getRating();
            atributDataAlter[i][2]=ArrayTempatRental.get(i).getPopuleritas();
            atributDataAlter[i][3]=ArrayTempatRental.get(i).getLat();
            atributDataAlter[i][4]=ArrayTempatRental.get(i).getLon();
            atributDataAlter[i][5]=ArrayTempatRental.get(i).getId();
        }
        alternatif=ArrayTempatRental.size();
        matriks_keputusan=new double[alternatif][kriteria];

        for (int i = 0; i < alternatif; i++) {
            for (int j = 0; j < kriteria; j++) {
                if (j==3){
                    ArrayAsync.add(i,new HitungJarak());
                    ArrayAsync.get(i).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,(Double.toString(atributDataAlter[i][3])),(Double.toString(atributDataAlter[i][4])),Double.toString(latitude),Double.toString(longitude),Integer.toString(i));
                }else if(j==4){
                    matriks_keputusan[i][j]=atributDataAlter[i][5];
                }else{
                    matriks_keputusan[i][j]=atributDataAlter[i][j];
                }
            }
        }

    }

    private class HitungJarak extends AsyncTask<String, Void, Double> {

        private Exception exception;
        protected Double doInBackground(String... urls) {

            StringBuilder stringBuilder = new StringBuilder();
            double dist = 0.1;
            int distInt;
            try {
                String url = "https://maps.googleapis.com/maps/api/directions/json?origin="+urls[2]+","+urls[3]+"&destination="+urls[0]+","+urls[1]+"&key=AIzaSyCIRamdXsRwe_47f6biRRD6XTXiqo4qhuo";
                HttpPost httppost = new HttpPost(url);
                HttpClient client = new DefaultHttpClient();
                HttpResponse response;
                stringBuilder = new StringBuilder();
                response = client.execute(httppost);
                HttpEntity entity = response.getEntity();
                InputStream stream = entity.getContent();
                int b;
                while ((b = stream.read()) != -1) {
                    stringBuilder.append((char) b);
                }
                Log.d("jarakfull",stringBuilder.toString());
            } catch (ClientProtocolException e) {
            } catch (IOException e) {
            }

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject = new JSONObject(stringBuilder.toString());
                JSONArray array = jsonObject.getJSONArray("routes");
                JSONObject routes = array.getJSONObject(0);
                JSONArray legs = routes.getJSONArray("legs");
                JSONObject steps = legs.getJSONObject(0);
                JSONObject distance = steps.getJSONObject("distance");
                distInt=Integer.parseInt(distance.getString("value") );
                if (distInt<1000){
                    dist=distInt/1000;
                    dist=Double.parseDouble(new DecimalFormat("##.##").format(dist));
                }else{
                    dist = Double.parseDouble(distance.getString("text").replaceAll("[^\\.0123456789]","") );
                }
                matriks_keputusan[Integer.valueOf(urls[4])][3]=dist;
                Log.d("semogaaa",Double.toString(dist));
                Log.d("nih int",Integer.toString(distInt));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return dist;
        }

        protected void onPostExecute(Double result) {
            counter++;
            if (counter==alternatif){
                AHP_TOPSIS ahptopsis=new AHP_TOPSIS(activity, matriks_berpasangan,matriks_keputusan,filter,filterTukar);
            }
            super.onPostExecute(result);
        }
    }

}
