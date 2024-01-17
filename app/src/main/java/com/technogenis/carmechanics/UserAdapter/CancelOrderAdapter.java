package com.technogenis.carmechanics.UserAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.technogenis.carmechanics.Model.BookModel;
import com.technogenis.carmechanics.R;

import java.util.List;

public class CancelOrderAdapter extends RecyclerView.Adapter<CancelOrderAdapter.MyViewHolder>{

    private Context context;
    private List<BookModel> mDataList;

    public CancelOrderAdapter(Context context, List<BookModel> mDataList) {
        this.context = context;
        this.mDataList = mDataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myview = LayoutInflater.from(context).inflate(R.layout.booking_list,parent,false);

        return new MyViewHolder(myview);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        BookModel model = mDataList.get(position);

        if (model.getBookRequest().equals("cancel"))
        {
            holder.card_layout.setVisibility(View.VISIBLE);
            holder.orderStatus_text.setText("Booking Status: "+model.getBookRequest());
            holder.orderKey.setText("Booking ID: "+model.getBookKey());
            holder.booking_time.setText("Book Date: "+model.getBookDate());
        }
        else
        {
            holder.card_layout.setVisibility(View.GONE);
        }

//        holder.itemView.setOnClickListener(v -> {
//            Intent intent = new Intent(context, ViewBookDetailsActivity.class);
//            intent.putExtra("name",model.getName());
//            intent.putExtra("phoneNumber",model.getPhoneNumber());
//            intent.putExtra("persons",model.getPersons());
//            intent.putExtra("message",model.getMessage());
//            intent.putExtra("bookDate",model.getBookDate());
//            intent.putExtra("bookTime",model.getBookTime());
//            intent.putExtra("bookKey",model.getBookKey());
//            intent.putExtra("userUID",model.getUserUID());
//            intent.putExtra("orderDate",model.getOrderDate());
//            intent.putExtra("orderTime",model.getOrderTime());
//            intent.putExtra("bookRequest",model.getBookRequest());
//            intent.putExtra("bookMarqueeName",model.getBookGarageMarqueeName());
//            intent.putExtra("bookMarqueeAddress",model.getBookGarageAddress());
//            intent.putExtra("bookMarqueeContact",model.getBookGarageContact());
//            context.startActivity(intent);
//        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{


        TextView orderStatus_text,view_details,orderKey,booking_time;
        CardView card_layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            card_layout = itemView.findViewById(R.id.card_layout);
            view_details = itemView.findViewById(R.id.view_details);
            orderStatus_text = itemView.findViewById(R.id.orderStatus_text);
            orderKey = itemView.findViewById(R.id.orderKey);
            booking_time = itemView.findViewById(R.id.booking_time);

            card_layout.setVisibility(View.GONE);
        }
    }
}
