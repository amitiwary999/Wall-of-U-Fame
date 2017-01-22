package com.example.amit.uniconnexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
    private DatabaseReference mDatabasenotifdata,mDatanotiflike;
    RecyclerView notificationrecycle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        notificationrecycle=(RecyclerView) findViewById(R.id.mnotification_list);
        tablayoutbottom=(TabLayout)findViewById(R.id.tabLayoutbottom);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        mDatanotiflike= FirebaseDatabase.getInstance().getReference().child("notificationdata").child("like").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mDatanotiflike.child("count").setValue(0);
        mDatabasenotifdata= FirebaseDatabase.getInstance().getReference().child("notificationdata").child("data").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        Utils.setUpToolbarBackButton(Notification.this, toolbar);
        LinearLayoutManager lm=new LinearLayoutManager(this);
        notificationrecycle.setLayoutManager(lm);
        setupTabIconsBottom();
        
        // setupTabIcons();
        bindWidgetsWithAnEvent();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Notificationmodel,NotificationViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Notificationmodel, NotificationViewHolder>(
                Notificationmodel.class,
                R.layout.activity_notificationitem,
                NotificationViewHolder.class,
                mDatabasenotifdata
        ) {
            @Override
            protected void populateViewHolder(NotificationViewHolder viewHolder, Notificationmodel model, int position) {
                viewHolder.bindData(model);
            }
        };
        notificationrecycle.setAdapter(firebaseRecyclerAdapter);
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
        public NotificationViewHolder(View itemView) {
            super(itemView);
            view=itemView;
        }
        public void bindData(Notificationmodel model){
            TextView tname=(TextView)view.findViewById(R.id.bname);
            ImageView iview=(ImageView)view.findViewById(R.id.pimage);
            tname.setText(model.getTxt());
            if(model.getImg()!=null)
                iview.setImageBitmap(Utils.decodeBase64(model.getImg()));
            else{
                iview.setImageResource(R.drawable.account);
            }
        }
    }

    private void setCurrentTabFragment(int tabPosition)
    {
        switch (tabPosition)
        {
            case 0:
                startActivity(new Intent(Notification.this,Tabs.class));
                finish();
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
            case 5:
                startActivity(new Intent(Notification.this,Settings.class));
                finish();
                // replaceFragment(new Settings());
                break;
        }
    }
}
