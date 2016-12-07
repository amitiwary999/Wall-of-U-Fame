package com.example.amit.uniconnexample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

            }
        };
    }

    public static class Chatviewholder extends RecyclerView.ViewHolder{
        View mView;
        public Chatviewholder(View itemView) {
            super(itemView);
            mView=itemView;
        }
        public void bindData(Chatusermodel model){

        }
    }
}
