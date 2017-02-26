package com.example.amit.uniconnexample.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amit.uniconnexample.Chatstart;
import com.example.amit.uniconnexample.Message;
import com.example.amit.uniconnexample.Message_model;
import com.example.amit.uniconnexample.NewTabActivity;
import com.example.amit.uniconnexample.R;
import com.example.amit.uniconnexample.utils.Utils;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by amit on 18/2/17.
 */

public class Msgfrag extends Fragment {
    RecyclerView rview;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager laymanager;
    private static ArrayList<Message_model> data;
    private DatabaseReference mDatabase,newmessage,newsnd,pdata;
    String key,name,pic;
    long num;
    SwipeRefreshLayout refresh;
    static String text;
    private FirebaseAuth auth;
    FirebaseUser user;
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
        pdata= FirebaseDatabase.getInstance().getReference();
        rview.setLayoutManager(new LinearLayoutManager(getActivity()));
        newsnd=FirebaseDatabase.getInstance().getReference().child("Smessage").child(auth.getCurrentUser().getUid());

        mDatabase = FirebaseDatabase.getInstance().getReference().child("message").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        newmessage = FirebaseDatabase.getInstance().getReference().child("Userdetail");
        newsnd.keepSynced(true);

        data = new ArrayList<Message_model>();
      //  setTitle("");
       // tablayoutbottom = (TabLayout) findViewById(R.id.tabLayoutbottom);
       // toolbar = (Toolbar) findViewById(R.id.toolbar);
       // Utils.setUpToolbarBackButton(Message.this, toolbar);
       // setupTabIconsBottom();

        // setupTabIcons();
      //  bindWidgetsWithAnEvent();
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
        //  }else{
        //    Toast.makeText(Message.this,"No internet connection",Toast.LENGTH_LONG).show();
        //}
       /* mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Toast.makeText(Message.this,"iicc  "+dataSnapshot.getChildrenCount(),Toast.LENGTH_SHORT).show();
                    key=snapshot.getKey();
                    Toast.makeText(Message.this,key,Toast.LENGTH_SHORT).show();
                    // newmessage=FirebaseDatabase.getInstance().getReference().child("Userdetail").child(key).child("photo");
                    newmessage.child(key).child("photo").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            pic= dataSnapshot.getValue(String.class);
                            //   Toast.makeText(Message.this,"ii  "+pic,Toast.LENGTH_LONG).show();
                            //    text.setText(name);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    newmessage.child(key).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                           name=dataSnapshot.getValue(String.class);
                            Toast.makeText(Message.this,"name  "+name,Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    num=snapshot.getChildrenCount();
                    Toast.makeText(Message.this,"iiccc  "+num,Toast.LENGTH_SHORT).show();
                    mDatabase.child(key).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot snapShot:dataSnapshot.getChildren()) {

                                num= num-1;
                           //     Toast.makeText(Message.this,"iiccc  "+num,Toast.LENGTH_SHORT).show();
                                if(num==0){
                                   // Toast.makeText(Message.this,"iicccl  "+num,Toast.LENGTH_SHORT).show();
                                 //   Toast.makeText(Message.this,"ii  "+snapShot.hasChild("msg2"),Toast.LENGTH_SHORT).show();
                                    if(snapShot.hasChild("msg1")){
                                        text=snapShot.child("msg1").getValue(String.class);
                                        Toast.makeText(Message.this,"ii  "+text,Toast.LENGTH_SHORT).show();
                                    }else if(snapShot.hasChild("msg2")){
                                        text=snapShot.child("msg2").getValue(String.class);
                                        Toast.makeText(Message.this,"iii  "+text,Toast.LENGTH_SHORT).show();
                                    }

                                    break;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    final String msgt=text;
                    final String pict=pic;
                    final String namet=name;
                    final String keyt=key;

                         Toast.makeText(Message.this,"iiii  "+msgt,Toast.LENGTH_SHORT).show();
                        Message_model model=new Message_model(pict,msgt,namet,keyt);
                        data.add(model);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        adapter=new Messageadapter(data);
        rview.setAdapter(adapter);
    }*/
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