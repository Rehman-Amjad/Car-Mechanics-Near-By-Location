package com.technogenis.carmechanics.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.technogenis.carmechanics.Model.ImageModel;
import com.technogenis.carmechanics.R;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder>
{
    private Context context;
    private ArrayList<ImageModel> mDataList;

    public ImageAdapter(Context context, ArrayList<ImageModel> mDataList) {
        this.context = context;
        this.mDataList = mDataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View myView = LayoutInflater.from(context).inflate(R.layout.image_item_list,parent,false);

        return new MyViewHolder(myView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ImageModel model = mDataList.get(position);
        Glide.with(context).load(model.getGarageImages()).into(holder.image_product);


    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView image_product;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            image_product = itemView.findViewById(R.id.image_product);
        }
    }
}
