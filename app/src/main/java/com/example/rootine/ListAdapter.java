package com.example.rootine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends ArrayAdapter<Produk2> {

    private ListAdapterListener listener;
    private List<Boolean> isExpandedList;
    private int expandedPosition = -1;
    public void setListener(ListAdapterListener listener) {
        this.listener = listener;
    }

    public interface ListAdapterListener {
        void onUbahButtonClicked(String orderId, String nama_produk, String alamat, int gambar, String jumlah);
        void onHapusButtonClicked(String orderId);
        void onCheckoutButtonClicked(String orderId, String nama_produk, String alamat, String varian, String jumlah, String harga);
    }

    private class ViewHolder {
        ViewFlipper flipper;
        Button ubah;
        Button hapus;
        Button checkout;
        TextView selengkapnya;
        TextView selesai;
        TextView status;
        ImageView gambarP;
        LinearLayout goToDetail;
    }
    public ListAdapter(Context context, ArrayList<Produk2> produk2ArrayList){
        super(context, R.layout.list_view,produk2ArrayList);
        isExpandedList = new ArrayList<>(produk2ArrayList.size());
        for (int i = 0; i < produk2ArrayList.size(); i++) {
            isExpandedList.add(false);
        }
    }

    @NotNull
    @Override
    public View getView(int position, @NotNull View convertView, @NotNull ViewGroup parent){

        Produk2 produk2 = getItem(position);
        ViewHolder holder;

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view,parent,false);
            holder = new ViewHolder();
            holder.flipper = convertView.findViewById(R.id.flipper);
            holder.ubah = convertView.findViewById(R.id.ubah);
            holder.hapus = convertView.findViewById(R.id.hapus);
            holder.checkout = convertView.findViewById(R.id.checkout);
            holder.selengkapnya = convertView.findViewById(R.id.selengkapnya);
            holder.selesai = convertView.findViewById(R.id.selesai);
            holder.status = convertView.findViewById(R.id.status);
            holder.gambarP = convertView.findViewById(R.id.foto_produk);
            holder.goToDetail = convertView.findViewById(R.id.toDetail);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        //panggil data kedalam layout listview
        TextView namaProduk = convertView.findViewById(R.id.namaProduk);
        TextView varianPesan = convertView.findViewById(R.id.varianB);
        TextView hargaProduk = convertView.findViewById(R.id.harga);
        TextView tokoProduk = convertView.findViewById(R.id.toko);
        ImageView gambarProduk = convertView.findViewById(R.id.foto_produk);
        TextView statusProduk = convertView.findViewById(R.id.status);
        HorizontalScrollView scroll = convertView.findViewById(R.id.scroll);

        namaProduk.setText(produk2.nama_produk );
        varianPesan.setText("("+produk2.varian+")");
        hargaProduk.setText(produk2.harga);
        gambarProduk.setImageResource(produk2.gambar);
        tokoProduk.setText(produk2.nama_produk);
        //end//

        // Melakukan pengaturan tampilan berdasarkan status isExpanded
        if (position == expandedPosition) {
            holder.selengkapnya.setVisibility(View.GONE);
            holder.selesai.setVisibility(View.VISIBLE);
            holder.ubah.setVisibility(View.VISIBLE);
            holder.hapus.setVisibility(View.VISIBLE);
            holder.checkout.setVisibility(View.VISIBLE);
        } else {
            holder.selengkapnya.setVisibility(View.VISIBLE);
            holder.selesai.setVisibility(View.GONE);
            holder.ubah.setVisibility(View.GONE);
            holder.hapus.setVisibility(View.GONE);
            holder.checkout.setVisibility(View.GONE);
        }
        if (produk2.status.equals("dikeranjang")){
            holder.status.setVisibility(View.GONE);
        }else {
            holder.selengkapnya.setVisibility(View.GONE);
            holder.status.setVisibility(View.VISIBLE);
            statusProduk.setText(produk2.status);
            notifyDataSetChanged();
        }

        holder.selengkapnya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tutup item yang mungkin sedang diperluas
                if (expandedPosition != -1) {
                    isExpandedList.set(expandedPosition, false);
                }

                // Buka item yang baru diklik
                expandedPosition = position;
                isExpandedList.set(position, true);
                notifyDataSetChanged();

                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Scroll ke posisi hapus pada HorizontalScrollView
                        int scrollToX = holder.checkout.getRight();
                        scroll.smoothScrollTo(scrollToX, 0);
                    }
                }, 10);
            }
        });

        holder.selesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isExpandedList.set(position, false);
                expandedPosition = -1; // Tidak ada item yang diperluas
                notifyDataSetChanged();

            }
        });

        holder.ubah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onUbahButtonClicked(produk2.id,produk2.nama_produk,produk2.alamat,produk2.gambar,produk2.jumlah);
                }
            }
        });

        holder.hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onHapusButtonClicked(produk2.id);
                }

                scroll.smoothScrollTo(holder.hapus.getLeft(), 0);
            }
        });

        holder.checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onCheckoutButtonClicked(produk2.id,produk2.nama_produk,produk2.alamat,produk2.varian,produk2.jumlah, produk2.harga);
                }

                scroll.smoothScrollTo(holder.checkout.getLeft(), 0);
            }
        });

        return convertView;
    }

}