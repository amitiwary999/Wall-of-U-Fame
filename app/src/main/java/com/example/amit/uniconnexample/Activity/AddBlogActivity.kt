package com.example.amit.uniconnexample.Activity

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.*
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.media.ExifInterface
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.telephony.TelephonyManager
import android.telephony.gsm.GsmCellLocation
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.amit.uniconnexample.App
import com.example.amit.uniconnexample.MediaPicker.Media
import com.example.amit.uniconnexample.MediaPicker.MediaPickerActivity
import com.example.amit.uniconnexample.Model.BlogModel
import com.example.amit.uniconnexample.Others.CommonString
import com.example.amit.uniconnexample.Others.UserData
import com.example.amit.uniconnexample.PostBlogModel
import com.example.amit.uniconnexample.R
import com.example.amit.uniconnexample.Signupactivity
import com.example.amit.uniconnexample.rest.RetrofitClientBuilder
import com.example.amit.uniconnexample.rest.model.ModelResponseMessage
import com.example.amit.uniconnexample.utils.PrefManager
import com.example.amit.uniconnexample.utils.UtilPostIdGenerator
import com.example.amit.uniconnexample.utils.Utils
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.location.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_blog.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.db.NULL
import org.jetbrains.anko.info
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Meera on 09,November,2019
 */
