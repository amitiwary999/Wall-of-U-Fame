package com.example.amit.uniconnexample.Activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.telephony.TelephonyManager
import android.telephony.gsm.GsmCellLocation
import androidx.annotation.IdRes
import com.google.android.material.tabs.TabLayout
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import com.example.amit.uniconnexample.App
import com.example.amit.uniconnexample.Fragment.Home.HomeFragment
import com.example.amit.uniconnexample.R
import com.example.amit.uniconnexample.Fragment.Mainfrag
import com.example.amit.uniconnexample.Fragment.Msgfrag
import com.example.amit.uniconnexample.Fragment.Notifrag
import com.example.amit.uniconnexample.Fragment.Profilefrag
import com.example.amit.uniconnexample.Fragment.Settingfrag
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResult
import com.google.android.gms.location.LocationSettingsStates
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.roughike.bottombar.BottomBar
import com.roughike.bottombar.BottomBarTab
import com.roughike.bottombar.OnTabSelectListener

import java.io.IOException
import java.util.Locale

import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_new_tab.*
import kotlinx.android.synthetic.main.design_toolbar.*

/**
 * Created by amit on 19/2/17.
 */

class NewTabActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    lateinit var editor1: SharedPreferences.Editor
    internal var handler1 = Handler()
    internal var handler2 = Handler()
    private var mDatabasenotif: DatabaseReference? = null
    private var mDatanotiflike: DatabaseReference? = null
    private var newnotifchat: DatabaseReference? = null
    private var mDatabasenotiflike: DatabaseReference? = null
    internal var user: FirebaseUser? = null
    var valueEventListener: ValueEventListener ?= null
    var valueventlistener: ValueEventListener ?= null
    internal var switchflag: Boolean? = null
    internal var switchvibrate: Boolean? = null
    internal var location: Location? = null
    internal var mLastLocation: Location? = null
    internal var cityname: String ?= null
    internal var geocoder: Geocoder ?= null
    internal var addresses: List<Address> ?= null
    internal var latitude: Double = 0.toDouble()
    internal var longitude: Double = 0.toDouble()
    internal var mGoogleApiClient: GoogleApiClient? = null
    internal var result: PendingResult<LocationSettingsResult>? = null
    private var mLocationRequest: LocationRequest? = null
    private val mRequestingLocationUpdates = false
    internal var builder: LocationSettingsRequest.Builder ?= null
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_tab)
        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient()
            createLocationRequest()
        }
        builder = LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this@NewTabActivity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this@NewTabActivity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this@NewTabActivity,
                        arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), 12345)
                return
            }
        }
        geocoder = Geocoder(this, Locale.getDefault())
        user = FirebaseAuth.getInstance().currentUser
        editor1 = getSharedPreferences("com.example.amit.uniconnexample", Context.MODE_PRIVATE).edit()
        mDatabasenotiflike = FirebaseDatabase.getInstance().reference.child("notificationdata").child("like")
        newnotifchat = FirebaseDatabase.getInstance().reference.child("notificationdata").child("chat").child(user!!.uid)
        mDatabasenotif = FirebaseDatabase.getInstance().reference.child("notification").child("like").child(user!!.uid)
        mDatanotiflike = FirebaseDatabase.getInstance().reference.child("notificationdata").child("like").child(user!!.uid)
        newnotifchat!!.keepSynced(true)
        mDatabasenotif!!.keepSynced(true)
        mDatanotiflike!!.keepSynced(true)
        mDatabasenotiflike!!.keepSynced(true)
        //  tabLayout.setupWithViewPager(viewPager);
        //  setupViewPager(viewPager);
        setSupportActionBar(toolbar)
        //  setupTabIcons();
