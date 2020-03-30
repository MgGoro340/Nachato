package com.magus.nachato.Adapter;

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
import com.magus.nachato.MessageActivity;
import com.magus.nachato.R;
import com.magus.nachato.model.User;

import java.util.List;

public class UserAdapter  extends RecyclerView .Adapter<UserAdapter.ViewHolder>{

    private Context mContext;
    private List<User> mUsers;

    public UserAdapter(Context mContext, List<User> mUsers) {
        this.mUsers = mUsers;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from (mContext).inflate (R.layout.user_item,parent,false);

        return new UserAdapter.ViewHolder (view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final User user = mUsers.get(position);
        holder.username.setText (user.getUserName ());

        if ( user.getImageURL ().equals ("default")){
            holder.profile_image.setImageResource (R.mipmap.ic_launcher_glasses_round);
        } else {
            Glide.with(mContext).load(user.getImageURL ()).into(holder.profile_image);
        }

        holder.itemView.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent(mContext, MessageActivity.class);
                inten.putExtra ("userid",user.getId ());
                mContext.startActivity (inten);
            }
        });




    }

    @Override
    public int getItemCount() {
        return mUsers.size ();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{


        public TextView username;
        public ImageView profile_image;

        public ViewHolder(@NonNull View itemView) {
            super (itemView);

            username = itemView.findViewById (R.id.username);
            profile_image = itemView.findViewById (R.id.profile_image);

        }
    }


}
