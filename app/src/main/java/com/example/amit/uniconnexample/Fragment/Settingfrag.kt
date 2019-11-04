package com.example.amit.uniconnexample.Fragment

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.amit.uniconnexample.App
import com.example.amit.uniconnexample.Activity.NewTabActivity
import com.example.amit.uniconnexample.R
import com.github.zagum.switchicon.SwitchIconView

import android.content.Context.MODE_PRIVATE

/**
 * Created by amit on 18/2/17.
 */

class Settingfrag : Fragment() {
    internal var flag: Boolean? = null
    internal var vib: Boolean? = null
    private var button1: View? = null
    private var button2: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_setting_frag, container, false)
        button1 = view.findViewById(R.id.notifsound) as View
        button2 = view.findViewById(R.id.notifvibrate)
        switchIconView1 = view.findViewById<View>(R.id.switchsound) as SwitchIconView
        switchIconView2 = view.findViewById<View>(R.id.switchvibrate) as SwitchIconView
        //  tablayoutbottom=(TabLayout)findViewById(R.id.tabLayoutbottom);
        //toolbar=(Toolbar)findViewById(R.id.toolbar);
        val myPrefs = activity!!.getSharedPreferences("com.example.amit.uniconnexample", MODE_PRIVATE)
        // SharedPreferences myPrefs2=getSharedPreferences("com.example.amit.uniconnexample",MODE_PRIVATE);
        switchIconView1!!.isIconEnabled = myPrefs.getBoolean("isChecked1", true)
        switchIconView2!!.isIconEnabled = myPrefs.getBoolean("isChecked2", true)
        //Utils.setUpToolbarBackButton(Settings.this, toolbar);
        //  setupTabIconsBottom();
        button1!!.setOnClickListener {
            switchIconView1!!.switchState()
            flag = switchIconView1!!.isIconEnabled
            isEnabledswitch(flag)
            val editor1 = activity!!.getSharedPreferences("com.example.amit.uniconnexample", MODE_PRIVATE).edit()
            editor1.putBoolean("isChecked1", switchIconView1!!.isIconEnabled)
            editor1.commit()
        }
        button2!!.setOnClickListener {
            switchIconView2!!.switchState()
            vib = switchIconView2!!.isIconEnabled
            isEnabledvibrate(vib)
            val editor2 = activity!!.getSharedPreferences("com.example.amit.uniconnexample", MODE_PRIVATE).edit()
            editor2.putBoolean("isChecked2", switchIconView2!!.isIconEnabled)
            editor2.commit()
        }
        flag = switchIconView1!!.isIconEnabled
        vib = switchIconView2!!.isIconEnabled
        (activity!!.application as App).setFlag(flag)
        (activity!!.application as App).setVib(vib)
        // isEnabledswitch();
        // isEnabledvibrate();
        // setupTabIcons();
        // bindWidgetsWithAnEvent();
        return view
    }

    override fun onResume() {
        super.onResume()
        (activity as NewTabActivity).setTitle("    Setting        ")
    }

    fun isEnabledswitch(flag: Boolean?) {
        //  switchIconView1=(SwitchIconView)findViewById(R.id.switchsound);
        (activity!!.application as App).setFlag(flag)
    }

    fun isEnabledvibrate(flag: Boolean?) {
        // switchIconView2=(SwitchIconView)findViewById(R.id.switchvibrate);
        (activity!!.application as App).setVib(flag)

    }

    companion object {
        private var switchIconView1: SwitchIconView? = null
        private var switchIconView2: SwitchIconView? = null
    }
}
