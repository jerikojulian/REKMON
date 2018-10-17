package com.example.jerikohosea.rekmon.MetodeSPK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.jerikohosea.rekmon.Adapters.ListHasilRekomen;
import com.example.jerikohosea.rekmon.ServiceAndClassObject.TempatRental;

import java.util.ArrayList;

public class AHP_TOPSIS {
    ArrayList<TempatRental> ArrayTempatRental = new ArrayList<>();
    private static final int kriteria = 6;
    private static final int alternatif = 10;//gak bener
    double [][] dataAlternatif;
    private double []jarak_si_positif,si_positif, si_negatif,jarak_si_negatif,nilai_preferensi;
    private int []urutan;
    private double[]urutanJarak;
    double [][]mk_terbobot,matriks_keputusan,mk_normalisasi;
    double [][] mb_normalisasi;
    double []bobot_prioritas;
    double []bobot_prioritas_tes={0.3287581,0.3001633,0.2457516,0.1253267};
    double eigen_maks;
    double latitude;
    double longitude;
    Activity activity;
    double[][]matriks_berpasangan;
    double nilai_ci, nilai_cr;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public AHP_TOPSIS(Activity activity,double[][]matriks_berpasangan,double[][]matriks_keputusan,double[]filter,boolean[]filterTukar){
        /*ProgressDialog progress = new ProgressDialog(activity);
        progress.setTitle("AHP TOPSIS");
        progress.setMessage("AHP TOPSIS");
        progress.show();*/
        /*this.ArrayTempatRental=ArrayTempatRentalModel;

        if (ArrayTempatRental.size()<3){
            ArrayTempatRental.add(0,new TempatRental(0,0,0,"kosong",0,0,0));
            ArrayTempatRental.add(1,new TempatRental(0,0,0,"kosong",0,0,0));
            ArrayTempatRental.add(2,new TempatRental(0,0,0,"kosong",0,0,0));
        }*/

        /*dataAlternatif=new double[ArrayTempatRental.size()][kriteria];
        this.latitude=latitude;
        this.longitude=longitude;
        this.activity=activity;
        mDatabase = FirebaseDatabase.getInstance().getReference();
//        Log.d("TAG", "Value2 is: " + ArrayTempatRental.get(0).getRating());
        Log.d("TAG", "Value2 is: " + ArrayTempatRental.size());*/

        //ini harusnya pake parameter matriks_berpasangan
        AHP ahp=new AHP(matriks_berpasangan);
        mb_normalisasi =ahp.getNormalisasi();
        bobot_prioritas=ahp.getBobot_prioritas();
        eigen_maks=ahp.getEigenMaks();
        nilai_ci=ahp.getNilaiCI();
        nilai_cr=ahp.getNilaiCR();

        for (int i = 0; i <4 ; i++) {
                Log.d("Matriks berpasangan",Double.toString(matriks_berpasangan[i][0])+" "+Double.toString(matriks_berpasangan[i][1])+" "+Double.toString(matriks_berpasangan[i][2])+" "+Double.toString(matriks_berpasangan[i][3]));
        }

        for (int i = 0; i <4 ; i++) {
                Log.d("Normalisasi",Double.toString(mb_normalisasi[i][0])+" "+Double.toString(mb_normalisasi[i][1])+" "+Double.toString(mb_normalisasi[i][2])+" "+Double.toString(mb_normalisasi[i][3]));

        }

        for (int i = 0; i <4 ; i++) {
            Log.d("Bobot Prioritas",Double.toString(bobot_prioritas[i]));
        }

        Log.d("Eigen Maksimum",Double.toString(eigen_maks));

        Log.d("Nilai CI",Double.toString(nilai_ci));

        Log.d("Nilai CR",Double.toString(nilai_cr));

        for (int i = 0; i < ArrayTempatRental.size(); i++) {
            dataAlternatif[i][0]=ArrayTempatRental.get(i).getHarga();
            dataAlternatif[i][1]=ArrayTempatRental.get(i).getRating();
            dataAlternatif[i][2]=ArrayTempatRental.get(i).getPopuleritas();
            dataAlternatif[i][3]=ArrayTempatRental.get(i).getLat();
            dataAlternatif[i][4]=ArrayTempatRental.get(i).getLon();
            dataAlternatif[i][5]=ArrayTempatRental.get(i).getId();
        }
//        Log.d("Coba Data Alter di mode",Double.toString(ArrayTempatRental.get(0).getLon()));

        TOPSIS topsis= new TOPSIS(bobot_prioritas,matriks_keputusan);
        //matriks_keputusan=topsis.getMKeputusan();
        matriks_keputusan=topsis.getMKeputusan();
        mk_normalisasi=topsis.getMKNormalisasi();
        mk_terbobot=topsis.getMKTerbobot();
        jarak_si_positif=topsis.getJarakSIPositif();
        si_positif=topsis.getSIPositif();
        si_negatif= topsis.getSINegatif();
        jarak_si_negatif=topsis.getJarakSINegatif();
        nilai_preferensi=topsis.getNilaiPreferensi();
        urutan=topsis.getUrutan();
        urutanJarak=topsis.getUrutanJarak();

        for (int i = 0; i <alternatif ; i++) {
            Log.d("M Keputusan",Double.toString(matriks_keputusan[i][0])+" "+Double.toString(matriks_keputusan[i][1])+" "+Double.toString(matriks_keputusan[i][2])+" "+Double.toString(matriks_keputusan[i][3]));
        }

        for (int i = 0; i <alternatif ; i++) {
            Log.d("MK Normalisasi",Double.toString(mk_normalisasi[i][0])+" "+Double.toString(mk_normalisasi[i][1])+" "+Double.toString(mk_normalisasi[i][2])+" "+Double.toString(mk_normalisasi[i][3]));
        }

        for (int i = 0; i <alternatif ; i++) {
            Log.d("MK Terbobot",Double.toString(mk_terbobot[i][0])+" "+Double.toString(mk_terbobot[i][1])+" "+Double.toString(mk_terbobot[i][2])+" "+Double.toString(mk_terbobot[i][3]));
        }

        Log.d("SI Positif",Double.toString(si_positif[0])+" "+Double.toString(si_positif[1])+" "+Double.toString(si_positif[2])+" "+Double.toString(si_positif[3]));

        Log.d("SI Negatif",Double.toString(si_negatif[0])+" "+Double.toString(si_negatif[1])+" "+Double.toString(si_negatif[2])+" "+Double.toString(si_negatif[3]));

        for (int i = 0; i <alternatif ; i++) {
            Log.d("Jarak SI Positif",Double.toString(jarak_si_positif[i]));
        }

        for (int i = 0; i <alternatif ; i++) {
            Log.d("Jarak SI Negatif",Double.toString(jarak_si_negatif[i]));
        }

        for (int i = 0; i <alternatif ; i++) {
            Log.d("Nilai Preferensi",Double.toString(nilai_preferensi[i]));
        }

        for (int i = 0; i <10 ; i++) {
            Log.d("Urutan",Double.toString(urutan[i]));
        }

        for (int i = 0; i <10 ; i++) {
            Log.d("Urutan Jarak",Double.toString(urutanJarak[i]));
        }


        preferences = activity.getSharedPreferences("com.example.jerikohosea.rekmon", Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString("urutan1",String.valueOf(urutan[0]));
        editor.putString("urutan2",String.valueOf(urutan[1]));
        editor.putString("urutan3",String.valueOf(urutan[2]));
        editor.apply();

        Intent intent = new Intent(activity, ListHasilRekomen.class);
        intent.putExtra("urutan",urutan);
        intent.putExtra("urutan jarak",urutanJarak);
        intent.putExtra("filter",filter);
        intent.putExtra("filter tukar",filterTukar);
        activity.startActivity(intent);

    }



}
