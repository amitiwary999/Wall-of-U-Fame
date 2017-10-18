package com.example.amit.uniconnexample.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amit.uniconnexample.Activity.Chatstart;
import com.example.amit.uniconnexample.Message_model;
import com.example.amit.uniconnexample.Activity.NewTabActivity;
import com.example.amit.uniconnexample.R;
import com.example.amit.uniconnexample.utils.Utils;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by amit on 18/2/17.
 */

public class Msgfrag extends Fragment {
    RecyclerView rview;
    private DatabaseReference newsnd;
    SwipeRefreshLayout refresh;
    private FirebaseAuth auth;
    public Msgfrag() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_msg_frag,container,false);
        rview = (RecyclerView)view. findViewById(R.id.mchat_list);
        refresh=(SwipeRefreshLayout)view.findViewById(R.id.refresh);
        //  FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        auth=FirebaseAuth.getInstance();
        rview.setLayoutManager(new LinearLayoutManager(getActivity()));
        newsnd=FirebaseDatabase.getInstance().getReference().child("Smessage").child(auth.getCurrentUser().getUid());
        newsnd.keepSynced(true);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        // if(isNetworkConnected()) {
        FirebaseRecyclerAdapter<Message_model, MessageViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Message_model, MessageViewHolder>(
                Message_model.class,
                R.layout.activity_messageitem,
                MessageViewHolder.class,
                newsnd
        ) {
            @Override
            protected void populateViewHolder(MessageViewHolder viewHolder, Message_model model, int position) {
                final String msg_key = getRef(position).getKey();
                //    Toast.makeText(Message.this,msg_key,Toast.LENGTH_LONG).show();
                // mDatabase.add
                viewHolder.bindData(model);
                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isNetworkConnected()) {
                            Intent i = new Intent(getActivity(), Chatstart.class);
                            i.putExtra("chat", msg_key);
                            startActivity(i);
                          // getActivity(). finish();
                        }else{
                            Toast.makeText(getActivity(),"No internet connection",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        };
        rview.setAdapter(firebaseRecyclerAdapter);
        return view;
    }

    public void refresh(){
        FirebaseRecyclerAdapter<Message_model, MessageViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Message_model, MessageViewHolder>(
                Message_model.class,
                R.layout.activity_messageitem,
                MessageViewHolder.class,
                newsnd
        ) {
            @Override
            protected void populateViewHolder(MessageViewHolder viewHolder, Message_model model, int position) {
                final String msg_key = getRef(position).getKey();
                //    Toast.makeText(Message.this,msg_key,Toast.LENGTH_LONG).show();
                // mDatabase.add
                viewHolder.bindData(model);
                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isNetworkConnected()) {
                            Intent i = new Intent(getActivity(), Chatstart.class);
                            i.putExtra("chat", msg_key);
                            startActivity(i);
                          //  getActivity().finish();
                        }else{
                            Toast.makeText(getActivity(),"No internet connection",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        };
        rview.setAdapter(firebaseRecyclerAdapter);
        refreshcomplete();
    }
    public void refreshcomplete(){
        refresh.setRefreshing(false);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((NewTabActivity)getActivity()).setTitle("    Message     ");
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder{
        View view;
        public MessageViewHolder(View itemView) {
            super(itemView);
            view=itemView;
        }
        public void bindData(Message_model model){
            TextView tmsg=(TextView)view.findViewById(R.id.txt);
            TextView tname=(TextView)view.findViewById(R.id.txtname);
            ImageView iview=(ImageView)view.findViewById(R.id.photo);
            tmsg.setText(model.getMsg());
            tname.setText(model.getName());
            if(model.getImage()!=null)
                iview.setImageBitmap(Utils.decodeBase64(model.getImage()));
            else{
            }
        }
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(
                Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
