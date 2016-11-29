package com.example.amit.uniconnexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.amit.uniconnexample.utils.Utils;

/**
 * Created by amit on 28/11/16.
 */

public class Message extends AppCompatActivity {
    private TabLayout tablayoutbottom;
    private Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
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
            case 4:
                startActivity(new Intent(Message.this,Settings.class));
                finish();
                // replaceFragment(new Settings());
                break;
        }
    }
}