class AddBlogActivity: AppCompatActivity(), AnkoLogger{
    private var auth: FirebaseAuth? = null
    private var mImageUri: Uri? = null
    private var mStorage: StorageReference? = null
    private var mDatabase: DatabaseReference? = null
    private var mDatabas: DatabaseReference? = null
    private var mProgress: ProgressDialog? = null
    var bytearray: ByteArray ?= null
    internal var check: String ?= null
    internal var name: String ?= null
    internal var photo: String ?= null
    internal var checkmail: String ?= null
    internal var date: String? = null
    internal var time: String? = null
    private val isNetworkConnected: Boolean
        get() {
            val cm = getSystemService(
                    Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return cm.activeNetworkInfo != null
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blog)
        mProgress = ProgressDialog(this)

        auth = FirebaseAuth.getInstance()
        val n = auth!!.currentUser!!.email
        check = n!!.substring(n.indexOf("@") + 1, n.lastIndexOf("."))
        checkmail = n.substring(n.indexOf("@") + 1, n.lastIndexOf(".")) + n.substring(n.lastIndexOf(".") + 1)
        Utils.setUpToolbarBackButton(this, toolbar)
        date = getDate()
        time = currentTime
        val buttondone = findViewById<View>(R.id.buttondone) as Button
        mDatabase = FirebaseDatabase.getInstance().reference.child(check)
        mDatabas = FirebaseDatabase.getInstance().reference.child("Posts")
        name = PrefManager.getString(CommonString.USER_NAME, "")
        photo = PrefManager.getString(CommonString.USER_DP, "")

        val mSelectImage = findViewById<View>(R.id.mSelectImage) as ImageView
        mStorage = FirebaseStorage.getInstance().reference
        if (auth!!.currentUser != null) {
            mSelectImage.setOnClickListener(View.OnClickListener {
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 12345)
                    return@OnClickListener
                }
                val intent = Intent(this, MediaPickerActivity::class.java)
                startActivityForResult(intent, CommonString.MEDIA_PICKER_ACTIVITY)

//                val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//                galleryIntent.type = "image/*"
//                galleryIntent.action = Intent.ACTION_GET_CONTENT
//                startActivityForResult(galleryIntent, GALLERY_REQUEST)
            })
            buttondone.setOnClickListener {
                if (isNetworkConnected) {
                    startPosting()
                } else {
                    Toast.makeText(this, "No Internet connection", Toast.LENGTH_LONG).show()
                }
            }
        } else {
            Toast.makeText(this, "You are logged out...Please login ", Toast.LENGTH_LONG).show()
            loadLoginView()
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
        Log.e("Onstop", "stop")
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }


    private fun startPosting() {
        if (auth!!.currentUser != null) {
            mProgress!!.setMessage("Posting to Blog....")
            mProgress!!.show()

            val desc_val = mdesc.text.toString().trim { it <= ' ' }
            PrefManager.getString(CommonString.USER_NAME,"name")
            PrefManager.getString(CommonString.USER_DP,"")
            if (mImageUri != null) {
                val filepath = mStorage!!.child("User_Blog").child(mImageUri!!.lastPathSegment!!)
                mImageUri?.let {
                    val uploadTask = filepath.putFile(it)
                    uploadTask.addOnSuccessListener { taskSnapshot ->
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        val downloadUrl = taskSnapshot.downloadUrl
                        info { "url firebase ${downloadUrl}" }
                        val blogModel = PostBlogModel(UtilPostIdGenerator.generatePostId(),desc_val, downloadUrl.toString(),  date, name?:"", photo?:"" )
                        postBlog(blogModel)
                    }
                }

            } else if (desc_val.length != 0) {

                val blogModel = PostBlogModel(UtilPostIdGenerator.generatePostId(),desc_val, "", date, name?:"", photo?:"" )
                postBlog(blogModel)
            } else {
                mProgress!!.dismiss()
                // Toast.makeText(Blog.this,"Post can't be empty",Toast.LENGTH_LONG).show();
                val d = AlertDialog.Builder(this)
                d.setMessage("Post can't be empty").setCancelable(true)
                val alert = d.create()
                alert.setTitle("Alert...!")
                alert.show()
            }
        } else {
            Toast.makeText(this, "You are logged out...Please login ", Toast.LENGTH_LONG).show()
            loadLoginView()
        }
    }

    fun postBlog(postBlogModel: PostBlogModel){
        FirebaseAuth.getInstance()?.currentUser?.getToken(false)?.addOnCompleteListener {
            if(it.isSuccessful){
                RetrofitClientBuilder(CommonString.base_url).getmNetworkRepository()?.sendPost("Bearer ${it.result?.token}", postBlogModel)
                        ?.enqueue(object : Callback<ModelResponseMessage>{
                            override fun onFailure(call: Call<ModelResponseMessage>, t: Throwable) {
                                t.printStackTrace()
                                mProgress?.dismiss()
                            }

                            override fun onResponse(call: Call<ModelResponseMessage>, response: Response<ModelResponseMessage>) {
                                if(response.isSuccessful)
                                    Log.d("Add blog","success ${response.body()}")

                                Toast.makeText(this@AddBlogActivity, "Your post is posted ", Toast.LENGTH_LONG).show()
                                onBackPressed()
                                mProgress?.dismiss()
                            }
                        })
            }
        }
    }

    private fun loadLoginView() {
        val intent = Intent(this, Signupactivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        info { "request code ${requestCode} data ${data?.getParcelableArrayListExtra<Media>(CommonString.MEDIA)}" }
        if (requestCode == GALLERY_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 12345)
                return
            }

            mImageUri = data.data
            info { "picked image ${mImageUri}" }
          //  compressImage(mImageUri!!)

        }

        if(requestCode == CommonString.MEDIA_PICKER_ACTIVITY && data != null){
            val medias = data.getParcelableArrayListExtra<Media>(CommonString.MEDIA)
            if(medias.size > 0){
                mImageUri = medias.get(0).uri
            }
        }

        try {
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, mImageUri)

            val imageView = findViewById<View>(R.id.mSelectImage) as ImageView
            imageView.setImageBitmap(bitmap)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun compressImage(imageUri: Uri) {

        val filePath = getRealPathFromURI(imageUri)
        var scaledBitmap: Bitmap? = null

        val options = BitmapFactory.Options()

        //      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
        //      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true
        var bmp = BitmapFactory.decodeFile(filePath, options)

        var actualHeight = options.outHeight
        var actualWidth = options.outWidth

        //      max Height and width values of the compressed image is taken as 816x612

        val maxHeight = 616.0f
        val maxWidth = 412.0f
        var imgRatio = (actualWidth / actualHeight).toFloat()
        val maxRatio = maxWidth / maxHeight

        //      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight
                actualWidth = (imgRatio * actualWidth).toInt()
                actualHeight = maxHeight.toInt()
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth
                actualHeight = (imgRatio * actualHeight).toInt()
                actualWidth = maxWidth.toInt()
            } else {
                actualHeight = maxHeight.toInt()
                actualWidth = maxWidth.toInt()

            }
        }

        //      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight)

        //      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false

        //      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true
        options.inInputShareable = true
        options.inTempStorage = ByteArray(16 * 1024)

        try {
            //          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()

        }

        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }

        val ratioX = actualWidth / options.outWidth.toFloat()
        val ratioY = actualHeight / options.outHeight.toFloat()
        val middleX = actualWidth / 2.0f
        val middleY = actualHeight / 2.0f

        val scaleMatrix = Matrix()
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)

        val canvas = Canvas(scaledBitmap!!)
        canvas.matrix = scaleMatrix
        canvas.drawBitmap(bmp, middleX - bmp.width / 2, middleY - bmp.height / 2, Paint(Paint.FILTER_BITMAP_FLAG))

        //      check the rotation of the image and display it properly
        val exif: ExifInterface
        try {
            exif = ExifInterface(filePath)

            val orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0)
            Log.d("EXIF", "Exif: $orientation")
            val matrix = Matrix()
            if (orientation == 6) {
                matrix.postRotate(90f)
                Log.d("EXIF", "Exif: $orientation")
            } else if (orientation == 3) {
                matrix.postRotate(180f)
                Log.d("EXIF", "Exif: $orientation")
            } else if (orientation == 8) {
                matrix.postRotate(270f)
                Log.d("EXIF", "Exif: $orientation")
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.width, scaledBitmap.height, matrix,
                    true)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        var stream: ByteArrayOutputStream? = ByteArrayOutputStream()
        scaledBitmap!!.compress(Bitmap.CompressFormat.JPEG, 80, stream)
        bytearray = stream!!.toByteArray()
        try {
            stream.close()
            stream = null
        } catch (e: IOException) {

            e.printStackTrace()
        }

    }

    private fun getRealPathFromURI(contentUri: Uri): String? {
        // Uri contentUri = Uri.parse(contentURI);
        val cursor = contentResolver.query(contentUri, null, null, null, null)
        if (cursor == null) {
            return contentUri.path
        } else {
            cursor.moveToFirst()
            val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            return cursor.getString(index)
        }
    }

    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
            val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        }
        val totalPixels = (width * height).toFloat()
        val totalReqPixelsCap = (reqWidth * reqHeight * 2).toFloat()
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++
        }

        return inSampleSize
    }

    companion object {
        // Location updates intervals in sec
        private val UPDATE_INTERVAL = 10000 // 10 sec
        private val FATEST_INTERVAL = 5000 // 5 sec
        private val DISPLACEMENT = 10 // 10 meters
        private val GALLERY_REQUEST = 1
        private val LOCATION_SETTING_REQUEST = 1


        val currentTime: String
            get() {
                val delegate = "hh:mm aaa"
                return DateFormat.format(delegate, Calendar.getInstance().time) as String
            }

        fun getDate(): String {
            val sdf = SimpleDateFormat(" MMM dd yyyy")
            return sdf.format(Date())
        }


        private val PLAY_SERVICES_RESOLUTION_REQUEST = 9000
    }

}
