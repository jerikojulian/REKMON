package com.example.jerikohosea.rekmon.ServiceAndClassObject;

import org.parceler.Parcel;

/**
 * Created by Jupe Taek on 4/13/2018.
 */
@Parcel
public class TempatRental {

    private String  nama;
    private String imageUrl;
    private String noTelp;
    private long id;
    private double rating, lat, lon;
    private int harga, populeritas;

    private String deskripsi;

    public TempatRental(){

    }

    public TempatRental(long id, double lat, double lon, String nama, double rating, int harga, int populeritas){
        this.id=id;
        this.lat=lat;
        this.lon=lon;
        this.nama=nama;
        this.rating=rating;
        this.harga=harga;
        this.populeritas=populeritas;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getNoTelp() {
        return noTelp;
    }

    public void setNoTelp(String noTelp) {
        this.noTelp = noTelp;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public int getHarga() {
        return harga;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLat() {
        return lat;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLon() {
        return lon;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNama() {
        return nama;
    }

    public void setPopuleritas(int populeritas) {
        this.populeritas = populeritas;
    }

    public int getPopuleritas() {
        return populeritas;
    }


}
