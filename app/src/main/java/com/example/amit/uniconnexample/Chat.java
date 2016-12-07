package com.example.amit.uniconnexample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amit.uniconnexample.utils.Utils;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by amit on 7/12/16.
 */

public class Chat extends Fragment  {
    private RecyclerView mChat;
    private DatabaseReference mDatabase;
    private FirebaseAuth auth;
    FirebaseUser user;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_chat,container,false);
        mChat=(RecyclerView)view.findViewById(R.id.mchat_list);
        user = FirebaseAuth.getInstance().getCurrentUser();
        auth=FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("chat");
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Chatusermodel,Chatviewholder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Chatusermodel, Chatviewholder>(
                Chatusermodel.class,
                R.layout.activity_chatitem,
                Chatviewholder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(Chatviewholder viewHolder, Chatusermodel model, int position) {
                viewHolder.bindData(model);
            }
        };
        mChat.setAdapter(firebaseRecyclerAdapter);
    }

    public static class Chatviewholder extends RecyclerView.ViewHolder{
        View mView;
        public Chatviewholder(View itemView) {
            super(itemView);
            mView=itemView;
        }
        public void bindData(Chatusermodel model){
            TextView post_name=(TextView)mView.findViewById(R.id.bname);
            ImageView pro_pic=(ImageView)mView.findViewById(R.id.pimage);
            if(model.getName()==null){
                post_name.setText("Anonyms");
               // nam="Anonyms";
            }else{
                post_name.setText(model.getName());
               // nam=model.getName();
            }
            if(model.getPropic()!=null)
            pro_pic.setImageBitmap(Utils.decodeBase64(model.getPropic()));
        }
    }
}
