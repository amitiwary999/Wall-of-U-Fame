package com.example.amit.uniconnexample.Activity

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

import com.afollestad.materialdialogs.MaterialDialog
import com.example.amit.uniconnexample.App
import com.example.amit.uniconnexample.R
import com.example.amit.uniconnexample.Signupactivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ProviderQueryResult
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_login.*

import org.apache.commons.validator.routines.EmailValidator

/**
 * Created by amit on 29/10/16.
 */

class Loginactivity : AppCompatActivity() {
    private var auth: FirebaseAuth? = null
    lateinit var mProgress: ProgressDialog
    lateinit var editor1: SharedPreferences.Editor
    private var mDatabasenotiflike: DatabaseReference? = null

    private val isNetworkConnected: Boolean
        get() {
            val cm = getSystemService(
                    Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return cm.activeNetworkInfo != null
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        title = "Login"
        auth = FirebaseAuth.getInstance()

        mProgress = ProgressDialog(this)
        log_in.setOnClickListener { login() }
        signup.setOnClickListener { signup() }
        editor1 = getSharedPreferences("com.example.amit.uniconnexample", Context.MODE_PRIVATE).edit()
        if ((this.application as App).getLogincheck()) {
            //  Intent i = new Intent(Loginactivity.this, Tabs.class);
            val i = Intent(this@Loginactivity, NewTabActivity::class.java)
            startActivity(i)
            finish()
        }
        if (isNetworkConnected) {
            forpass.setOnClickListener {
                MaterialDialog.Builder(this@Loginactivity)
                        .content("You will receive an email on your ID to reset the password:")
                        .input("Enter your email", null, false) { dialog, input ->
                            val mail = dialog.inputEditText!!.text.toString()
                            auth!!.fetchProvidersForEmail(mail).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    ///////// getProviders() will return size 1. if email ID is available.
                                    if (task.result.providers!!.size == 0)
                                        Toast.makeText(this@Loginactivity, "Email id is not registered", Toast.LENGTH_LONG).show()
                                }
                            }
                            if (EmailValidator.getInstance(false).isValid(mail)) {
                                auth!!.sendPasswordResetEmail(mail)
                                dialog.dismiss()
                            } else
                                dialog.inputEditText!!.error = "Enter a valid email"
                        }
                        .autoDismiss(false)
                        .positiveText("Reset Password")
                        .show()
            }
            if (!(this.application as App).getLogincheck()) {
                if (auth!!.currentUser != null) {
                    startActivity(Intent(this@Loginactivity, NewTabActivity::class.java))
                    // startActivity(new Intent(Loginactivity.this, Tabs.class));
                    finish()
                }
            }
            if (ContextCompat.checkSelfPermission(this@Loginactivity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this@Loginactivity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this@Loginactivity, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this@Loginactivity,
                        arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.READ_EXTERNAL_STORAGE), 12345)
                return
            }
        } else {
            Toast.makeText(this@Loginactivity, "No Internet connection", Toast.LENGTH_LONG).show()
        }
    }

    // @OnClick(R.id.signup)
    internal fun signup() {
        if (auth!!.currentUser == null) {
            val i = Intent(this@Loginactivity, Signupactivity::class.java)
            startActivity(i)
            //finish();
        }
    }

    // @OnClick(R.id.log_in)
    internal fun login() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        inputManager.hideSoftInputFromWindow(currentFocus!!.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS)
        mProgress.setMessage("***Logging you***")
        mProgress.show()
        //  loginProgress.setVisibility(View.VISIBLE);
        val cemail = email.text.toString()
        val cpassword = password.text.toString()
        Log.d("mail", cemail)
        Log.d("pass", cpassword)

        if (cemail.length != 0) {
            if (cpassword.length != 0) {
                if (isNetworkConnected) {
                    log_in.isEnabled = false
                    //   loginProgress.setVisibility(View.VISIBLE);
                    //attemptLogin(cpassword, cemail);
                    try {
                        // Toast.makeText(Loginactivity.this,cemail,Toast.LENGTH_LONG).show();
                        auth!!.signInWithEmailAndPassword(cemail, cpassword).addOnCompleteListener(this@Loginactivity) { task ->
                            if (!task.isSuccessful) {
                                log_in.isEnabled = true
                                val d = AlertDialog.Builder(this@Loginactivity)
                                d.setMessage("Id and Password combination may be wrong").setCancelable(true)
                                val alert = d.create()
                                alert.setTitle("Oops...!")
                                alert.show()
                                //  email.setError("id may be wrong ");
                                //  password.setError("password may be wrong");
                            } else {
                                mDatabasenotiflike = FirebaseDatabase.getInstance().reference.child("notificationdata").child("like")
                                mDatabasenotiflike!!.addValueEventListener(object : ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        if (!dataSnapshot.hasChild(auth!!.currentUser!!.uid)) {
                                            mDatabasenotiflike!!.child(auth!!.currentUser!!.uid).child("count").setValue(0)

                                        }
                                        // loginProgress.setVisibility(View.GONE);
                                        editor1.putBoolean("isLoggedin", true)
                                        editor1.commit()
                                        mProgress.dismiss()
                                        // Intent i = new Intent(Loginactivity.this, Tabs.class);
                                        val i = Intent(this@Loginactivity, NewTabActivity::class.java)
                                        startActivity(i)
                                        finish()
                                    }

                                    override fun onCancelled(databaseError: DatabaseError) {

                                    }
                                })
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        //Toast.makeText(Loginactivity.this,cemail,Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(this@Loginactivity, "No Internet connection", Toast.LENGTH_LONG).show()
                }
            } else {
                val d = AlertDialog.Builder(this@Loginactivity)
                d.setMessage("Password field is empty").setCancelable(true)
                val alert = d.create()
                alert.setTitle("Attention...!")
                alert.show()
            }
        } else {
            val d = AlertDialog.Builder(this@Loginactivity)
            d.setMessage("Id field is empty").setCancelable(true)
            val alert = d.create()
            alert.setTitle("Attention...!")
            alert.show()
        }
    }

    public override fun onStart() {
        super.onStart()
    }

    public override fun onStop() {
        super.onStop()
    }
}
