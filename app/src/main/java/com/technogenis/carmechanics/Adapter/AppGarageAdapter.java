package com.technogenis.carmechanics.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.FirebaseDatabase;
import com.technogenis.carmechanics.AdminAppMain.LogGarageActivity;
import com.technogenis.carmechanics.AdminPanal.MyGarageDetailActivity;
import com.technogenis.carmechanics.AdminPanal.ServicesActivity;
import com.technogenis.carmechanics.Model.GarageModel;
import com.technogenis.carmechanics.R;


import java.util.List;

public class AppGarageAdapter extends RecyclerView.Adapter<AppGarageAdapter.MyViewFinderHolder>
{
    private Context context;
    private List<GarageModel> mDataList;


    public AppGarageAdapter(Context context, List<GarageModel> mDataList) {
        this.context = context;
        this.mDataList = mDataList;
    }

    @NonNull
    @Override
    public MyViewFinderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myview = LayoutInflater.from(context).inflate(R.layout.garage_list,parent,false);

        return new MyViewFinderHolder(myview);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewFinderHolder holder, int position) {



        GarageModel model = mDataList.get(position);


            holder.marqueeAddDate_text.setText("Added Date: " + model.getCurrentDate());
            Glide.with(context).load(model.getGarageCoverLink()).into(holder.cover_image);
            holder.marqueeName_text.setText("Name: " + model.getGarageName());
            holder.marqueeOwner_text.setText("Garage by: " + model.getGarageOwnerName());


        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MyGarageDetailActivity.class);
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


        holder.delete_image.setOnClickListener(v -> {
            AlertDialog.Builder builder=new AlertDialog.Builder(holder.marqueeName_text.getContext());
            builder.setTitle("Delete Services");
            builder.setMessage("Press Yes or No?");

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    FirebaseDatabase.getInstance().getReference().child("Garage")
                            .child(model.getGarageAddKey()).removeValue();
                    context.startActivity(new Intent(context, ServicesActivity.class));
                    ((LogGarageActivity)context).finish();
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            builder.show();
        });

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class MyViewFinderHolder extends RecyclerView.ViewHolder{

        TextView marqueeAddDate_text,marqueeName_text,marqueeOwner_text;
        ImageView cover_image,delete_image;

        public MyViewFinderHolder(@NonNull View itemView) {
            super(itemView);

            marqueeAddDate_text = itemView.findViewById(R.id.marqueeAddDate_text);
            marqueeName_text = itemView.findViewById(R.id.marqueeName_text);
            marqueeOwner_text = itemView.findViewById(R.id.marqueeOwner_text);
            cover_image = itemView.findViewById(R.id.cover_image);
            delete_image = itemView.findViewById(R.id.delete_image);
        }
    }
}
