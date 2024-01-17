package com.technogenis.carmechanics.UserAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.technogenis.carmechanics.Model.AdminChatListModel;
import com.technogenis.carmechanics.R;
import com.technogenis.carmechanics.UserPanal.UserChatActviity;

import java.util.ArrayList;

public class UserChatListAdapter extends RecyclerView.Adapter<UserChatListAdapter.ViewHolder>
{
    Context context;
    private ArrayList<AdminChatListModel> mDataList;

    public UserChatListAdapter(Context context, ArrayList<AdminChatListModel> mDataList) {
        this.context = context;
        this.mDataList = mDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myview = LayoutInflater.from(context).inflate(R.layout.chat_list_users,parent,false);

        return new ViewHolder(myview);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        AdminChatListModel model = mDataList.get(position);
        holder.email_text.setText(model.getGarageName());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, UserChatActviity.class);
            intent.putExtra("garageAddKey",model.getGarageAddKey());
            intent.putExtra("ownerUserUID",model.getOwnerUserUID());
            intent.putExtra("senderUID",model.getSenderUID());
            intent.putExtra("garageName",model.getGarageName());
            intent.putExtra("actType","adapter");
            context.startActivity(intent);
        });


    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView email_text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            email_text = itemView.findViewById(R.id.email_text);

        }
    }
}
