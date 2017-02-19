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
import com.example.amit.uniconnexample.Notifclick;
import com.example.amit.uniconnexample.Notification;
import com.example.amit.uniconnexample.Notificationmodel;
import com.example.amit.uniconnexample.Profile;
import com.example.amit.uniconnexample.R;
import com.example.amit.uniconnexample.Settings;
import com.example.amit.uniconnexample.Tabs;
import com.example.amit.uniconnexample.utils.Utils;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by amit on 18/2/17.
 */

public class Notifrag extends Fragment {
    FirebaseAuth auth;
    String key,post_key;
    String tag;
    SwipeRefreshLayout refresh;
    private DatabaseReference mDatabasenotifdata,mDatanotiflike;
    RecyclerView notificationrecycle;
    public Notifrag() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_notif_frag,container,false);
        tag=this.getClass().getSimpleName();
        notificationrecycle=(RecyclerView)view. findViewById(R.id.mnotification_list);
      //  tablayoutbottom=(TabLayout)findViewById(R.id.tabLayoutbottom);
      //  toolbar=(Toolbar)findViewById(R.id.toolbar);
        refresh=(SwipeRefreshLayout)view.findViewById(R.id.refresh);
        // FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        auth=FirebaseAuth.getInstance();
        mDatanotiflike= FirebaseDatabase.getInstance().getReference().child("notificationdata").child("like").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        //  Toast.makeText(this,"check",Toast.LENGTH_LONG).show();

        mDatabasenotifdata= FirebaseDatabase.getInstance().getReference().child("notificationdata").child("data").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
       // Utils.setUpToolbarBackButton(Notification.this, toolbar);
        LinearLayoutManager lm=new LinearLayoutManager(getActivity());
        notificationrecycle.setLayoutManager(lm);
        mDatanotiflike.keepSynced(true);
        mDatabasenotifdata.keepSynced(true);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Notificationmodel, NotificationViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Notificationmodel, NotificationViewHolder>(
                Notificationmodel.class,
                R.layout.activity_notificationitem,
                NotificationViewHolder.class,
                mDatabasenotifdata
        ) {
            @Override
            protected void populateViewHolder(NotificationViewHolder viewHolder, final Notificationmodel model, int position) {
                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //  mDatanotiflike.child("count").setValue(0);
                    }
                });
                viewHolder.iview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String key = model.getKey();
                        if (!key.equals(auth.getCurrentUser().getUid())) {
                            //   Toast.makeText(getActivity(), key, Toast.LENGTH_LONG).show();
                            Intent i = new Intent(getActivity(), Chatstart.class);
                            i.putExtra("chat", key);
                            startActivity(i);
                            getActivity().finish();
                        } else {
                            Toast.makeText(getActivity(), "You can't chat with yourself", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                viewHolder.tname.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                     //   android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                        post_key = model.getPost_key();
                        //  fragmentManager.beginTransaction().add(R.id.content_frame,new Notifclickfrag(),tag).commit();
                        //   Notifclickfrag notifclickfrag=new Notifclickfrag();
                        Intent i = new Intent(getActivity(), Notifclick.class);
                        i.putExtra("postkey", post_key);
                        startActivity(i);
                    }
                });
                viewHolder.bindData(model);

            }
        };
        notificationrecycle.setAdapter(firebaseRecyclerAdapter);
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder{
        View view;
        ImageView iview;
        TextView tname;
        private DatabaseReference mDatabasenotifdata,mDatanotiflike;
        public NotificationViewHolder(View itemView) {
            super(itemView);
            view=itemView;
            tname=(TextView)view.findViewById(R.id.bname);
            iview=(ImageView)view.findViewById(R.id.pimage);
            mDatanotiflike= FirebaseDatabase.getInstance().getReference().child("notificationdata").child("like").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        }
        public void bindData(Notificationmodel model){
            // TextView tname=(TextView)view.findViewById(R.id.bname);
            // ImageView iview=(ImageView)view.findViewById(R.id.pimage);
            tname.setText(model.getTxt());
            if(model.getImg()!=null) {
                iview.setImageBitmap(Utils.decodeBase64(model.getImg()));
            }
            else{
                iview.setImageResource(R.drawable.account);
            }
            // mDatanotiflike.child("count").setValue(0);
        }
    }
    public void refresh(){

        FirebaseRecyclerAdapter<Notificationmodel, NotificationViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Notificationmodel, NotificationViewHolder>(
                Notificationmodel.class,
                R.layout.activity_notificationitem,
                NotificationViewHolder.class,
                mDatabasenotifdata
        ) {
            @Override
            protected void populateViewHolder(NotificationViewHolder viewHolder, final Notificationmodel model, int position) {
                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //  mDatanotiflike.child("count").setValue(0);
                    }
                });
                viewHolder.iview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String key = model.getKey();
                        if (!key.equals(auth.getCurrentUser().getUid())) {
                            //   Toast.makeText(getActivity(), key, Toast.LENGTH_LONG).show();
                            Intent i = new Intent(getActivity(), Chatstart.class);
                            i.putExtra("chat", key);
                            startActivity(i);
                            getActivity().finish();
                        } else {
                            Toast.makeText(getActivity(), "You can't chat with yourself", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                viewHolder.tname.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                     //   android.support.v4.app.FragmentManager fragmentManager = getChildFragmentManager();
                        post_key = model.getPost_key();
                        //  fragmentManager.beginTransaction().add(R.id.content_frame,new Notifclickfrag(),tag).commit();
                        //   Notifclickfrag notifclickfrag=new Notifclickfrag();
                        Intent i = new Intent(getActivity(), Notifclick.class);
                        i.putExtra("postkey", post_key);
                        startActivity(i);
                    }
                });
                viewHolder.bindData(model);

            }
        };
        notificationrecycle.setAdapter(firebaseRecyclerAdapter);
        refreshcomplete();
    }
    public void refreshcomplete(){
        refresh.setRefreshing(false);
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager)getActivity(). getSystemService(
                Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
