package com.example.rootine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ProdukAdapter extends BaseAdapter {
    private Context context;
    private List<Produk> produkList;

    public ProdukAdapter(Context context, List<Produk> produkList) {
        this.context = context;
        this.produkList = produkList;
    }

    @Override
    public int getCount() {
        return produkList.size();
    }

    @Override
    public Object getItem(int position) {
        return produkList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_produk, parent, false);
        }

        Produk produk = produkList.get(position);

        TextView namaProduk = convertView.findViewById(R.id.nama_prd);
        TextView descProduk = convertView.findViewById(R.id.desc_prd);
        ImageView gambarProduk = convertView.findViewById(R.id.image_prd);

        namaProduk.setText(produk.nm_produk);
        descProduk.setText(produk.desc_produk);
        gambarProduk.setImageResource(produk.img_produk);

        return convertView;
    }
}
