package com.example.rootine;

public class CuacaModel {
    private String cityName;
    private String temper;

    public CuacaModel() {
        // Inisialisasi nilai default atau kosong jika diperlukan
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getTemper() {
        return temper;
    }

    public void setTemper(String temper) {
        this.temper = temper;
    }
}
