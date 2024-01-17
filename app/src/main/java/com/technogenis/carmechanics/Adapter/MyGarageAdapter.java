package com.technogenis.carmechanics.Adapter;

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
import com.google.firebase.auth.FirebaseAuth;
import com.technogenis.carmechanics.AdminPanal.MyGarageDetailActivity;
import com.technogenis.carmechanics.Model.GarageModel;
import com.technogenis.carmechanics.R;

import java.util.List;

public class MyGarageAdapter extends RecyclerView.Adapter<MyGarageAdapter.MyViewFinderHolder>
{
    private Context context;
    private List<GarageModel> mDataList;


    public MyGarageAdapter(Context context, List<GarageModel> mDataList) {
        this.context = context;
        this.mDataList = mDataList;
    }

    @NonNull
    @Override
    public MyViewFinderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myview = LayoutInflater.from(context).inflate(R.layout.my_garage_list,parent,false);

        return new MyViewFinderHolder(myview);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewFinderHolder holder, int position) {

        FirebaseAuth mAuth  = FirebaseAuth.getInstance();
        String userUID = mAuth.getUid();

        GarageModel model = mDataList.get(position);

        assert userUID != null;
        if (userUID.equals(model.getOwnerUserUID()))
        {
            holder.marqueeAddDate_text.setText("Added Date: " + model.getCurrentDate());
            Glide.with(context).load(model.getGarageCoverLink()).into(holder.cover_image);
            holder.marqueeName_text.setText("Name: " + model.getGarageName());
            holder.marqueeOwner_text.setText("Garage by: " + model.getGarageOwnerName());
        }



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

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class MyViewFinderHolder extends RecyclerView.ViewHolder{

        TextView marqueeAddDate_text,marqueeName_text,marqueeOwner_text;
        ImageView cover_image;

        public MyViewFinderHolder(@NonNull View itemView) {
            super(itemView);

            marqueeAddDate_text = itemView.findViewById(R.id.marqueeAddDate_text);
            marqueeName_text = itemView.findViewById(R.id.marqueeName_text);
            marqueeOwner_text = itemView.findViewById(R.id.marqueeOwner_text);
            cover_image = itemView.findViewById(R.id.cover_image);
        }
    }
}
