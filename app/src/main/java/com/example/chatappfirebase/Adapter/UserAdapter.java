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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context context;
    private ArrayList<User> arrayList;
    private boolean isChat;
    String theLastMessage;

    public UserAdapter(Context context, ArrayList<User> arrayList, boolean isChat) {
        this.context = context;
        this.arrayList = arrayList;
        this.isChat = isChat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview_listuser, viewGroup, false);

        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final User user = arrayList.get(i);
        viewHolder.name.setText(user.getUsername());
        if (user.getImageURL().equals("default")) {
            viewHolder.avatar.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(context).load(user.getImageURL()).into(viewHolder.avatar);
        }
        if (isChat) {
            lastMessage(user.getId(), viewHolder.txt_last_message);
        } else {
            viewHolder.txt_last_message.setVisibility(View.GONE);
        }
        if (isChat) {
            if (user.getStatus().equals("online")) {
                viewHolder.status_on.setVisibility(View.VISIBLE);
                viewHolder.status_off.setVisibility(View.GONE);
            }
            if (user.getStatus().equals("offline")) {
                viewHolder.status_on.setVisibility(View.GONE);
                viewHolder.status_off.setVisibility(View.VISIBLE);
            }
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, MessageActivity.class);
                i.putExtra("userID", user.getId());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView avatar;
        public TextView name;
        private CircleImageView status_on;
        private CircleImageView status_off;
        TextView txt_last_message;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            name = itemView.findViewById(R.id.username);
            status_on = itemView.findViewById(R.id.status_on);
            status_off = itemView.findViewById(R.id.status_off);
            txt_last_message = itemView.findViewById(R.id.last_message);

        }

    }

    private void lastMessage(final String userid, final TextView lastMessage) {
        theLastMessage = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chats chats = snapshot.getValue(Chats.class);
                    assert firebaseUser != null;
                    assert chats != null;
                    if (firebaseUser != null) {
                        if ((chats.getReceiver().equals(firebaseUser.getUid()) && chats.getSender().equals(userid))
                                || (chats.getReceiver().equals(userid) && chats.getSender().equals(firebaseUser.getUid()))) {
                            theLastMessage = chats.getMessage();
                        }
                    }
                }
                switch (theLastMessage) {
                    case "default":
                        lastMessage.setText("No Message");
                        break;
                    default:
                        lastMessage.setText(theLastMessage);
                        break;
                }
                theLastMessage = "default";
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
