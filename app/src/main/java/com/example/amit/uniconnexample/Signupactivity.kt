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
import com.example.amit.uniconnexample.Others.UserData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import org.apache.commons.validator.routines.EmailValidator

import java.io.File

import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import timber.log.Timber

/**
 * Created by amit on 29/10/16.
 */

class Signupactivity : AppCompatActivity() {
    @BindView(R.id.name)
    internal var name: TextInputLayout? = null
    @BindView(R.id.email)
    internal var email: TextInputLayout? = null
    @BindView(R.id.password)
    internal var password: TextInputLayout? = null
    @BindView(R.id.phone)
    internal var phone: TextInputLayout? = null
    @BindView(R.id.confirmpassword)
    internal var confrmpassword: TextInputLayout? = null
    @BindView(R.id.iview)
    internal var iv: ImageView? = null
    @BindView(R.id.clg)
    internal var clg: TextInputLayout? = null
    @BindView(R.id.sign_up)
    internal var signup: Button? = null
    @BindView(R.id.person)
    internal var person: ImageView? = null
    @BindView(R.id.msg)
    internal var msg: ImageView? = null
    @BindView(R.id.lock1)
    internal var lock1: ImageView? = null
    @BindView(R.id.lock2)
    internal var lock2: ImageView? = null
    @BindView(R.id.college)
    internal var college: ImageView? = null
    @BindView(R.id.phn)
    internal var phonen: ImageView? = null
    internal var user: FirebaseUser? = null
    private var mProgress: ProgressDialog? = null
    private var mAuth: FirebaseAuth? = null
    private var mAuthListener: FirebaseAuth.AuthStateListener? = null
    internal var userData: UserData
    internal var mDatabasenotiflike: DatabaseReference
    internal var mal: String
    internal var pass: String
    internal var confrmpass: String
    internal var phn: String
    internal var nam: String
    internal var clgname: String
    internal var flag = 0
    internal var check: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        ButterKnife.bind(this)
        mProgress = ProgressDialog(this)
        userData = UserData()
        mDatabasenotiflike = FirebaseDatabase.getInstance().reference.child("notificationdata").child("like")
        phn = phone!!.editText!!.text.toString().trim { it <= ' ' }
        pass = password!!.editText!!.text.toString().trim { it <= ' ' }
        confrmpass = confrmpassword!!.editText!!.text.toString().trim { it <= ' ' }
        nam = name!!.editText!!.text.toString().trim { it <= ' ' }
        mal = email!!.editText!!.text.toString().trim { it <= ' ' }
        clgname = clg!!.editText!!.text.toString().trim { it <= ' ' }
        mAuth = FirebaseAuth.getInstance()
        val tabIconColor = ContextCompat.getColor(baseContext, R.color.md_orange_600)
        person!!.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN)
        msg!!.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN)
        lock1!!.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN)
        lock2!!.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN)
        college!!.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN)
        phonen!!.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN)
        //    user=FirebaseAuth.getInstance().getCurrentUser();
        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            user = firebaseAuth.currentUser
            if (user != null) {                    // User is signed in
                Toast.makeText(this@Signupactivity, "Successfully signed up", Toast.LENGTH_SHORT).show()

                writeUserData(user!!.uid)
                //      mDatabasenotiflike.child(user.getUid()).setValue(new Likemodel(0));
                val intent = Intent(this@Signupactivity, Loginactivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                // .putExtra("user", userData));
                //finish();
            } else {                    // User is signed out
                Timber.d("User is signed out")
            }
        }
    }

    private fun writeUserData(uid: String) {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.reference
        val myref = database.reference
        myRef.child("Userdetail").child(uid).setValue(userData)
        myref.child(check + "chat").child(uid).setValue(com.example.amit.uniconnexample.Chatusermodel(userData.name, userData.photo))
    }

    @OnClick(R.id.iview)
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

    @OnClick(R.id.sign_up)
    internal fun sup() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        inputManager.hideSoftInputFromWindow(currentFocus!!.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS)
        pass = password!!.editText!!.text.toString().trim { it <= ' ' }
        confrmpass = confrmpassword!!.editText!!.text.toString().trim { it <= ' ' }
        if (EmailValidator.getInstance(false).isValid(email!!.editText!!.text.toString().trim { it <= ' ' })) {
            email!!.error = null
            if (password!!.editText!!.text.toString().trim { it <= ' ' }.length >= 6) {
                password!!.error = null
                if (confrmpass == pass) {
                    if (name!!.editText!!.text.toString().trim { it <= ' ' }.length >= 4) {
                        name!!.error = null
                        if (phone!!.editText!!.text.toString().trim { it <= ' ' }.length == 10) {
                            phone!!.error = null
                            if (clg!!.editText!!.text.toString().trim { it <= ' ' }.length != 0) {
                                clg!!.error = null
                            } else {
                                clg!!.error = "Enter college name"
                            }
                            attemptSignup(password!!.editText!!.text.toString().trim { it <= ' ' }, email!!.editText!!.text.toString().trim { it <= ' ' })
                            signup!!.isEnabled = false
                        } else
                            phone!!.error = "Enter 10 digits"
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
                iv!!.setImageBitmap(bitmap)
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
        //  mProgress.setMessage("Loading....");
        //   mProgress.show();
        userData.phone = phone!!.editText!!.text.toString()
        userData.name = name!!.editText!!.text.toString()
        userData.email = email!!.editText!!.text.toString()
        val n = userData.email
        check = n.substring(n.indexOf("@") + 1, n.lastIndexOf("."))
        userData.clg = clg!!.editText!!.text.toString()
        val icon = BitmapFactory.decodeResource(resources,
                R.drawable.user)
        if (flag != 1) {
            userData.photo = com.example.amit.uniconnexample.utils.Utils.encodeToBase64(icon, Bitmap.CompressFormat.PNG, 100)
        }
        Timber.d(userData.photo)
        Timber.d("flag$flag")
        phn = phone!!.editText!!.text.toString().trim { it <= ' ' }

        mAuth!!.createUserWithEmailAndPassword(id, pass)
                .addOnCompleteListener(this) { task ->
                    if (!task.isSuccessful) {
                        signup!!.isEnabled = true
                        Toast.makeText(this@Signupactivity, "Signup failed." + task.exception!!.message,
                                Toast.LENGTH_SHORT).show()
                    }
                }
    }

    public override fun onStart() {
        super.onStart()
        mAuth!!.addAuthStateListener(mAuthListener!!)
    }

    public override fun onStop() {
        super.onStop()
        if (mAuthListener != null) {
            mAuth!!.removeAuthStateListener(mAuthListener!!)
        }
    }

}
