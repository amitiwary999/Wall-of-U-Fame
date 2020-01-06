package com.example.amit.uniconnexample.Activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.media.ExifInterface
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.amit.uniconnexample.MediaPicker.ChosenMediaFile
import com.example.amit.uniconnexample.MediaPicker.MediaPickerActivity
import com.example.amit.uniconnexample.Others.CommonString
import com.example.amit.uniconnexample.PostBlogModel
import com.example.amit.uniconnexample.R
import com.example.amit.uniconnexample.Signupactivity
import com.example.amit.uniconnexample.View.VideoPlayerView
import com.example.amit.uniconnexample.rest.RetrofitClientBuilder
import com.example.amit.uniconnexample.rest.model.ModelResponseMessage
import com.example.amit.uniconnexample.utils.PrefManager
import com.example.amit.uniconnexample.utils.UtilDpToPixel
import com.example.amit.uniconnexample.utils.UtilPostIdGenerator
import com.example.amit.uniconnexample.utils.Utils
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_blog.*
import org.jetbrains.anko.AnkoLogger
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
    private var mVideoUri: Uri ?= null
    private var extType: String ?= null
    private var mimeType: String = ""
    private var mStorage: StorageReference? = null
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

    var simpleExoPlayer: SimpleExoPlayer? = null
    var play: ImageButton? = null
    var dataSourceFactory: DataSource.Factory? = null

    var loadControl: LoadControl = DefaultLoadControl.Builder().setBufferDurationsMs(
            15 * 1000,  // Min buffer size
            30 * 1000,  // Max buffer size
            500,  // Min playback time buffered before starting video
            100).createDefaultLoadControl()

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

        val mSelectImage = findViewById<View>(R.id.mSelectImage) as ImageView
        mStorage = FirebaseStorage.getInstance().reference
        if (auth!!.currentUser != null) {
            selected_media.setOnClickListener(View.OnClickListener {
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 12345)
                    return@OnClickListener
                }
                val intent = Intent(this, MediaPickerActivity::class.java)
                intent.putExtra(MediaPickerActivity.SINGLE_MEDIA_ALLOWED, 1)
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
            name = PrefManager.getString(CommonString.USER_NAME,"name")
            photo = PrefManager.getString(CommonString.USER_DP,"")
            val postId = UtilPostIdGenerator.generatePostId()
            var filepath: StorageReference ?= FirebaseStorage.getInstance(CommonString.STORAGE_URL).reference.child("User_Blog").child("$postId.$extType")
            if (mImageUri != null || mVideoUri != null) {
                var uploadTask: UploadTask ?= null
                mImageUri?.let {
                    uploadTask = filepath?.putFile(it)
                }

                mVideoUri?.let {
                    uploadTask = filepath?.putFile(it)
                }

                uploadTask?.addOnFailureListener {
                    mProgress?.dismiss()
                }

                uploadTask?.addOnSuccessListener {
                    uploadTask?.continueWithTask {
                        filepath?.downloadUrl
                    }?.addOnCompleteListener {
                        if(it.isSuccessful && it.result != null){
                            val downloadUrl = it.result?.toString()
                            info { "url firebase ${downloadUrl}" }
                            val blogModel = PostBlogModel(postId,desc_val, downloadUrl.toString(),  date, name?:"", photo?:"", mimeType )
                            postBlog(blogModel)
                        }else{
                            mProgress?.dismiss()
                        }
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
        FirebaseAuth.getInstance()?.currentUser?.getIdToken(false)?.addOnCompleteListener {
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
        info { "request code ${requestCode} data ${data?.getParcelableArrayListExtra<ChosenMediaFile>(CommonString.MEDIA)}" }
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
            val medias = data.getParcelableArrayListExtra<ChosenMediaFile>(CommonString.MEDIA)
            if(medias.size > 0){
                medias[0].mimeType?.let {
                    extType = medias[0].extType
                    mimeType = it
                    if(it.contains(CommonString.MimeType.IMAGE)){
                        mImageUri = medias.get(0).uri
                    }else if(it.contains(CommonString.MimeType.VIDEO)){
                        mVideoUri = medias.get(0).uri
                    }
                }
            }
        }
        mImageUri?.let {
            mSelectImage.visibility = View.VISIBLE
           // selected_video_player_view.visibility = View.GONE
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, mImageUri)

                val imageView = findViewById<View>(R.id.mSelectImage) as ImageView
                imageView.setImageBitmap(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        mVideoUri?.let {
            val height = UtilDpToPixel.convertDpToPixel(250f, this)
            mSelectImage.visibility = View.GONE
            val view = VideoPlayerView(this)
            lifecycle.addObserver(view)
            val layoutParam = (ViewGroup.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, height.toInt()))
          //  layoutParam.setMargins(margin.toInt(), margin.toInt(), margin.toInt(), margin.toInt())
            view.layoutParams = layoutParam
            selected_media.addView(view)
            view.setData(it, false)
//            selected_video_player_view.visibility = View.VISIBLE
//            selected_video_player_view.init()
//            selected_video_player_view.setData(it, false)
        }
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
