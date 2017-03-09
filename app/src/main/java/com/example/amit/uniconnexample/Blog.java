package com.example.amit.uniconnexample;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amit.uniconnexample.Model.BlogModel;
import com.example.amit.uniconnexample.utils.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by amit on 31/10/16.
 */

public class Blog extends AppCompatActivity implements ConnectionCallbacks, OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    private FirebaseAuth auth;
    private Uri mImageUri = null;
    private StorageReference mStorage;
    private DatabaseReference mDatabase,mDatabas;
    EditText titlefield,mDesc;
    Toolbar toolbar;
    private TrackGPS gps;
    SharedPreferences sprfnc;
    private ProgressDialog mProgress;
    byte[] bytearray;
    String mal,check,name,photo,checkmail,bitimage,cityname=null,date=null,time=null;
    UserData userdata;
    LocationManager locationManager;
    int LOCATION_REFRESH_TIME=10;
    int LOCATION_REFRESH_DISTANCE=1000;
    Location location=null,mLastLocation;
    Geocoder geocoder;
    List<Address> addresses;
    double latitude,longitude;
    GoogleApiClient mGoogleApiClient;
    PendingResult<LocationSettingsResult> result;
    private LocationRequest mLocationRequest;
    private boolean mRequestingLocationUpdates = false;
    LocationSettingsRequest.Builder builder;
    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters
    private static final int GALLERY_REQUEST = 1;
    private static final int LOCATION_SETTING_REQUEST = 1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);
        mProgress = new ProgressDialog(this);
        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();
            createLocationRequest();
        }
        builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);


        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(Blog.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(Blog.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Blog.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 12345);
                return;
            }
        }
        geocoder = new Geocoder(this, Locale.getDefault());

       /* gps = new TrackGPS(this);
        if(gps.canGetLocation()){


            longitude = gps.getLongitude();
            latitude = gps .getLatitude();

            Toast.makeText(getApplicationContext(),"Longitude:"+Double.toString(longitude)+"\nLatitude:"+Double.toString(latitude),Toast.LENGTH_SHORT).show();
        }
        else
        {

            gps.showSettingsAlert();
        }*/

      /*  if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(Blog.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(Blog.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Blog.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},12345);
                return;
            }

            geocoder = new Geocoder(this, Locale.getDefault());
            try {
                 gps = new TrackGPS(this);
        if(gps.canGetLocation()){


            longitude = gps.getLongitude();
            latitude = gps .getLatitude();

            Toast.makeText(getApplicationContext(),"Longitude:"+Double.toString(longitude)+"\nLatitude:"+Double.toString(latitude),Toast.LENGTH_SHORT).show();
        }
        else
        {

            gps.showSettingsAlert();
        }
       /* locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
         List<String> providers = locationManager.getProviders(true);
            Criteria cri=new Criteria();

            String provider=locationManager.getBestProvider(cri,false);
          //  if(provider!=null & !provider.equals(""))
          //  {
              for (int i = providers.size() - 1; i >= 0; i--) {
                location=locationManager.getLastKnownLocation(providers.get(i));
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                        LOCATION_REFRESH_DISTANCE, this);
                if(location!=null){
                    onLocationChanged(location);
                }
                else{
                    Toast.makeText(getApplicationContext(),"location not found",Toast.LENGTH_LONG ).show();
                }
            }
            //else {
           //     Toast.makeText(getApplicationContext(),"Provider is null",Toast.LENGTH_LONG).show();
           // }
            if (location != null) {
            latitude=location.getLatitude();
            longitude=location.getLongitude();

                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                 }
        else
        {
            Toast.makeText(this,"ADDRESS NOT FOUND",Toast.LENGTH_SHORT).show();
        } */
       /*         addresses = geocoder.getFromLocation(latitude, longitude, 1);
                cityname = addresses.get(0).getAddressLine(0);
            }catch (IOException e){
                e.printStackTrace();
            }

    }else if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M) {
            geocoder = new Geocoder(this, Locale.getDefault());
            try {
                gps = new TrackGPS(this);
        if(gps.canGetLocation()){


            longitude = gps.getLongitude();
            latitude = gps .getLatitude();
            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                cityname = addresses.get(0).getAddressLine(0);
            }catch (IndexOutOfBoundsException e){
                e.printStackTrace();
            }
            Toast.makeText(getApplicationContext(),"Longitude:"+Double.toString(longitude)+"\nLatitude:"+Double.toString(latitude),Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"location not found",Toast.LENGTH_LONG ).show();
            gps.showSettingsAlert();
        }
             /*   if (mGoogleApiClient == null) {
                  mGoogleApiClient = new GoogleApiClient.Builder(this)
             .addConnectionCallbacks(this)
             .addOnConnectionFailedListener(this)
             .addApi(LocationServices.API)
             .build();
            }
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
             List<String> providers = locationManager.getProviders(true);
         //   locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
         //           LOCATION_REFRESH_DISTANCE, this);
          //  locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
          //  Criteria cri=new Criteria();

         //   String provider=locationManager.getBestProvider(cri,false);
        //    if(provider!=null & !provider.equals(""))
          //  {
                 for (int i = providers.size() - 1; i >= 0; i--) {
                location=locationManager.getLastKnownLocation(providers.get(i));
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                        LOCATION_REFRESH_DISTANCE, this);
                if(location!=null){

                  //  onLocationChanged(location);
                    break;
                }
                else{
                    Toast.makeText(getApplicationContext(),"location not found",Toast.LENGTH_LONG ).show();
                }
            }
            //else {
              //  Toast.makeText(getApplicationContext(),"Provider is null",Toast.LENGTH_LONG).show();
            //}
             if (location != null) {
            latitude=location.getLatitude();
            longitude=location.getLongitude();

                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                 cityname = addresses.get(0).getAddressLine(0);
                }
                 else {
               Toast.makeText(this,"ADDRESS NOT FOUND",Toast.LENGTH_SHORT).show();
             }

            }catch (IOException e){
                e.printStackTrace();
            }

        }*/
      /*  if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }*/
      //  SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
      //  mal=sharedPreferences.getString("email","");
       // userdata=new UserData();
        auth=FirebaseAuth.getInstance();
        String n=auth.getCurrentUser().getEmail();
        check=n.substring(n.indexOf("@")+1,n.lastIndexOf("."));
        checkmail=n.substring(n.indexOf("@")+1,n.lastIndexOf("."))+n.substring(n.lastIndexOf(".")+1);
      //  titlefield = (EditText) findViewById(R.id.titleField);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        Utils.setUpToolbarBackButton(this, toolbar);
       // title=(TextView)findViewById(R.id.title);
       // image=(ImageView)findViewById(R.id.primage);
        date=getDate();
        time=getCurrentTime();
        mDesc = (EditText) findViewById(R.id.mdesc);
        Button buttondone = (Button) findViewById(R.id.buttondone);
        mDatabase= FirebaseDatabase.getInstance().getReference().child(check);
        mDatabas=FirebaseDatabase.getInstance().getReference().child("Posts");
        userdata = getIntent().getExtras().getParcelable("user");
       // byte[] decodestring= Base64.decode(userdata.photo,Base64.DEFAULT);
        name=userdata.name;
        photo=userdata.photo;
       // Bitmap icon= BitmapFactory.decodeResource(getResources(),photo);

       // bitimage=com.example.amit.uniconnexample.utils.Utils.encodeToBase64(icon, Bitmap.CompressFormat.PNG, 100);
      //  title.setText(name);
      //  image.setImageBitmap(Utils.decodeBase64(photo));
       // Toast.makeText(Blog.this,name,Toast.LENGTH_LONG).show();

        ImageView mSelectImage=(ImageView) findViewById(R.id.mSelectImage);
        mStorage = FirebaseStorage.getInstance().getReference();
        if(auth.getCurrentUser()!=null) {
            mSelectImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ContextCompat.checkSelfPermission(Blog.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(Blog.this,
                                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},12345);
                        return;
                    }
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    galleryIntent.setType("image/*");
                    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(galleryIntent, GALLERY_REQUEST);

                }
            });
            buttondone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  //  Toast.makeText(Blog.this,"right",Toast.LENGTH_LONG).show();
                    if(isNetworkConnected()) {
                        startPosting();
                    }else{
                        Toast.makeText(Blog.this, "No Internet connection", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }else{
            Toast.makeText(Blog.this,"You are logged out...Please login ",Toast.LENGTH_LONG).show();
            loadLoginView();
        }
    }

    @Override
    protected void onStart() {
//        mGoogleApiClient.connect();
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        PendingResult<LocationSettingsResult> result1;
        result1 = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                builder.build());
        settingLocation(result1);
        Log.e("Blog","Hi"+cityname);
    }

    @Override
    protected void onStop() {
     //   mGoogleApiClient.disconnect();
        super.onStop();
        Log.e("Onstop","stop");
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
     //   gps.stopUsingGPS();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
        if (mGoogleApiClient.isConnected() ) {
            startLocationUpdates();
        }
      //  showLocation();
    }


    private void startPosting() {
        if(auth.getCurrentUser()!=null) {
            mProgress.setMessage("Posting to Blog....");
            mProgress.show();

          //  final String title_val = titlefield.getText().toString().trim();
            final String desc_val = mDesc.getText().toString().trim();
            final String name_val=name;
            final String photo_val=photo;
            final String id_val=auth.getCurrentUser().getUid();
        //    final  String city_Name=cityname;
            final String time_val=time;
            final String date_val=date;
            final String check_mail=checkmail;
            final String city_name;
            if(cityname!=null) {
                city_name = cityname.substring(0, cityname.indexOf(","));
            }else{
                city_name=null;
            }

            // bl=new Blog(title_val,desc_val,mImageUri.toString());
         //   if ( !TextUtils.isEmpty(desc_val) && mImageUri != null) {
                if(mImageUri!=null) {
                    StorageReference filepath = mStorage.child("Blog_Images").child(mImageUri.getLastPathSegment());
                    UploadTask uploadTask=filepath.putBytes(bytearray);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            DatabaseReference databaseReference=mDatabas.push();
                            databaseReference.setValue(new BlogModel(id_val,desc_val, downloadUrl.toString(), name_val, photo_val,0,0,time_val,date_val,check_mail,city_name));
                            DatabaseReference newPost = mDatabase.push();
                           // newPost.setValue(new Blogmodel(id_val,desc_val, downloadUrl.toString(), name_val, photo_val,0,0,time_val,date_val));
                       /* newPost.child("title").setValue(title_val);
                        newPost.child("desc").setValue(desc_val);
                        newPost.child("image").setValue(downloadUrl.toString());
                        newPost.child("uname").setValue(name_val);
                        newPost.child("profpic").setValue(photo);*/
                            mProgress.dismiss();
                           // startActivity(new Intent(Blog.this, Tabs.class));
                            finish();
                        }
                    });
                  /*  filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            DatabaseReference newPost = mDatabase.push();
                            newPost.setValue(new Blogmodel(id_val,desc_val, downloadUrl.toString(), name_val, photo_val,0,0,time_val,date_val));
                       /* newPost.child("title").setValue(title_val);
                        newPost.child("desc").setValue(desc_val);
                        newPost.child("image").setValue(downloadUrl.toString());
                        newPost.child("uname").setValue(name_val);
                        newPost.child("profpic").setValue(photo);*/
                          /*  mProgress.dismiss();
                            startActivity(new Intent(Blog.this, Tabs.class));
                            finish();
                        }
                    });*/

                }else if(desc_val.length()!=0){
                    DatabaseReference newPost=mDatabase.push();
                    newPost.setValue(new BlogModel(id_val,desc_val,null,name_val,photo_val,0,0,time_val,date_val,check_mail,city_name));
                    mProgress.dismiss();
                    startActivity(new Intent(Blog.this, Tabs.class));
                    finish();

                }else{
                    mProgress.dismiss();
                   // Toast.makeText(Blog.this,"Post can't be empty",Toast.LENGTH_LONG).show();
                    AlertDialog.Builder d = new AlertDialog.Builder(Blog.this);
                    d.setMessage("Post can't be empty").
                            setCancelable(true);
                    AlertDialog alert = d.create();
                    alert.setTitle("Alert...!");
                    alert.show();
                }
          //  }else{
             //   mProgress.dismiss();
            //    Toast.makeText(Blog.this,"Please add a image",Toast.LENGTH_LONG).show();
