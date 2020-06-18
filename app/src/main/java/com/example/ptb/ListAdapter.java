package com.example.ptb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ptb.model.Tambalban;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private ArrayList<Tambalban> listTb;
    private Context context;

    public ListAdapter(ArrayList<Tambalban> listTb, Context context) {
        this.listTb = listTb;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemtb, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
holder.tvNama.setText(listTb.get(position).getNama());
        holder.tvAlamat.setText(listTb.get(position).getAlamat());
        holder.tvJam.setText(listTb.get(position).getJambuka()+" - "+listTb.get(position).getJamtutup());
        int rating = listTb.get(position).getTubles()?4:3;
        holder.tvRating.setText(rating+" Star");
        holder.tvJarak.setText("Destination : ");
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.logo_tambal_ban).error(R.drawable.logokuning);
        Glide.with(context).load(listTb.get(position).getFotobengkel()).apply(requestOptions).into(holder.ivFoto);

        holder.cvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.cvItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return listTb.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvAlamat, tvRating, tvJam, tvJarak;
        ImageView ivFoto;
        CardView cvItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvAlamat = itemView.findViewById(R.id.tvAlamat);
            tvJam = itemView.findViewById(R.id.tvJam);
            tvJarak = itemView.findViewById(R.id.tvJarak);
            tvRating = itemView.findViewById(R.id.tvRating);
            ivFoto = itemView.findViewById(R.id.ivFoto);
            cvItem = itemView.findViewById(R.id.cvItem);
        }
    }
}
