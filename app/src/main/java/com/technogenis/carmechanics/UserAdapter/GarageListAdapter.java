package com.technogenis.carmechanics.UserAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.technogenis.carmechanics.Model.GarageModel;
import com.technogenis.carmechanics.R;
import com.technogenis.carmechanics.UserPanal.GarageDetailsActivity;


import java.util.List;

public class GarageListAdapter extends RecyclerView.Adapter<GarageListAdapter.MyViewFinderHolder>
{
    private Context context;
    private List<GarageModel> mDataList;

    public GarageListAdapter(Context context, List<GarageModel> mDataList) {
        this.context = context;
        this.mDataList = mDataList;
    }

    @NonNull
    @Override
    public MyViewFinderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myview = LayoutInflater.from(context).inflate(R.layout.garage_list_layout,parent,false);

        return new MyViewFinderHolder(myview);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewFinderHolder holder, int position) {

        GarageModel model = mDataList.get(position);
        holder.marqueeName_text.setText(model.getGarageName());
        Glide.with(context).load(model.getGarageCoverLink()).into(holder.cover_image);
        holder.marqueeAddress_text.setText(model.getGarageAddress());


        holder.infoDirection_image.setOnClickListener(v -> {
            Intent intent = new Intent(context, GarageDetailsActivity.class);
            intent.putExtra("garageName",model.getGarageName());
            intent.putExtra("garageOwnerName",model.getGarageOwnerName());
            intent.putExtra("garageContactNumber",model.getGarageContactNumber());
            intent.putExtra("garageBio",model.getGarageBio());
            intent.putExtra("ownerUserUID",model.getOwnerUserUID());
            intent.putExtra("garageAddKey",model.getGarageAddKey());
            intent.putExtra("currentTime",model.getCurrentTime());
            intent.putExtra("currentDate",model.getCurrentDate());
            intent.putExtra("garageCoverLink",model.getGarageCoverLink());
            intent.putExtra("garageAddress",model.getGarageAddress());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class MyViewFinderHolder extends RecyclerView.ViewHolder{

        TextView marqueeName_text,marqueeAddress_text;
        ImageView cover_image,infoDirection_image;

        public MyViewFinderHolder(@NonNull View itemView) {
            super(itemView);

            marqueeName_text = itemView.findViewById(R.id.marqueeName_text);
            marqueeAddress_text = itemView.findViewById(R.id.marqueeAddress_text);
            cover_image = itemView.findViewById(R.id.cover_image);
            infoDirection_image = itemView.findViewById(R.id.infoDirection_image);

        }
    }
}
