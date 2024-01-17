package com.technogenis.carmechanics.NearByPlace;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.technogenis.carmechanics.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.viewHolder> {

    private final static int FADE_DURATION = 1000;

    List<Result> resultList;
    Context context;

    public PlacesAdapter(List<Result> resultList, Context context) {
        this.resultList = resultList;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_venue, parent, false);
        return new viewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {


        // Set the view to fade in
        setFadeAnimation(holder.itemView);

        Result result = resultList.get(position);
        holder.venueNameTv.setText(result.name);
        holder.venueCategoryTv.setText(result.address);

        if (result.distance!=null){
            holder.venueRatingTV.setText(result.distance);
            //holder.venueRating.setRating(Float.parseFloat(result.distance));
        }else {
            holder.venueRatingTV.setText("3.5");
        }

//        try {
//            holder.venueRating.setRating(Float.parseFloat(result.distance));
//        }catch (Exception e){
//            e.printStackTrace();
//        }


        holder.btnShowMap.setOnClickListener(v -> {
            Intent intent = new Intent(context, MapsActivity.class);
            intent.putExtra("latitude", result.getLatitude());
            intent.putExtra("longitude", result.getLongitude());
            intent.putExtra("placeName", result.getName());
            context.startActivity(intent);
        });

        holder.btnShowDirection.setOnClickListener(v -> {
            Intent intent = new Intent(context, PlaceOnMapActivity.class);
            intent.putExtra("latitude", result.getLatitude());
            intent.putExtra("longitude", result.getLongitude());
            intent.putExtra("placeName", result.getName());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {

        TextView venueNameTv;
        TextView venueCategoryTv;
        RatingBar venueRating;
        ImageView btnShowDirection;
        ImageView btnShowMap;
        TextView venueRatingTV;

        public viewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            venueNameTv = itemView.findViewById(R.id.venueNameTv);
            venueCategoryTv = itemView.findViewById(R.id.venueCategoryTv);
            venueRating = itemView.findViewById(R.id.venueRating);
            venueRatingTV=itemView.findViewById(R.id.venueRatingTV);
            btnShowDirection = itemView.findViewById(R.id.btnShowDirection);
            btnShowMap = itemView.findViewById(R.id.btnShowMap);


        }
    }

    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }
}
