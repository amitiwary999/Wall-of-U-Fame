package com.example.amit.uniconnexample.Activity

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders

import com.example.amit.uniconnexample.App
import com.example.amit.uniconnexample.Fragment.Home.HomeFragment
import com.example.amit.uniconnexample.Fragment.Login.LoginFragment
import com.example.amit.uniconnexample.R
import com.example.amit.uniconnexample.Fragment.Profilefrag
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

import kotlinx.android.synthetic.main.activity_new_tab.*
import kotlinx.android.synthetic.main.design_toolbar.*

/**
 * Created by amit on 19/2/17.
 */

class NewTabActivity : AppCompatActivity() {
    lateinit var editor1: SharedPreferences.Editor
    internal var handler1 = Handler()
    internal var user: FirebaseUser? = null
    internal var switchflag: Boolean? = null
    internal var switchvibrate: Boolean? = null
    internal var doubleBackToExitPressedOnce = false
    internal var m = 0
    internal var m1 = 500
    internal var count: Int = 0
    internal var flag = 0
    internal var msgcount = 0
    private val isNetworkConnected: Boolean
        get() {
            val cm = getSystemService(
                    Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return cm.activeNetworkInfo != null
        }
    var mainViewModel: MainViewModel ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_tab)
        val firebaseAuth = FirebaseAuth.getInstance().currentUser
        user = FirebaseAuth.getInstance().currentUser
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        setSupportActionBar(toolbar)
        if(firebaseAuth == null){
            login_btn.visibility = View.VISIBLE
            logout_btn.visibility = View.GONE
            login_btn.setOnClickListener { LoginFragment().show(supportFragmentManager, "Login") }
        }else{
            logout_btn.visibility = View.VISIBLE
            login_btn.visibility = View.GONE
            logout_btn.setOnClickListener { v -> signout(v) }
        }
        bottomtab.setOnTabSelectListener { tabId ->
            if (tabId == R.id.tab_home) {
                toolbar_title.text = "    Home        "
                attachFragment(HomeFragment())
                //  viewPager.setVisibility(View.VISIBLE);
            } else if (tabId == R.id.tab_account) {
                //  viewPager.setVisibility(View.GONE);
                if(firebaseAuth == null){
                    LoginFragment().show(supportFragmentManager, "Login")
                }else{
                    toolbar_title.text = "    Profile     "
                    attachFragment(Profilefrag())
                }

            }
//            else if (tabId == R.id.tab_notification) {
//                if (isNetworkConnected) {
//                    flag = 0
//                }
//                //    mDatanotiflike.setValue(new Likemodel(0));
//                //  viewPager.setVisibility(View.GONE);
//                toolbar_title.text = "    Notification"
//                attachFragment(Notifrag())
//            } else if (tabId == R.id.tab_message) {
//                if (isNetworkConnected) {
//                    //bottomBarTabmsg.removeBadge();
//                    msgcount = 0
//                }
//                //  viewPager.setVisibility(View.GONE);
//                toolbar_title.text = "    Message     "
//                attachFragment(Msgfrag())
//            } else if (tabId == R.id.tab_setting) {
//                //  viewPager.setVisibility(View.GONE);
//                toolbar_title.text = "    Setting     "
//                attachFragment(Settingfrag())
//            }
        }
        //  bottomBarTab.se
    }

    override fun onStart() {
        super.onStart()
        //startService(new Intent(this,Notificationservice.class));
        switchflag = (this.application as App).getFlag()
        switchvibrate = (this.application as App).getVib()
    }

    fun attachFragment(fm: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.content_frame, fm)
        try {
            fragmentTransaction.commitAllowingStateLoss()
        } catch (e: IllegalStateException) {
            fragmentTransaction.commit()
        }

    }

    fun signout(v: View) {
        val d = AlertDialog.Builder(this@NewTabActivity)
        d.setMessage("Are you sure ?").setCancelable(false).setPositiveButton("Yes") { dialog, which ->
            if (isNetworkConnected) {
                // stopService(new Intent(NewTabActivity.this, Notificationservice.class));
                handler1.postDelayed({
                    FirebaseAuth.getInstance().signOut()
                    Toast.makeText(this@NewTabActivity, "Logging out..", Toast.LENGTH_SHORT).show()
                    mainViewModel?.getPagedPost()
                }, 2000)

            } else {
                Toast.makeText(this@NewTabActivity, "No internet connection", Toast.LENGTH_LONG).show()
            }
        }.setNegativeButton("No") { dialog, which -> dialog.cancel() }

        val alert = d.create()
        alert.setTitle("Logout")
        alert.show()
        val nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE)
        nbutton.setTextColor(Color.BLACK)
        val pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE)
        pbutton.setTextColor(Color.BLACK)
    }

    fun setTitle(tooltitle: String) {
        toolbar_title.text = tooltitle
    }
}
