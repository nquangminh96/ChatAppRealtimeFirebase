package com.example.chatappfirebase.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chatappfirebase.MainActivity;
import com.example.chatappfirebase.MessageActivity;
import com.example.chatappfirebase.Model.Chats;
import com.example.chatappfirebase.Model.User;
import com.example.chatappfirebase.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    public static final int TYPE_MESS_LEFT = 0;
    public static final int TYPE_MESS_RIGHT = 1;
    private Context context;
    private ArrayList<Chats> arrayList;
    private String imageURL;

    FirebaseUser firebaseUser;

    public MessageAdapter(Context context, ArrayList<Chats> arrayList, String imageURL) {
        this.context = context;
        this.arrayList = arrayList;
        this.imageURL = imageURL;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == TYPE_MESS_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, viewGroup, false);
            return new MessageAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, viewGroup, false);
            return new MessageAdapter.ViewHolder(view);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder viewHolder, int i) {
        Chats chats = arrayList.get(i);
        viewHolder.message.setText(chats.getMessage());
        if (imageURL.equals("default")) {
            viewHolder.avatar.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(context).load(imageURL).into(viewHolder.avatar);
        }
        if (i == arrayList.size()-1){
            if (chats.isIsseen()==true){
                viewHolder.txtSeen.setText("Seen");
            }else viewHolder.txtSeen.setText("Delivered");
        }
        else viewHolder.txtSeen.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView avatar;
        public TextView message;
        public TextView txtSeen;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.profile_image);
            message = itemView.findViewById(R.id.message);
            txtSeen = itemView.findViewById(R.id.txt_seen);
        }

    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (arrayList.get(position).getSender().equals(firebaseUser.getUid())) {
            return TYPE_MESS_RIGHT;
        } else return TYPE_MESS_LEFT;
    }
}
