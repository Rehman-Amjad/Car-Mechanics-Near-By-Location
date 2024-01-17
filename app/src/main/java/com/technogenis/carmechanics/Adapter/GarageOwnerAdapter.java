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


import com.google.firebase.database.FirebaseDatabase;
import com.technogenis.carmechanics.AdminAppMain.GarageActivity;
import com.technogenis.carmechanics.AdminAppMain.LogGarageActivity;
import com.technogenis.carmechanics.Model.UserModel;
import com.technogenis.carmechanics.R;

import java.util.List;

public class GarageOwnerAdapter extends RecyclerView.Adapter<GarageOwnerAdapter.MyViewFinderHolder>
{
    private Context context;
    private List<UserModel> mDataList;


    public GarageOwnerAdapter(Context context, List<UserModel> mDataList) {
        this.context = context;
        this.mDataList = mDataList;
    }

    @NonNull
    @Override
    public MyViewFinderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myview = LayoutInflater.from(context).inflate(R.layout.garage_owner_list,parent,false);


        return new MyViewFinderHolder(myview);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewFinderHolder holder, int position) {

        UserModel model = mDataList.get(position);

        holder.userName_text.setText("Name: " + model.getName());
        holder.userEmail_text.setText("Name: " + model.getEmail());
        holder.userPhone_text.setText("Name: " + model.getPhoneNumber());


        holder.delete_image.setOnClickListener(v -> {
            AlertDialog.Builder builder=new AlertDialog.Builder(holder.userName_text.getContext());
            builder.setTitle("Delete Account");
            builder.setMessage("Press Yes or No?");

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    FirebaseDatabase.getInstance().getReference().child("GarageOwnerAccounts")
                            .child(model.getUserUID()).removeValue();
                    context.startActivity(new Intent(context, GarageActivity.class));
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

      TextView userName_text,userEmail_text,userPhone_text;
      ImageView delete_image;

        public MyViewFinderHolder(@NonNull View itemView) {
            super(itemView);

            userName_text = itemView.findViewById(R.id.userName_text);
            userEmail_text = itemView.findViewById(R.id.userEmail_text);
            userPhone_text = itemView.findViewById(R.id.userPhone_text);
            delete_image = itemView.findViewById(R.id.delete_image);
        }
    }
}
