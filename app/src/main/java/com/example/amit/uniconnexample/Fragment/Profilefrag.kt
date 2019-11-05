package com.example.amit.uniconnexample.Fragment

import android.Manifest
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast

import com.afollestad.materialdialogs.MaterialDialog
import com.example.amit.uniconnexample.Activity.NewTabActivity
import com.example.amit.uniconnexample.R
import com.example.amit.uniconnexample.Others.UserData
import com.example.amit.uniconnexample.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_prof_frag.*

import java.io.File

import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import timber.log.Timber

/**
 * Created by amit on 18/2/17.
 */

class Profilefrag : Fragment() {
    internal var user: FirebaseUser? = null
    lateinit var userData: UserData
    lateinit var mDatabase: DatabaseReference
    lateinit var mProgress: ProgressDialog
    private val isNetworkConnected: Boolean
        get() {
            val cm = activity!!.getSystemService(
                    Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return cm.activeNetworkInfo != null
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_prof_frag, container, false)
        email.keyListener = null
        mProgress = ProgressDialog(activity)
        mProgress.setMessage("***Loading***")
        mProgress.show()
        //  FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        user = FirebaseAuth.getInstance().currentUser
        userData = UserData()
        name.setOnClickListener { name.isCursorVisible = true }
        //  DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().reference.child("Userdetail").child(user!!.uid)
        mDatabase.keepSynced(true)
        if (isNetworkConnected) {
            mDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    userData = dataSnapshot.getValue<UserData>(UserData::class.java)
                    // Toast.makeText(Profile.this,userData.name,Toast.LENGTH_LONG).show();
                    mProgress.dismiss()
                    updateUI()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Timber.d("oncancelled")
                }
            })
            //  }else{
            //    Toast.makeText(Profile.this,"No internet connection",Toast.LENGTH_LONG).show();
        } else {
            mProgress.dismiss()
            Toast.makeText(activity, "No internet connection", Toast.LENGTH_LONG).show()
        }
        save.setOnClickListener { save() }
        photo.setOnClickListener { pickPhoto() }
        refresh.setOnRefreshListener { refresh() }
        return view
    }

    override fun onResume() {
        super.onResume()
        (activity as NewTabActivity).setTitle("    Profile        ")
        activity!!.window.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        )
    }

    internal fun updateUI() {
        val nam: String
        val phon: String
        val phot: String
        val mail: String
        val clgname: String
        nam = userData.name?:""
        phon = userData.phone?:""
        phot = userData.photo?:""
        mail = userData.email?:""
        clgname = userData.clg?:""
        name.setText(nam)
        phone.setText(phon)
        photo.setImageBitmap(Utils.decodeBase64(phot))
        email.setText(mail)
        clg.setText(clgname)
    }

    //   @OnClick(R.id.photo)
    internal fun pickPhoto() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            EasyImage.openChooserWithGallery(this, "Pick profile image", 0)
            //  Toast.makeText(Profile.this, "Check", Toast.LENGTH_LONG).show();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(activity!!, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(activity!!, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity!!,
                        arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA), 12345)
                return
            }
            EasyImage.openChooserWithGallery(this, "Pick profile image", 0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (ContextCompat.checkSelfPermission(activity!!, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(activity!!, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity!!,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA), 12345)
            return
        }
        EasyImage.handleActivityResult(requestCode, resultCode, data, activity, object : DefaultCallback() {
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
                photo.setImageBitmap(bitmap)
                userData.photo = Utils.encodeToBase64(bitmap, Bitmap.CompressFormat.PNG, 100)
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

    //  @OnClick(R.id.save)
    internal fun save() {
        if (isNetworkConnected) {
            writeUserData(user!!.uid)
        } else {
            Toast.makeText(activity, "No Internet connection", Toast.LENGTH_LONG).show()
        }

    }

    private fun writeUserData(uid: String) {
        val dialog = MaterialDialog.Builder(activity!!)
                .progress(true, 100)
                .content("Saving..")
                .show()
        userData.name = name.text.toString()
        userData.phone = phone.text.toString()
        userData.clg = clg.text.toString()
        val database = FirebaseDatabase.getInstance()
        val myRef = database.reference
        myRef.child("Userdetail").child(uid).setValue(userData) { databaseError, databaseReference ->
            dialog.dismiss()
            Toast.makeText(activity, "Saved successfully!", Toast.LENGTH_SHORT).show()
            //   startActivity(new Intent(Profile.this, Tabs.class));
            // finish();
        }
    }

    fun refresh() {
        mProgress.show()
        if (isNetworkConnected) {
            mDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    userData = dataSnapshot.getValue<UserData>(UserData::class.java)
                    // Toast.makeText(Profile.this,userData.name,Toast.LENGTH_LONG).show();
                    mProgress.dismiss()
                    updateUI()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Timber.d("oncancelled")
                }
            })
            //  }else{
            //    Toast.makeText(Profile.this,"No internet connection",Toast.LENGTH_LONG).show();
        } else {
            mProgress.dismiss()
            Toast.makeText(activity, "No internet connection", Toast.LENGTH_LONG).show()
        }
        refreshcomplete()
    }

    fun refreshcomplete() {
        refresh.isRefreshing = false
    }

}
