package com.example.jerikohosea.rekmon.MetodeSPK;

/**
 * Created by Jupe Taek on 4/12/2018.
 */

public class AHP {

    private static final int kriteria = 4;
    private static final double random_index = 0.09;
    private double total_perkolom;
    private double [][] matriks_berpasangan ={{1,0.333333,3,1,0.5},
            {3,1,4,2,2},
            {0.333333,0.25,1,0.2,0.5},
            {1,0.5,5,1,3},{2,0.5,2,0.333333,1}};
    private double [][]mb_normalisasi=new double[kriteria][kriteria];
    private double []bobot_prioritas=new double[kriteria];
    private double eigen_maks;
    private double nilai_ci;
    private double nilai_cr;

    public AHP(double[][] matriks_berpasangan){
        this.matriks_berpasangan=matriks_berpasangan;
    }

    double[][] getNormalisasi(){
        for (int i = 0; i < matriks_berpasangan[0].length; i++) {
            total_perkolom =0;
            for (int j = 0; j < matriks_berpasangan.length; j++) {
                total_perkolom += matriks_berpasangan[j][i];
            }

            for (int k = 0; k < matriks_berpasangan.length; k++) {
                mb_normalisasi[k][i]= matriks_berpasangan[k][i]/ total_perkolom;
            }
        }
        return mb_normalisasi;
    }
    
    double[] getBobot_prioritas(){
        for (int i = 0; i < bobot_prioritas.length; i++) {
            for (int j = 0; j < bobot_prioritas.length; j++) {
                bobot_prioritas[i]+= mb_normalisasi[i][j];
            }
            bobot_prioritas[i]/= kriteria;
        }
        return bobot_prioritas;
    }

    double getEigenMaks(){
        double nilai_kepentingan=0;
        for (int i = 0; i < kriteria; i++) {
            for (int j = 0; j < kriteria; j++) {
                nilai_kepentingan+=matriks_berpasangan[j][i];
            }
            nilai_kepentingan*=bobot_prioritas[i];
            eigen_maks+=nilai_kepentingan;
            nilai_kepentingan=0;
        }
        return eigen_maks;
    }

    double getNilaiCI(){

        nilai_ci=(eigen_maks-kriteria)/(kriteria-1);
        return nilai_ci;
    }

    double getNilaiCR(){

        nilai_cr=nilai_ci/random_index;
        return nilai_cr;
    }

}
