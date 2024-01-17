package com.technogenis.carmechanics.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.technogenis.carmechanics.AdminPanal.AddNewServicesActivity;
import com.technogenis.carmechanics.AdminPanal.ServicesActivity;
import com.technogenis.carmechanics.Model.ServicesModel;
import com.technogenis.carmechanics.R;

import java.util.List;

public class MyServicesAdapter extends RecyclerView.Adapter<MyServicesAdapter.MyViewFinderHolder>
{
    private Context context;
    private List<ServicesModel> mDataList;


    public MyServicesAdapter(Context context, List<ServicesModel> mDataList) {
        this.context = context;
        this.mDataList = mDataList;
    }

    @NonNull
    @Override
    public MyViewFinderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myview = LayoutInflater.from(context).inflate(R.layout.my_services_list,parent,false);

        return new MyViewFinderHolder(myview);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewFinderHolder holder, int position) {

        FirebaseAuth mAuth  = FirebaseAuth.getInstance();
        String userUID = mAuth.getUid();

        ServicesModel model = mDataList.get(position);


        Log.d("adapterLog", "onBindViewHolder: "+model.getUserUID());

        if (model.getUserUID().equals(userUID))
        {
            holder.services_name.setText(model.getServiceCategory());
            holder.service_charges.setText("RS: "+model.getServiceCharges());
//            holder.garage_name.setText(model.getGarageOwnerName());
        }

        holder.edit_Image.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddNewServicesActivity.class);
            intent.putExtra("garageAddKey",model.getGarageAddKey());
            intent.putExtra("garageName",model.getGarageOwnerName());
            intent.putExtra("serviceCategory",model.getServiceCategory());
            intent.putExtra("serviceProviderName",model.getServiceProviderName());
            intent.putExtra("serviceCharges",model.getServiceCharges());
            intent.putExtra("serviceDetails",model.getServicesDetails());
            intent.putExtra("screenType","Adapter");
            context.startActivity(intent);
        });

        holder.delete_image.setOnClickListener(v -> {

            AlertDialog.Builder builder=new AlertDialog.Builder(holder.services_name.getContext());
            builder.setTitle("Delete Services");
            builder.setMessage("Press Yes or No?");

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    FirebaseDatabase.getInstance().getReference().child("Services")
                            .child(model.getServiceAddKey()).removeValue();
                    context.startActivity(new Intent(context, ServicesActivity.class));
                    ((ServicesActivity)context).finish();
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

        TextView services_name,service_charges,garage_name;
        ImageView edit_Image,delete_image;

        public MyViewFinderHolder(@NonNull View itemView) {
            super(itemView);

            services_name = itemView.findViewById(R.id.services_name);
            service_charges = itemView.findViewById(R.id.service_charges);
            garage_name = itemView.findViewById(R.id.garage_name);
            edit_Image = itemView.findViewById(R.id.edit_Image);
            delete_image = itemView.findViewById(R.id.delete_image);

        }
    }
}
