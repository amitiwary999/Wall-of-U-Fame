package com.example.amit.uniconnexample.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.amit.uniconnexample.App;
import com.example.amit.uniconnexample.Activity.NewTabActivity;
import com.example.amit.uniconnexample.R;
import com.example.amit.uniconnexample.utils.Utils;
import com.github.zagum.switchicon.SwitchIconView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by amit on 18/2/17.
 */

public class Settingfrag extends Fragment {
    Boolean flag,vib;
    private static SwitchIconView switchIconView1,switchIconView2;
    private View button1,button2;
    public Settingfrag() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_setting_frag,container,false);
        button1=(View)view.findViewById(R.id.notifsound);
        button2=view.findViewById(R.id.notifvibrate);
        switchIconView1=(SwitchIconView)view.findViewById(R.id.switchsound);
        switchIconView2=(SwitchIconView)view.findViewById(R.id.switchvibrate);
      //  tablayoutbottom=(TabLayout)findViewById(R.id.tabLayoutbottom);
        //toolbar=(Toolbar)findViewById(R.id.toolbar);
        SharedPreferences myPrefs=getActivity().getSharedPreferences("com.example.amit.uniconnexample",MODE_PRIVATE);
        // SharedPreferences myPrefs2=getSharedPreferences("com.example.amit.uniconnexample",MODE_PRIVATE);
        switchIconView1.setIconEnabled(myPrefs.getBoolean("isChecked1",true));
        switchIconView2.setIconEnabled(myPrefs.getBoolean("isChecked2",true));
        //Utils.setUpToolbarBackButton(Settings.this, toolbar);
      //  setupTabIconsBottom();
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchIconView1.switchState();
                flag=switchIconView1.isIconEnabled();
                isEnabledswitch(flag);
                SharedPreferences.Editor editor1=getActivity().getSharedPreferences("com.example.amit.uniconnexample",MODE_PRIVATE).edit();
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
                SharedPreferences.Editor editor2=getActivity().getSharedPreferences("com.example.amit.uniconnexample",MODE_PRIVATE).edit();
                editor2.putBoolean("isChecked2",switchIconView2.isIconEnabled());
                editor2.commit();
            }
        });
        flag=switchIconView1.isIconEnabled();
        vib=switchIconView2.isIconEnabled();
        ((App)getActivity().getApplication()).setFlag(flag);
        ((App)getActivity().getApplication()).setVib(vib);
        // isEnabledswitch();
        // isEnabledvibrate();
        // setupTabIcons();
       // bindWidgetsWithAnEvent();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((NewTabActivity)getActivity()).setTitle("    Setting        ");
    }

    public  void isEnabledswitch(Boolean flag){
        //  switchIconView1=(SwitchIconView)findViewById(R.id.switchsound);
        ((App)getActivity().getApplication()).setFlag(flag);
    }
    public  void isEnabledvibrate(Boolean flag){
        // switchIconView2=(SwitchIconView)findViewById(R.id.switchvibrate);
        ((App)getActivity().getApplication()).setVib(flag);

    }
}