//
           // }
        }else{
            Toast.makeText(Blog.this,"You are logged out...Please login ",Toast.LENGTH_LONG).show();
            loadLoginView();
        }
    }
    private void loadLoginView() {
        Intent intent = new Intent(this, Signupactivity.class);
        startActivity(intent);
        finish();
    }


    public static String getCurrentTime() {
        String delegate = "hh:mm aaa";
        return (String) DateFormat.format(delegate, Calendar.getInstance().getTime());
    }

    public static String getDate(){
        SimpleDateFormat sdf = new SimpleDateFormat(" MMM dd yyyy");
        return sdf.format(new Date());
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            if (ContextCompat.checkSelfPermission(Blog.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Blog.this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},12345);
                return;
            }

            mImageUri = data.getData();
            compressImage(mImageUri);

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageUri);

                ImageView imageView = (ImageView) findViewById(R.id.mSelectImage);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(requestCode==LOCATION_SETTING_REQUEST){
            Log.e("Blog","Location");
            switch(resultCode){
                case Activity.RESULT_OK:
                    Log.e("Blog","Ok");
                    Handler handler=new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                             showLocation();
                        }
                    },2000);

                    break;
                case RESULT_CANCELED:
                    Log.e("Blog","Cancel");
                    PendingResult<LocationSettingsResult> result;
                    result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                            builder.build());
                    settingLocation(result);
                    break;
                default:
                    break;
            }
        }
    }
    public void compressImage(Uri imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 616.0f;
        float maxWidth = 412.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80,stream);
        bytearray=stream.toByteArray();
        try {
            stream.close();
            stream = null;
        } catch (IOException e) {

            e.printStackTrace();
        }
      /*  FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80,stream);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/

        //return filename;

    }

  /*  public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }*/

    private String getRealPathFromURI(Uri contentUri) {
       // Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(
                Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    private void showLocation(){
        if(ContextCompat.checkSelfPermission(Blog.this, Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(Blog.this, Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED)  {
            ActivityCompat.requestPermissions(Blog.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},12345);
            return;
        }
        try {
            mLastLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                latitude = mLastLocation.getLatitude();
                longitude = mLastLocation.getLongitude();

                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                cityname = addresses.get(0).getAddressLine(2);
                Toast.makeText(Blog.this,cityname,Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "ADDRESS NOT FOUND", Toast.LENGTH_SHORT).show();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }


    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i("Push Check play", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    protected void startLocationUpdates() {
      if(ContextCompat.checkSelfPermission(Blog.this, Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(Blog.this, Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED)  {
          ActivityCompat.requestPermissions(Blog.this,
                  new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},12345);
          return;
      }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

    }

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
          Toast.makeText(Blog.this,"Connection failed: ConnectionResult.getErrorCode() = "
                  + connectionResult.getErrorCode(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation=location;
    }
    private void settingLocation( PendingResult<LocationSettingsResult> result1){
        result1.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();
                Toast.makeText(Blog.this,"Start",Toast.LENGTH_SHORT).show();
                final LocationSettingsStates locationSettingsStates = locationSettingsResult.getLocationSettingsStates();
                switch (status.getStatusCode()) {

                    case LocationSettingsStatusCodes.SUCCESS:
                        Toast.makeText(Blog.this,"Starst",Toast.LENGTH_SHORT).show();
                        showLocation();
                        // All location settings are satisfied. The client can
                        // initialize location requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Toast.makeText(Blog.this,"Startr",Toast.LENGTH_SHORT).show();
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    Blog.this,
                                    LOCATION_SETTING_REQUEST);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Toast.makeText(Blog.this,"Startu",Toast.LENGTH_SHORT).show();
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Log.d(TAG, "onActivityResult() called with: " + "requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");
        switch (requestCode) {
            case Activity.RESULT_OK:
                break;
            case Activity.RESULT_CANCELED:
                settingLocation();
                Log.e("Blog", "RESULT_CANCELED");
                break;
            default:
                break;
        }
    }*/

   /* @Override
    public void onLocationChanged(Location location) {
      //  this.location=location;

       //   latitude=location. getLatitude();

       // longitude=location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
       /* mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
          //  mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
          //  mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
        }*/
   /* }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }*/
}
