package com.example.rootine;

public class Tanaman {
    String id, nama, latin, kesulitan, deskripsi, tips, hama, penyakit, kategori, d_img;
    int imgID;

    public Tanaman(String id, String nama, String latin, String kesulitan, String deskripsi, String tips, String hama, String penyakit, String kategori, String d_img, int imgID) {
        this.id = id;
        this.nama = nama;
        this.latin = latin;
        this.kesulitan = kesulitan;
        this.deskripsi = deskripsi;
        this.tips = tips;
        this.hama = hama;
        this.penyakit = penyakit;
        this.kategori = kategori;
        this.d_img = d_img;
        this.imgID = imgID;
    }

    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getLatin() {
        return latin;
    }

    public String getKesulitan() {
        return kesulitan;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public String getTips() {
        return tips;
    }

    public String getHama() {
        return hama;
    }

    public String getPenyakit() {
        return penyakit;
    }

    public String getKategori() {
        return kategori;
    }

    public String getD_img() {
        return d_img;
    }

    public int getImgID() {
        return imgID;
    }
}
