package com.example.amit.uniconnexample

import android.Manifest
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_signup.*

import org.apache.commons.validator.routines.EmailValidator

import java.io.File
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

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
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            EasyImage.openChooserWithGallery(this, "Pick profile image", 0)
            //  Toast.makeText(Profile.this, "Check", Toast.LENGTH_LONG).show();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this@Signupactivity, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this@Signupactivity, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this@Signupactivity,
                        arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA), 12345)
                return
            }
            EasyImage.openChooserWithGallery(this, "Pick profile image", 0)
        }
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
        if (ContextCompat.checkSelfPermission(this@Signupactivity, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this@Signupactivity, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this@Signupactivity,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA), 12345)
            return
        }
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, object : DefaultCallback() {
            override fun onImagePickerError(e: Exception?, source: EasyImage.ImageSource?, type: Int) {
                //Some error handling
            }

            override fun onImagePicked(imageFile: File, source: EasyImage.ImageSource, type: Int) {
                //Handle the image
                val options = BitmapFactory.Options()
                options.inPreferredConfig = Bitmap.Config.ARGB_4444
                var bitmap = BitmapFactory.decodeFile(imageFile.absolutePath, options)
                bitmap = getResizedBitmap(bitmap, 100)
                Timber.d("xxx" + bitmap.byteCount)
                iview!!.setImageBitmap(bitmap)
                flag = 1
                Timber.d("flag$flag")
                userData.photo = com.example.amit.uniconnexample.utils.Utils.encodeToBase64(bitmap, Bitmap.CompressFormat.PNG, 100)

            }
        })
    }

    fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap {
        var width = image.width
        var height = image.height

        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 0) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
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
                                userId = mAuth?.currentUser?.uid?:""
                                val userDetailRequestModel = UserDetailRequestModel(name?.editText?.text?.toString()?:"", "", email?.editText?.text?.toString()?:"")
                                RetrofitClientBuilder(CommonString.base_url).getmNetworkRepository()?.sendUser("Bearer ${it.result?.token}", userDetailRequestModel)
                                        ?.enqueue(object : Callback<ModelResponseMessage>{
                                            override fun onFailure(call: Call<ModelResponseMessage>, t: Throwable) {
                                                t.printStackTrace()
                                                sign_up?.isEnabled = true
                                            }

                                            override fun onResponse(call: Call<ModelResponseMessage>, response: Response<ModelResponseMessage>) {
                                                PrefManager.putString(CommonString.USER_ID, userId)
                                                val i = Intent(this@Signupactivity, NewTabActivity::class.java)
                                                startActivity(i)
                                                finish()
                                            }
                                        })
                            }else{
                                Log.d("sign up","auth failed")
                            }

                        }
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
