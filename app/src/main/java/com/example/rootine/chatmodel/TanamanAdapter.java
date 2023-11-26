package com.example.rootine.chatmodel;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.rootine.Tanaman;

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
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
