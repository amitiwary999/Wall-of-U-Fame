package com.example.amit.uniconnexample;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amit on 27/11/16.
 */

public class Tabs extends AppCompatActivity {
    private TabLayout tabLayout,tablayoutbottom;
    private ViewPager viewPager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);
        tabLayout=(TabLayout)findViewById(R.id.tabLayout);
        tablayoutbottom=(TabLayout)findViewById(R.id.tabLayoutbottom);
        viewPager=(ViewPager)findViewById(R.id.viewPager);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIconsBottom();
        setupTabIcons();
        bindWidgetsWithAnEvent();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new MainActivity(), "My Campus");
        adapter.addFrag(new Global(), "Global");
        adapter.addFrag(new Trending(), "Trending");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void setupTabIcons() {
        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.tabtext, null);
        tabOne.setText("My Campus");
      //  tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.myaccount, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.tabtext, null);
        tabTwo.setText("Global");
       // tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.notifications, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.tabtext, null);
        tabThree.setText("Trending");
        //tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.message, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);

        //  bottomtab();
    }

    private void setupTabIconsBottom() {
        tablayoutbottom.addTab(tablayoutbottom.newTab().setIcon(R.drawable.home),true);
        tablayoutbottom.addTab(tablayoutbottom.newTab().setIcon(R.drawable.myaccount));
        tablayoutbottom.addTab(tablayoutbottom.newTab().setIcon(R.drawable.notifications));
        tablayoutbottom.addTab(tablayoutbottom.newTab().setIcon(R.drawable.message));
        tablayoutbottom.addTab(tablayoutbottom.newTab().setIcon(R.drawable.settings));
       /* TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.tabtext, null);
        tabOne.setText("My Profile");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.myaccount, 0, 0);
        tablayoutbottom.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.tabtext, null);
        tabTwo.setText("Notification");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.notifications, 0, 0);
        tablayoutbottom.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.tabtext, null);
        tabThree.setText("Blackboard");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.message, 0, 0);
        tablayoutbottom.getTabAt(2).setCustomView(tabThree);

        TextView tabFour = (TextView) LayoutInflater.from(this).inflate(R.layout.tabtext, null);
        tabFour.setText("Settings");
        tabFour.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.settings, 0, 0);
        tablayoutbottom.getTabAt(3).setCustomView(tabFour);*/
        //  bottomtab();
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
               // startActivity(new Intent(Tabs.this,Tabs.class));
                break;
            case 1 :
                startActivity(new Intent(Tabs.this,Profile.class));
                //finish();
                //replaceFragment(new Profile());
                break;
            case 2 :
                startActivity(new Intent(Tabs.this,Notification.class));
                //finish();
               // replaceFragment(new Message());
                break;
            case 3:
                startActivity(new Intent(Tabs.this,Message.class));
              //  finish();
                //replaceFragment(new Notification());
                break;
            case 4:
                startActivity(new Intent(Tabs.this,Settings.class));
               // finish();
               // replaceFragment(new Settings());
                break;
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if(item.getItemId()==R.id.signout){
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(Tabs.this, "Logging out..", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Tabs.this, Loginactivity.class));
            finish();
          /*  AlertDialog.Builder d = new AlertDialog.Builder(this);
            d.setMessage("Are you sure ?").
                    setCancelable(false).
                    setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseAuth.getInstance().signOut();
                            Toast.makeText(Tabs.this, "Logging out..", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Tabs.this, Loginactivity.class));
                            finish();

                        }
                    }).
                    setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert = d.create();
            alert.setTitle("Logout");
            alert.show();
            Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
            nbutton.setTextColor(Color.BLACK);
            Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
            pbutton.setTextColor(Color.BLACK);*/
        }
        return super.onOptionsItemSelected(item);
    }

}
