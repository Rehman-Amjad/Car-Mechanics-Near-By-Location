package com.technogenis.carmechanics.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.technogenis.carmechanics.Model.ChatModel;
import com.technogenis.carmechanics.R;

import java.util.ArrayList;

public class AdminChatAdapter extends RecyclerView.Adapter<AdminChatAdapter.MyViewHolder>
{
    private Context context;
    private ArrayList<ChatModel> mDataList;

    public AdminChatAdapter(Context context, ArrayList<ChatModel> mDataList) {
        this.context = context;
        this.mDataList = mDataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View myView = LayoutInflater.from(context).inflate(R.layout.admin_chat_list,parent,false);

        return new MyViewHolder(myView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ChatModel model = mDataList.get(position);

        holder.userChat_text.setVisibility(View.GONE);
        holder.my_chat_layout.setVisibility(View.GONE);
        holder.chat_time_text.setVisibility(View.GONE);
        holder.userChat_layout.setVisibility(View.GONE);
        holder.user_time_text.setVisibility(View.GONE);

        if (model.getMessageType().equals("user"))
        {
            holder.userChat_layout.setVisibility(View.VISIBLE);
            holder.userChat_text.setVisibility(View.VISIBLE);
            holder.userChat_text.setText(model.getMessage());

            holder.user_time_text.setVisibility(View.VISIBLE);
            holder.user_time_text.setText(model.getSendingTime());
        }
        if (model.getMessageType().equals("owner"))
        {
            holder.my_chat_layout.setVisibility(View.VISIBLE);
            holder.my_chat_text.setVisibility(View.VISIBLE);
            holder.my_chat_text.setText(model.getMessage());

            holder.chat_time_text.setVisibility(View.VISIBLE);
            holder.chat_time_text.setText(model.getSendingTime());
        }


    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView my_chat_text,userChat_text,chat_time_text,user_time_text;
        LinearLayout my_chat_layout,userChat_layout;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            my_chat_text = itemView.findViewById(R.id.my_chat_text);
            userChat_text = itemView.findViewById(R.id.userChat_text);
            my_chat_layout = itemView.findViewById(R.id.my_chat_layout);
            chat_time_text = itemView.findViewById(R.id.chat_time_text);
            userChat_layout = itemView.findViewById(R.id.userChat_layout);
            user_time_text = itemView.findViewById(R.id.user_time_text);

        }
    }
}
