package com.example.amit.uniconnexample;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amit.uniconnexample.utils.Utils;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

/**
 * Created by amit on 28/11/16.
 */

public class Notification extends AppCompatActivity {
    private TabLayout tablayoutbottom;
    private Toolbar toolbar;
    FirebaseAuth auth;
    String key,post_key;
    String tag;
    private DatabaseReference mDatabasenotifdata,mDatanotiflike;
    RecyclerView notificationrecycle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        tag=this.getClass().getSimpleName();
        notificationrecycle=(RecyclerView) findViewById(R.id.mnotification_list);
        tablayoutbottom=(TabLayout)findViewById(R.id.tabLayoutbottom);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
       // FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        auth=FirebaseAuth.getInstance();
        mDatanotiflike= FirebaseDatabase.getInstance().getReference().child("notificationdata").child("like").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
      //  Toast.makeText(this,"check",Toast.LENGTH_LONG).show();

        mDatabasenotifdata= FirebaseDatabase.getInstance().getReference().child("notificationdata").child("data").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        Utils.setUpToolbarBackButton(Notification.this, toolbar);
        LinearLayoutManager lm=new LinearLayoutManager(this);
        notificationrecycle.setLayoutManager(lm);
        mDatanotiflike.keepSynced(true);
        mDatabasenotifdata.keepSynced(true);

        setupTabIconsBottom();

        // setupTabIcons();
        bindWidgetsWithAnEvent();

    }

    @Override
    protected void onStart() {
        super.onStart();
      //  if(isNetworkConnected()) {
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
                                Intent i = new Intent(Notification.this, Chatstart.class);
                                i.putExtra("chat", key);
                                startActivity(i);
                                finish();
                            } else {
                                Toast.makeText(Notification.this, "You can't chat with yourself", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    viewHolder.tname.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                            post_key = model.getPost_key();
                            //  fragmentManager.beginTransaction().add(R.id.content_frame,new Notifclickfrag(),tag).commit();
                            //   Notifclickfrag notifclickfrag=new Notifclickfrag();
                            Intent i = new Intent(Notification.this, Notifclick.class);
                            i.putExtra("postkey", post_key);
                            startActivity(i);
                        }
                    });
                    viewHolder.bindData(model);

                }
            };
            notificationrecycle.setAdapter(firebaseRecyclerAdapter);
     //   }else{
       //     Toast.makeText(Notification.this, "No Internet connection", Toast.LENGTH_LONG).show();
        //}
    }

    private void setupTabIconsBottom() {
        tablayoutbottom.addTab(tablayoutbottom.newTab().setIcon(R.drawable.home));
        tablayoutbottom.addTab(tablayoutbottom.newTab().setIcon(R.drawable.myaccount));
        tablayoutbottom.addTab(tablayoutbottom.newTab().setIcon(R.drawable.notifications), true);
        tablayoutbottom.addTab(tablayoutbottom.newTab().setIcon(R.drawable.message));
       // tablayoutbottom.addTab(tablayoutbottom.newTab().setIcon(R.drawable.chati));
        tablayoutbottom.addTab(tablayoutbottom.newTab().setIcon(R.drawable.settings));
    }

    private void bindWidgetsWithAnEvent()
    {
        tablayoutbottom.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setCurrentTabFragment(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                setCurrentTabFragment(tab.getPosition());
            }
        });

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

    private void setCurrentTabFragment(int tabPosition)
    {
        switch (tabPosition)
        {
            case 0:
                Intent intent=new Intent(Notification.this, Tabs.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            case 1 :
                startActivity(new Intent(Notification.this,Profile.class));
                finish();
                //replaceFragment(new Profile());
                break;
            case 2 :
              //  startActivity(new Intent(Notification.this,Notification.class));
                // replaceFragment(new Message());
                break;
            case 3:
                 startActivity(new Intent(Notification.this,Message.class));
                finish();
                //replaceFragment(new Notification());
                break;
          /*  case 4:
                startActivity(new Intent(Notification.this,Chat.class));
                finish();
                break;*/
            case 4:
                startActivity(new Intent(Notification.this,Settings.class));
                finish();
                // replaceFragment(new Settings());
                break;
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(
                Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
