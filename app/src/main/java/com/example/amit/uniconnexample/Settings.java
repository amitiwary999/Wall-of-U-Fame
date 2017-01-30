package com.example.amit.uniconnexample;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.amit.uniconnexample.utils.Utils;
import com.github.zagum.switchicon.SwitchIconView;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

/**
 * Created by amit on 28/11/16.
 */

public class Settings extends AppCompatActivity {
    private TabLayout tablayoutbottom;
    private Toolbar toolbar;
    Boolean flag,vib;
    private static SwitchIconView switchIconView1,switchIconView2;
    private View button1,button2;
    public static Settings settings;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        settings=this;
        button1=findViewById(R.id.notifsound);
        button2=findViewById(R.id.notifvibrate);
        switchIconView1=(SwitchIconView)findViewById(R.id.switchsound);
        switchIconView2=(SwitchIconView)findViewById(R.id.switchvibrate);
        tablayoutbottom=(TabLayout)findViewById(R.id.tabLayoutbottom);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        SharedPreferences myPrefs=getSharedPreferences("com.example.amit.uniconnexample",MODE_PRIVATE);
       // SharedPreferences myPrefs2=getSharedPreferences("com.example.amit.uniconnexample",MODE_PRIVATE);
        switchIconView1.setIconEnabled(myPrefs.getBoolean("isChecked1",true));
        switchIconView2.setIconEnabled(myPrefs.getBoolean("isChecked2",true));
        Utils.setUpToolbarBackButton(Settings.this, toolbar);
        setupTabIconsBottom();
         button1.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 switchIconView1.switchState();
                 flag=switchIconView1.isIconEnabled();
                 isEnabledswitch(flag);
                 SharedPreferences.Editor editor1=getSharedPreferences("com.example.amit.uniconnexample",MODE_PRIVATE).edit();
                 editor1.putBoolean("isChecked1",switchIconView1.isIconEnabled());
                 editor1.commit();
             }
         });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchIconView2.switchState();
                vib=switchIconView2.isIconEnabled();
                isEnabledvibrate(vib);
                SharedPreferences.Editor editor2=getSharedPreferences("com.example.amit.uniconnexample",MODE_PRIVATE).edit();
                editor2.putBoolean("isChecked2",switchIconView2.isIconEnabled());
                editor2.commit();
            }
        });
        flag=switchIconView1.isIconEnabled();
        vib=switchIconView2.isIconEnabled();
        ((App)this.getApplication()).setFlag(flag);
        ((App)this.getApplication()).setVib(vib);
       // isEnabledswitch();
       // isEnabledvibrate();
        // setupTabIcons();
        bindWidgetsWithAnEvent();
    }

    public static Settings getInstance(){
        return settings;
    }
   public  void isEnabledswitch(Boolean flag){
     //  switchIconView1=(SwitchIconView)findViewById(R.id.switchsound);
       ((App)this.getApplication()).setFlag(flag);
   }
    public  void isEnabledvibrate(Boolean flag){
       // switchIconView2=(SwitchIconView)findViewById(R.id.switchvibrate);
        ((App)this.getApplication()).setVib(flag);

    }
    private void setupTabIconsBottom() {
        tablayoutbottom.addTab(tablayoutbottom.newTab().setIcon(R.drawable.home));
        tablayoutbottom.addTab(tablayoutbottom.newTab().setIcon(R.drawable.myaccount));
        tablayoutbottom.addTab(tablayoutbottom.newTab().setIcon(R.drawable.notifications));
        tablayoutbottom.addTab(tablayoutbottom.newTab().setIcon(R.drawable.message));
     //   tablayoutbottom.addTab(tablayoutbottom.newTab().setIcon(R.drawable.chati));
        tablayoutbottom.addTab(tablayoutbottom.newTab().setIcon(R.drawable.settings),true);
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
                Intent intent=new Intent(Settings.this, Tabs.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            case 1 :
                startActivity(new Intent(Settings.this,Profile.class));
                finish();
                //replaceFragment(new Profile());
                break;
            case 2 :
                  startActivity(new Intent(Settings.this,Notification.class));
                finish();
                // replaceFragment(new Message());
                break;
            case 3:
                startActivity(new Intent(Settings.this,Message.class));
                finish();
                //replaceFragment(new Notification());
                break;
          /*  case 4:
                startActivity(new Intent(Settings.this,Chat.class));
                finish();
                break;*/
            case 4:
              //  startActivity(new Intent(Notification.this,Settings.class));
                // replaceFragment(new Settings());
                break;
        }
    }
}
