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
import com.example.amit.uniconnexample.Model.BlogModel
import com.example.amit.uniconnexample.Others.UserData
import com.example.amit.uniconnexample.R
import com.example.amit.uniconnexample.Signupactivity
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
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Meera on 09,November,2019
 */
class AddBlogActivity: AppCompatActivity(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
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
    internal var cityname: String? = null
    internal var date: String? = null
    internal var time: String? = null
    internal var userdata: UserData? = null
    internal var mLastLocation: Location? = null
    internal var geocoder: Geocoder?= null
    internal var addresses: List<Address> ?= null
    internal var latitude: Double = 0.toDouble()
    internal var longitude: Double = 0.toDouble()
    internal var mGoogleApiClient: GoogleApiClient? = null
    private var mLocationRequest: LocationRequest? = null
    internal var builder: LocationSettingsRequest.Builder ?= null
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
        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient()
            createLocationRequest()
        }
        builder = LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), 12345)
                return
            }
        }
        geocoder = Geocoder(this, Locale.getDefault())
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
        userdata = intent.extras!!.getParcelable<UserData>("user")
        name = userdata!!.name
        photo = userdata!!.photo

        val mSelectImage = findViewById<View>(R.id.mSelectImage) as ImageView
        mStorage = FirebaseStorage.getInstance().reference
        if (auth!!.currentUser != null) {
            mSelectImage.setOnClickListener(View.OnClickListener {
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 12345)
                    return@OnClickListener
                }
                val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                galleryIntent.type = "image/*"
                galleryIntent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(galleryIntent, GALLERY_REQUEST)
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
        //        mGoogleApiClient.connect();
        super.onStart()
        if (mGoogleApiClient != null) {
            mGoogleApiClient!!.connect()
        }
        val result1: PendingResult<LocationSettingsResult>
        result1 = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                builder?.build())
        settingLocation(result1)
    }

    override fun onStop() {
        //   mGoogleApiClient.disconnect();
        super.onStop()
        Log.e("Onstop", "stop")
        mGoogleApiClient!!.disconnect()
    }

    override fun onDestroy() {
        super.onDestroy()
        //   gps.stopUsingGPS();
    }

    override fun onPause() {
        super.onPause()
        if (mGoogleApiClient!!.isConnected) {
            stopLocationUpdates()
        }
    }

    override fun onResume() {
        super.onResume()
        checkPlayServices()
        if (mGoogleApiClient!!.isConnected) {
            startLocationUpdates()
        }
        //  showLocation();
    }


    private fun startPosting() {
        if (auth!!.currentUser != null) {
            mProgress!!.setMessage("Posting to Blog....")
            mProgress!!.show()

            //  final String title_val = titlefield.getText().toString().trim();
            val desc_val = mdesc.text.toString().trim { it <= ' ' }
            val name_val = name
            val photo_val = photo
            val id_val = auth!!.currentUser!!.uid
            //    final  String city_Name=cityname;
            val time_val = time
            val date_val = date
            val check_mail = checkmail
            val city_name: String?
            if (cityname != null) {
                // city_name = cityname.substring(0, cityname.indexOf(","));
                city_name = cityname
                if (App.getPref("cityname", applicationContext) == null) {
                    App.putPref("cityname", cityname?:"", applicationContext)
                }
            } else {
                city_name = null
            }

            // bl=new Blog(title_val,desc_val,mImageUri.toString());
            //   if ( !TextUtils.isEmpty(desc_val) && mImageUri != null) {
            if (mImageUri != null && bytearray != null) {
                val filepath = mStorage!!.child("Blog_Images").child(mImageUri!!.lastPathSegment!!)
                val uploadTask = filepath.putBytes(bytearray!!)
                uploadTask.addOnSuccessListener { taskSnapshot ->
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    val downloadUrl = taskSnapshot.downloadUrl
                    val databaseReference = mDatabas!!.push()
                    databaseReference.setValue(BlogModel(desc_val, downloadUrl!!.toString(), name_val?:"user", photo_val?:"", 0, 0, time_val?:"", date_val?:"", check_mail?:"", city_name?:""))
                    val newPost = mDatabase!!.push()
                    mProgress!!.dismiss()
                    startActivity(Intent(this, NewTabActivity::class.java))
                    finish()
                }

            } else if (desc_val.length != 0) {
                val newPost = mDatabase!!.push()
                newPost.setValue(BlogModel(desc_val, "", name_val?:"", photo_val?:"", 0, 0, time_val?:"", date_val?:"", check_mail?:"", city_name?:""))
                mProgress!!.dismiss()
                startActivity(Intent(this, NewTabActivity::class.java))
                finish()

            } else {
                mProgress!!.dismiss()
                // Toast.makeText(Blog.this,"Post can't be empty",Toast.LENGTH_LONG).show();
                val d = AlertDialog.Builder(this)
                d.setMessage("Post can't be empty").setCancelable(true)
                val alert = d.create()
                alert.setTitle("Alert...!")
                alert.show()
            }
            //  }else{
            //   mProgress.dismiss();
            //    Toast.makeText(Blog.this,"Please add a image",Toast.LENGTH_LONG).show();
            //
            // }
        } else {
            Toast.makeText(this, "You are logged out...Please login ", Toast.LENGTH_LONG).show()
            loadLoginView()
        }
    }

    private fun loadLoginView() {
        val intent = Intent(this, Signupactivity::class.java)
        startActivity(intent)
        finish()
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
            compressImage(mImageUri!!)

            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, mImageUri)

                val imageView = findViewById<View>(R.id.mSelectImage) as ImageView
                imageView.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }

        } else if (requestCode == LOCATION_SETTING_REQUEST) {
            Log.e("Blog", "Location")
            when (resultCode) {
                Activity.RESULT_OK -> {
                    Log.e("Blog", "Ok")
                    val handler = Handler()
                    handler.postDelayed({ showLocation() }, 2000)
                }
                Activity.RESULT_CANCELED -> {
                    Log.e("Blog", "Cancel")
                    val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                    val cellLocation = telephonyManager.allCellInfo as GsmCellLocation

                    val cellid= cellLocation.getCid();
                    val celllac = cellLocation.getLac();
                    cityname = celllac.toString()
                    Log.d("CellLocation", cellLocation.toString());
                    Log.d("GSM CELL ID",  cellid.toString());
                    Log.d("GSM Location Code", celllac.toString())
                }
                else -> {
                }
            }
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

    private fun showLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 12345)
            return
        }
        try {
            mLastLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient)
            if (mLastLocation != null) {
                latitude = mLastLocation!!.latitude
                longitude = mLastLocation!!.longitude

                addresses = geocoder?.getFromLocation(latitude, longitude, 1)
                // cityname = addresses.get(0).getAddressLine(2);
                cityname = addresses?.let {
                    it[0].locality
                }?:""
                Toast.makeText(this, cityname, Toast.LENGTH_SHORT).show()
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 12345)
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
        Toast.makeText(this, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.errorCode, Toast.LENGTH_SHORT).show()
    }

    override fun onLocationChanged(location: Location) {
        mLastLocation = location
    }

    private fun settingLocation(result1: PendingResult<LocationSettingsResult>) {
        result1.setResultCallback { locationSettingsResult ->
            val status = locationSettingsResult.status
            Toast.makeText(this, "Start", Toast.LENGTH_SHORT).show()
            val locationSettingsStates = locationSettingsResult.locationSettingsStates
            when (status.statusCode) {

                LocationSettingsStatusCodes.SUCCESS -> {
                    Toast.makeText(this, "Starst", Toast.LENGTH_SHORT).show()
                    showLocation()
                }
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                    Toast.makeText(this, "Startr", Toast.LENGTH_SHORT).show()
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        status.startResolutionForResult(
                                this,
                                LOCATION_SETTING_REQUEST)
                    } catch (e: IntentSender.SendIntentException) {
                        // Ignore the error.
                    }

                }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> Toast.makeText(this, "Startu", Toast.LENGTH_SHORT).show()
            }// All location settings are satisfied. The client can
            // initialize location requests here.
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
