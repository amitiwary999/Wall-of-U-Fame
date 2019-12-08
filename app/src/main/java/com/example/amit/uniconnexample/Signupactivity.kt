package com.example.amit.uniconnexample

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import com.google.android.material.textfield.TextInputLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast

import com.example.amit.uniconnexample.Activity.Loginactivity
import com.example.amit.uniconnexample.Activity.NewTabActivity
import com.example.amit.uniconnexample.Others.CommonString
import com.example.amit.uniconnexample.Others.UserData
import com.example.amit.uniconnexample.rest.RetrofitClientBuilder
import com.example.amit.uniconnexample.rest.model.ModelResponseMessage
import com.example.amit.uniconnexample.rest.model.UserDetailRequestModel
import com.example.amit.uniconnexample.utils.PrefManager
import com.example.amit.uniconnexample.utils.UtilPostIdGenerator
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_signup.*

import org.apache.commons.validator.routines.EmailValidator

import java.io.File
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

/**
 * Created by amit on 29/10/16.
 */

class Signupactivity : AppCompatActivity() {
    internal var user: FirebaseUser? = null
    private var mProgress: ProgressDialog? = null
    private var mAuth: FirebaseAuth? = null
    lateinit var userData: UserData
    lateinit var mal: String
    lateinit var pass: String
    lateinit var confrmpass: String
    lateinit var phn: String
    lateinit var nam: String
    lateinit var clgname: String
    internal var flag = 0
    var check: String ?= ""
    var mImageUri: Uri?= null

    companion object{
        private val GALLERY_REQUEST = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        mProgress = ProgressDialog(this)
        userData = UserData()
        mAuth = FirebaseAuth.getInstance()
        val tabIconColor = ContextCompat.getColor(baseContext, R.color.md_orange_600)
        person!!.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN)
        msg!!.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN)
        lock1!!.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN)
        lock2!!.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN)

        iview.setOnClickListener { pickPhoto() }

        sign_up.setOnClickListener{ sup() }
    }

    internal fun pickPhoto() {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 12345)
            return
        }
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryIntent.type = "image/*"
        galleryIntent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(galleryIntent, GALLERY_REQUEST)
    }

    internal fun sup() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        inputManager.hideSoftInputFromWindow(currentFocus!!.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS)
        pass = password!!.editText!!.text.toString().trim { it <= ' ' }
        confrmpass = confirmpassword!!.editText!!.text.toString().trim { it <= ' ' }
        if (EmailValidator.getInstance(false).isValid(email!!.editText!!.text.toString().trim { it <= ' ' })) {
            email!!.error = null
            if (password!!.editText!!.text.toString().trim { it <= ' ' }.length >= 6) {
                password!!.error = null
                if (confrmpass == pass) {
                    if (name!!.editText!!.text.toString().trim { it <= ' ' }.length >= 4) {
                        name!!.error = null

                        attemptSignup(pass, email?.editText?.text?.toString()?:"")
                        sign_up?.isEnabled = false
                    } else
                        name!!.error = "Enter at least 4 characters"
                } else {
                    val d = AlertDialog.Builder(this@Signupactivity)
                    d.setMessage("Password is not matching").setCancelable(true)
                    val alert = d.create()
                    alert.setTitle("Oops...!")
                    alert.show()
                }
            } else
                password!!.error = "Not long enough"
        } else {
            Toast.makeText(this@Signupactivity, email!!.editText!!.text.toString().trim { it <= ' ' }, Toast.LENGTH_LONG).show()
            email!!.error = "Invalid email"
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GALLERY_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 12345)
                return
            }

            mImageUri = data.data

            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, mImageUri)
                iview.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    fun uploadProfile(){
        if (mImageUri != null) {
            val filepath =  FirebaseStorage.getInstance().reference.child("User_Image").child(mImageUri!!.lastPathSegment!!)
            mImageUri?.let {
                val uploadTask = filepath.putFile(it)
                uploadTask.addOnSuccessListener {
                    val downloadUrl = it.downloadUrl?.toString()?:""
                    doSingUp(downloadUrl)
                }

                uploadTask.addOnFailureListener {
                    doSingUp("")
                }

            }

        }
    }

    internal fun attemptSignup(pass: String, id: String) {
        Log.d("Sign up","attempt")
        var userId = ""
        mAuth?.createUserWithEmailAndPassword(id, pass)
                ?.addOnCompleteListener(this) { task ->
                    if (!task.isSuccessful) {
                        sign_up!!.isEnabled = true
                        Toast.makeText(this@Signupactivity, "Signup failed." + task.exception!!.message,
                                Toast.LENGTH_SHORT).show()
                    }else{
                        Log.d("Sign up","completed ${mAuth?.currentUser?.uid}")
                        mAuth?.currentUser?.getToken(false)?.addOnCompleteListener {
                            if(it.isSuccessful){
                                uploadProfile()
                            }else{
                                Log.d("sign up","auth failed")
                            }

                        }
                    }
                }
    }

    fun doSingUp(imageUrl: String){
        FirebaseAuth.getInstance()?.currentUser?.getToken(false)?.addOnCompleteListener {
            if(it.isSuccessful && it.result != null){
                val userId = FirebaseAuth.getInstance()?.currentUser?.uid?:""
                val userDetailRequestModel = UserDetailRequestModel(name?.editText?.text?.toString()?:"", imageUrl, email?.editText?.text?.toString()?:"")
                RetrofitClientBuilder(CommonString.base_url).getmNetworkRepository()?.sendUser("Bearer ${it.result?.token}", userDetailRequestModel)
                        ?.enqueue(object : Callback<ModelResponseMessage>{
                            override fun onFailure(call: Call<ModelResponseMessage>, t: Throwable) {
                                t.printStackTrace()
                                sign_up?.isEnabled = true
                            }

                            override fun onResponse(call: Call<ModelResponseMessage>, response: Response<ModelResponseMessage>) {
                                PrefManager.putString(CommonString.USER_ID, userId)
                                PrefManager.putString(CommonString.USER_NAME, name?.editText?.text?.toString()?:"")
                                PrefManager.putString(CommonString.USER_DP, imageUrl)
                                PrefManager.putString(CommonString.USER_EMAIL, email?.editText?.text?.toString()?:"")
                                val i = Intent(this@Signupactivity, NewTabActivity::class.java)
                                startActivity(i)
                                finish()
                            }
                        })
            }
        }
    }

    public override fun onStart() {
        super.onStart()
    }

    public override fun onStop() {
        super.onStop()
    }

}
