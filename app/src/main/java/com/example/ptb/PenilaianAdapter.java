package com.example.ptb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ptb.model.Penilaian;
import com.example.ptb.model.Tambalban;

import java.util.ArrayList;

public class PenilaianAdapter extends RecyclerView.Adapter<PenilaianAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Penilaian> listPenilaian;

    public PenilaianAdapter( ArrayList<Penilaian> listPenilaian, Context context) {
        this.listPenilaian = listPenilaian;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_penilaian, parent, false);
        PenilaianAdapter.ViewHolder vh = new PenilaianAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvUser.setText(listPenilaian.get(position).getUser());
        holder.ratingBar.setRating(Float.parseFloat(listPenilaian.get(position).getRating()));
        holder.tvKomen.setText(listPenilaian.get(position).getKomentar());
    }

    @Override
    public int getItemCount() {
        return listPenilaian.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUser, tvKomen;
        RatingBar ratingBar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUser = itemView.findViewById(R.id.tvUser);
            tvKomen = itemView.findViewById(R.id.tvKomen);
            ratingBar = itemView.findViewById(R.id.ratingSmall);
        }
    }
}
