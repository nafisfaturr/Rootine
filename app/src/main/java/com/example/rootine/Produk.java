package com.example.rootine;

public class Produk {
    String id, nm_produk, desc_produk,detail_produk,  hrg_produk, stk_produk, brt_produk, ktg_produk, detail_img;
    int img_produk;

    public Produk(String id, String nm_produk, String desc_produk ,String detail_produk, String hrg_produk, String stk_produk, String brt_produk, String ktg_produk, String detail_img, int img_produk) {
        this.id = id;
        this.nm_produk = nm_produk;
        this.desc_produk = desc_produk;
        this.detail_produk = detail_produk;
        this.hrg_produk = hrg_produk;
        this.stk_produk = stk_produk;
        this.brt_produk = brt_produk;
        this.ktg_produk = ktg_produk;
        this.img_produk = img_produk;
        this.detail_img = detail_img;
    }

    public String getId() {
        return id;
    }
    public String getNm_produk() {
        return nm_produk;
    }
    public String getDesc_produk() {
        return desc_produk;
    }
    public String getDetail_produk() {
        return detail_produk;
    }
    public String getHrg_produk() {
        return hrg_produk;
    }
    public String getStk_produk() {
        return stk_produk;
    }
    public String getBrt_produk() {
        return brt_produk;
    }
    public String getKtg_produk() {
        return ktg_produk;
    }
    public String getDetail_img() {
        return detail_img;
    }
    public int getImg_produk() {
        return img_produk;
    }
}
