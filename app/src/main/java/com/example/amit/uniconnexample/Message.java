package com.example.amit.uniconnexample;

import android.content.Intent;
import android.os.Bundle;
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

import com.example.amit.uniconnexample.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/**
 * Created by amit on 28/11/16.
 */

public class Message extends AppCompatActivity {
    private TabLayout tablayoutbottom;
    private Toolbar toolbar;
    RecyclerView rview;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager laymanager;
    private static ArrayList<Message_model> data;
    private DatabaseReference mDatabase;
    private FirebaseAuth auth;
    FirebaseUser user;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        rview=(RecyclerView)findViewById(R.id.mchat_list);
        rview.setLayoutManager(new LinearLayoutManager(this));

        data=new ArrayList<Message_model>();
        adapter=new Messageadapter(data);
        rview.setAdapter(adapter);
        tablayoutbottom=(TabLayout)findViewById(R.id.tabLayoutbottom);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        Utils.setUpToolbarBackButton(Message.this, toolbar);
        setupTabIconsBottom();
       // setupTabIcons();
        bindWidgetsWithAnEvent();
    }

    private void setupTabIconsBottom() {
        tablayoutbottom.addTab(tablayoutbottom.newTab().setIcon(R.drawable.home));
        tablayoutbottom.addTab(tablayoutbottom.newTab().setIcon(R.drawable.myaccount));
        tablayoutbottom.addTab(tablayoutbottom.newTab().setIcon(R.drawable.notifications));
        tablayoutbottom.addTab(tablayoutbottom.newTab().setIcon(R.drawable.message),true);
     //   tablayoutbottom.addTab(tablayoutbottom.newTab().setIcon(R.drawable.chati));
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

    private void setCurrentTabFragment(int tabPosition)
    {
        switch (tabPosition)
        {
            case 0:
                startActivity(new Intent(Message.this,Tabs.class));
                finish();
                break;
            case 1 :
                startActivity(new Intent(Message.this,Profile.class));
                finish();
                //replaceFragment(new Profile());
                break;
            case 2 :
                startActivity(new Intent(Message.this,Notification.class));
                finish();
                // replaceFragment(new Message());
                break;
            case 3:
               // startActivity(new Intent(Message.this,Message.class));
                //replaceFragment(new Notification());
                break;
           /* case 4:
                startActivity(new Intent(Message.this,Chat.class));
                finish();
                break;*/
            case 5:
                startActivity(new Intent(Message.this,Settings.class));
                finish();
                // replaceFragment(new Settings());
                break;
        }
    }
}
