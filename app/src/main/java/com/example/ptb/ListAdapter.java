package com.example.ptb;

import android.content.Context;
import android.util.Log;
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

    private static final String TAG = "ListAdapter";
    private ArrayList<Tambalban> listTb;
    private Context context;
    private SharePreferenceManager spManager;

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
        holder.tvJam.setText(listTb.get(position).getJambuka() + " - " + listTb.get(position).getJamtutup());
        int rating = listTb.get(position).getTubles() ? 4 : 3;

        String latS = listTb.get(position).getLatitude().replace(",", "");
        String lngS = listTb.get(position).getLongitude().replace(",", "");
        Log.d("list" + position, latS);
        Log.d("list" + position, lngS);
        double lat = Double.parseDouble(latS);
        double lng = Double.parseDouble(lngS);
        double jarak = getHarvesine(lat, lng);
        String jarakS = String.format("%.2f", jarak);
        holder.tvJarak.setText("Jarak : " + jarakS + " km");
        String ratingS = String.format("%.2f",listTb.get(position).getRating());
        holder.tvRating.setText( ratingS+ " Star");
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.logo_tambal_ban).error(R.drawable.logokuning);
        Glide.with(context).load(listTb.get(position).getFotobengkel()).apply(requestOptions).into(holder.ivFoto);
        Log.d("list" + "arb", "aaaa");

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

    public double getHarvesine(double lat, double lng) {
        final int R = 6371; // Radious of the earth
        spManager = new SharePreferenceManager(context);
        double lat1 = spManager.getSPDouble(SharePreferenceManager.SP_Lat, 0);
        double lon1 = spManager.getSPDouble(SharePreferenceManager.SP_Lng, 0);
        double lat2 = lat;
        double lon2 = lng;
        double latDistance = toRad(lat2 - lat1);
        double lonDistance = toRad(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) *
                        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;
        return distance;
    }

    private static double toRad(double value) {
        return value * Math.PI / 180;
    }


}