//        FirebaseAuth.getInstance().currentUser?.getToken(false)?.addOnCompleteListener {
//            if(it.isSuccessful && it.result != null){
//                Log.d("MainActivity ","token ${it.result?.token}")
//            }
//        }
        img.setOnClickListener { v -> signout(v) }
        bottomtab.setOnTabSelectListener { tabId ->
            if (tabId == R.id.tab_home) {
                toolbar_title.text = "    Home        "
                attachFragment(HomeFragment())
                //  viewPager.setVisibility(View.VISIBLE);
            } else if (tabId == R.id.tab_account) {
                //  viewPager.setVisibility(View.GONE);
                toolbar_title.text = "    Profile     "
                attachFragment(Profilefrag())
            } else if (tabId == R.id.tab_notification) {
                if (isNetworkConnected) {
                    newnotifchat!!.removeEventListener(valueEventListener)
                    mDatabasenotif!!.removeEventListener(valueventlistener)
                    flag = 0
                }
                //    mDatanotiflike.setValue(new Likemodel(0));
                //  viewPager.setVisibility(View.GONE);
                toolbar_title.text = "    Notification"
                attachFragment(Notifrag())
            } else if (tabId == R.id.tab_message) {
                if (isNetworkConnected) {
                    newnotifchat!!.removeEventListener(valueEventListener)
                    mDatabasenotif!!.removeEventListener(valueventlistener)
                    //bottomBarTabmsg.removeBadge();
                    msgcount = 0
                }
                //  viewPager.setVisibility(View.GONE);
                toolbar_title.text = "    Message     "
                attachFragment(Msgfrag())
            } else if (tabId == R.id.tab_setting) {
                //  viewPager.setVisibility(View.GONE);
                toolbar_title.text = "    Setting     "
                attachFragment(Settingfrag())
            }
        }
        //  bottomBarTab.se
    }

    override fun onPause() {
        super.onPause()
        if (mGoogleApiClient!!.isConnected) {
            stopLocationUpdates()
        }
        //Toast.makeText(Tabs.this, "checkpause", Toast.LENGTH_SHORT).show();

    }

    override fun onStart() {
        super.onStart()
        if (mGoogleApiClient != null) {
            mGoogleApiClient!!.connect()
        }
        val result1: PendingResult<LocationSettingsResult>
        result1 = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                builder?.build())
        settingLocation(result1)
        //Log.e("Blog","Hi"+cityname);
        if (isNetworkConnected) {
            // stopService(new Intent(NewTabActivity.this, Notificationservice.class));
        }
        //startService(new Intent(this,Notificationservice.class));
        switchflag = (this.application as App).getFlag()
        switchvibrate = (this.application as App).getVib()

        if (isNetworkConnected) {
            valueEventListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.children) {
                        //  Toast.makeText(Tabs.this,"Tabs",Toast.LENGTH_LONG).show();
                        //notifiy(++m1, snapshot.getRef(), snapshot, switchflag, switchvibrate);
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            }
            if (newnotifchat != null) {
                newnotifchat!!.addValueEventListener(valueEventListener)
            }

            valueventlistener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.children) {
                        // handler1.postDelayed(new Runnable() {
                        //   @Override
                        //   public void run() {
                        snapshot.ref.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                for (snapshot in dataSnapshot.children) {

                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {

                            }
                        })

                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            }
            if (mDatabasenotif != null) {
                mDatabasenotif!!.addValueEventListener(valueventlistener)
            }

        } else {
            Toast.makeText(this@NewTabActivity, "No internet connection", Toast.LENGTH_LONG).show()
        }
    }

    override fun onStop() {
        super.onStop()
        mGoogleApiClient!!.disconnect()
        // Toast.makeText(Tabs.this, "checkstop", Toast.LENGTH_SHORT).show();
        if (isNetworkConnected) {
            newnotifchat?.removeEventListener(valueEventListener)
            mDatabasenotif?.removeEventListener(valueventlistener)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //    Toast.makeText(Tabs.this, "checkstop", Toast.LENGTH_SHORT).show();
        if (isNetworkConnected) {
            //startService(new Intent(NewTabActivity.this, Notificationservice.class));
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOCATION_SETTING_REQUEST) {
            Log.e("Blog", "Location")
            when (resultCode) {
                Activity.RESULT_OK -> {
                    Log.e("Blog", "Ok")
                    val handler = Handler()
                    handler.postDelayed({ showLocation() }, 2000)
                }
                Activity.RESULT_CANCELED -> {
                    Log.e("Blog", "Cancel")
                }
                else -> {
                }
            }
        }
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toasty.info(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()
        // Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        //  getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        checkPlayServices()
        if (mGoogleApiClient!!.isConnected) {
            startLocationUpdates()
        }
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
                newnotifchat!!.removeEventListener(valueEventListener)
                mDatabasenotif!!.removeEventListener(valueventlistener)
                handler1.postDelayed({
                    FirebaseAuth.getInstance().signOut()
                    Toast.makeText(this@NewTabActivity, "Logging out..", Toast.LENGTH_SHORT).show()
                    // myPrefs.edit().clear().commit();
                    editor1.putBoolean("isLoggedin", false)
                    editor1.commit()
                    startActivity(Intent(this@NewTabActivity, Loginactivity::class.java))
                    finish()
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

    private fun showLocation() {
        if (ContextCompat.checkSelfPermission(this@NewTabActivity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this@NewTabActivity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this@NewTabActivity,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION), 12345)
            return
        }
        try {
            mLastLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient)
            if (mLastLocation != null) {
                latitude = mLastLocation!!.latitude
                longitude = mLastLocation!!.longitude

                addresses = geocoder?.getFromLocation(latitude, longitude, 1)
                //cityname = addresses.get(0).getAddressLine(2);
                //Toast.makeText(NewTabActivity.this,"hi"+addresses.get(0).getLocality(),Toast.LENGTH_LONG).show();
                // App.putPref("cityname",cityname.substring(0,cityname.indexOf(",")),getApplicationContext());
                cityname = addresses?.let {
                    it[0].locality
                }
                App.putPref("cityname", cityname?:"", applicationContext)
                // Toast.makeText(NewTabActivity.this,cityname,Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "ADDRESS NOT FOUND", Toast.LENGTH_SHORT).show()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    protected fun createLocationRequest() {
        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = UPDATE_INTERVAL.toLong()
        mLocationRequest!!.fastestInterval = FATEST_INTERVAL.toLong()
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest!!.smallestDisplacement = DISPLACEMENT.toFloat()
    }

    @Synchronized
    protected fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build()
    }

    private fun checkPlayServices(): Boolean {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = apiAvailability.isGooglePlayServicesAvailable(this)
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show()
            } else {
                Log.i("Push Check play", "This device is not supported.")
                finish()
            }
            return false
        }
        return true
    }

    protected fun startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this@NewTabActivity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this@NewTabActivity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this@NewTabActivity,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION), 12345)
            return
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this)

    }

    /**
     * Stopping location updates
     */
    protected fun stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this)

    }

    override fun onConnected(bundle: Bundle?) {

    }

    override fun onConnectionSuspended(i: Int) {
        mGoogleApiClient!!.connect()
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Toast.makeText(this@NewTabActivity, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.errorCode, Toast.LENGTH_SHORT).show()
    }

    override fun onLocationChanged(location: Location) {
        mLastLocation = location
    }

    private fun settingLocation(result1: PendingResult<LocationSettingsResult>) {
        result1.setResultCallback { locationSettingsResult ->
            val status = locationSettingsResult.status
            //   Toast.makeText(NewTabActivity.this,"Start",Toast.LENGTH_SHORT).show();
            val locationSettingsStates = locationSettingsResult.locationSettingsStates
            when (status.statusCode) {

                LocationSettingsStatusCodes.SUCCESS ->
                    // Toast.makeText(NewTabActivity.this,"Starst",Toast.LENGTH_SHORT).show();
                    showLocation()
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->
                    // Toast.makeText(NewTabActivity.this,"Startr",Toast.LENGTH_SHORT).show();
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        status.startResolutionForResult(
                                this@NewTabActivity,
                                LOCATION_SETTING_REQUEST)
                    } catch (e: IntentSender.SendIntentException) {
                        // Ignore the error.
                    }

                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                }
            }// All location settings are satisfied. The client can
            // initialize location requests here.
            //   Toast.makeText(NewTabActivity.this,"Startu",Toast.LENGTH_SHORT).show();
            // Location settings are not satisfied. However, we have no way
            // to fix the settings so we won't show the dialog.
        }
    }

    companion object {
        // Location updates intervals in sec
        private val UPDATE_INTERVAL = 10000 // 10 sec
        private val FATEST_INTERVAL = 5000 // 5 sec
        private val DISPLACEMENT = 10 // 10 meters
        private val GALLERY_REQUEST = 1
        private val LOCATION_SETTING_REQUEST = 1


        private val PLAY_SERVICES_RESOLUTION_REQUEST = 9000
    }

}
