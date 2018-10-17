package com.example.jerikohosea.rekmon.MetodeSPK;

import android.util.Log;

import java.util.Arrays;

/**
 * Created by Jupe Taek on 4/12/2018.
 */

public class TOPSIS /*implements AsyncResponse*/{


    //0=harga
    //1=rating
    //2=populeritas
    //3=jarak
    //biaya 0,3
    //keuntungan 1,2
    private int alternatif = 6;//gak bener
    private static final int kriteria = 4;
    private double [][] matriks_keputusan;
    private double [][] atributDataAlter;
    private double [][] mk_normalisasi;
    private double []bobot_prioritas;
    private double [][]mk_terbobot;
    private double []si_positif={Integer.MAX_VALUE,Integer.MIN_VALUE,Integer.MIN_VALUE,Integer.MAX_VALUE};
    private double []si_negatif={Integer.MIN_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MIN_VALUE};
    private double []jarak_si_positif;
    private double []jarak_si_negatif;
    private double []nilai_preferensi;
    private double []nilai_preferensi_terurut;
    private double []nilai_preferensi_top3;
    private double total_perkolom;
    private int []urutan;
    private double []urutanJarak;

    private double jarak=0;

    public TOPSIS(double[] bobot_prioritas,double[][] matriks_keputusan){
        this.bobot_prioritas=bobot_prioritas;
        alternatif=matriks_keputusan.length;
        urutan=new int[alternatif];
        urutanJarak=new double[alternatif];
        this.matriks_keputusan=matriks_keputusan;
        alternatif=matriks_keputusan.length;
        mk_normalisasi =new double[alternatif][kriteria];
        mk_terbobot=new double[alternatif][kriteria];
        jarak_si_positif=new double[alternatif];
        jarak_si_negatif=new double[alternatif];
        nilai_preferensi=new double[alternatif];
        nilai_preferensi_terurut=new double[alternatif];
        nilai_preferensi_top3=new double[alternatif];
        //this.atributDataAlter=atributDataAlter;

    }

    public double[][] getMKeputusan(){
        return matriks_keputusan;
    }

    public double[][] getMKNormalisasi(){
        for (int i = 0; i < kriteria; i++) {
            total_perkolom =0;
            for (int j = 0; j < matriks_keputusan.length; j++) {
                total_perkolom += matriks_keputusan[j][i];
            }

            for (int k = 0; k < matriks_keputusan.length; k++) {
                mk_normalisasi[k][i]= matriks_keputusan[k][i]/ total_perkolom;
            }
        }
        return mk_normalisasi;
    }

    public double[][] getMKTerbobot(){

        for (int i = 0; i < alternatif; i++) {
            for (int j = 0; j < kriteria; j++) {
                mk_terbobot[i][j]=mk_normalisasi[i][j]*bobot_prioritas[j];
            }
        }
        Log.d("COBAAAA",Double.toString(mk_terbobot[0][1]));
        for (int i = 0; i < alternatif; i++) {
            for (int j = 0; j < kriteria; j++) {
                //solusi ideal positif n negatif
                if ((j==1) || (j==2)){//keuntungan
                    if (mk_terbobot[i][j]>si_positif[j]){
                        si_positif[j]=mk_terbobot[i][j];
                    }
                    if (mk_terbobot[i][j]<si_negatif[j]){
                        si_negatif[j]=mk_terbobot[i][j];
                    }
                }else if ((j==0) || (j==3)){//biaya
                    if (mk_terbobot[i][j]>si_negatif[j]){
                        si_negatif[j]=mk_terbobot[i][j];
                    }
                    if (mk_terbobot[i][j]<si_positif[j]){
                        si_positif[j]=mk_terbobot[i][j];
                    }
                }
            }
        }
        return mk_terbobot;
    }

    public double[] getSIPositif(){
        return si_positif;
    }

    public double[] getSINegatif(){
        return si_negatif;
    }

    public double[] getJarakSIPositif(){
        double temp=0;
        for (int i = 0; i < alternatif; i++) {
            for (int j = 0; j < kriteria; j++) {
                temp+=Math.pow((mk_terbobot[i][j]-si_positif[j]),2);
            }
            jarak_si_positif[i]=Math.sqrt(temp);
            temp=0;
        }
        return jarak_si_positif;
    }

    public double[] getJarakSINegatif(){
        double temp=0;
        for (int i = 0; i < alternatif; i++) {
            for (int j = 0; j < kriteria; j++) {
                temp+=Math.pow((mk_terbobot[i][j]-si_negatif[j]),2);
            }
            jarak_si_negatif[i]=Math.sqrt(temp);
            temp=0;
        }
        return jarak_si_negatif;
    }

    public double[] getNilaiPreferensi(){
        for (int i = 0; i < alternatif; i++) {
            nilai_preferensi[i]=(jarak_si_negatif[i]/(jarak_si_negatif[i]+jarak_si_positif[i]));
        }
        Log.d("COBAAAA",Double.toString(nilai_preferensi[2]));
        return nilai_preferensi;
    }

    public int[] getUrutan(){
        double temp=0;
        nilai_preferensi_terurut=Arrays.copyOf(nilai_preferensi,nilai_preferensi.length);
        Arrays.sort(nilai_preferensi_terurut);

        for (int i = 0; i < alternatif; i++) {
            nilai_preferensi_top3[i]=nilai_preferensi_terurut[(alternatif-1)-i];
        }

        for (int i = 0; i < nilai_preferensi.length; i++) {
            for (int j = 0; j < alternatif; j++) {
                if (nilai_preferensi_top3[j]==nilai_preferensi[i]){
                    urutan[j]=(int)matriks_keputusan[i][4];
                    urutanJarak[j]=(Double) matriks_keputusan[i][3];
                }
            }
        }


        return urutan;
    }

    public double[] getUrutanJarak(){
        return urutanJarak;
    }

/*
    @Override
    public void processFinish(Double output){
        //Here you will receive the result fired from async class
        //of onPostExecute(result) method.
        jarak=output;
        Log.d("Jarak",Double.toString(output));
    }*/



}
