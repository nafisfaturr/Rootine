package com.example.rootine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class TanamanAdapter extends BaseAdapter {
    private Context context;
    private List<Tanaman> tanamanList;

    public TanamanAdapter(Context context, List<Tanaman> tanamanList){
        this.context = context;
        this.tanamanList = tanamanList;
    }
    @Override
    public int getCount() {
        return tanamanList.size();
    }

    @Override
    public Object getItem(int position) {
        return tanamanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_tanaman, viewGroup, false);
        }

        Tanaman tanaman = tanamanList.get(position);

        ImageView iconTanaman = convertView.findViewById(R.id.ic_tanaman);
        TextView namaTanaman = convertView.findViewById(R.id.nama_tnm);

        iconTanaman.setImageResource(tanaman.imgID);
        namaTanaman.setText(tanaman.nama);

        return convertView;
    }
}
