package com.technogenis.carmechanics.UserAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.technogenis.carmechanics.Model.ServicesModel;
import com.technogenis.carmechanics.R;

import java.util.List;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.MyViewFinderHolder>
{
    private Context context;
    private List<ServicesModel> mDataList;


    public ServicesAdapter(Context context, List<ServicesModel> mDataList) {
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


        ServicesModel model = mDataList.get(position);


            holder.services_name.setText(model.getServiceCategory());
            holder.service_charges.setText("RS: "+model.getServiceCharges());
//            holder.garage_name.setText(model.getGarageOwnerName());
            holder.garage_name.setVisibility(View.GONE);
            holder.delete_image.setVisibility(View.GONE);
            holder.edit_Image.setVisibility(View.GONE);

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
