package com.example.amit.uniconnexample;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amit.uniconnexample.utils.Utils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

/**
 * Created by amit on 31/10/16.
 */

public class Blog extends AppCompatActivity {
    private FirebaseAuth auth;
    private Uri mImageUri = null;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    EditText titlefield,mDesc;
    Toolbar toolbar;
    SharedPreferences sprfnc;
    private ProgressDialog mProgress;
    String mal,check,name,photo,bitimage;
    UserData userdata;
    private static final int GALLERY_REQUEST = 1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);
        mProgress = new ProgressDialog(this);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
      //  mal=sharedPreferences.getString("email","");
       // userdata=new UserData();
        auth=FirebaseAuth.getInstance();
        String n=auth.getCurrentUser().getEmail();
        check=n.substring(n.indexOf("@")+1,n.lastIndexOf("."));
      //  titlefield = (EditText) findViewById(R.id.titleField);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        Utils.setUpToolbarBackButton(this, toolbar);
       // title=(TextView)findViewById(R.id.title);
       // image=(ImageView)findViewById(R.id.primage);
        mDesc = (EditText) findViewById(R.id.mdesc);
        Button buttondone = (Button) findViewById(R.id.buttondone);
        mDatabase= FirebaseDatabase.getInstance().getReference().child(check);
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
                    startPosting();
                }
            });
        }else{
            loadLoginView();
        }
    }

    private void startPosting() {
        if(auth.getCurrentUser()!=null) {
            mProgress.setMessage("Posting to Blog....");
            mProgress.show();

          //  final String title_val = titlefield.getText().toString().trim();
            final String desc_val = mDesc.getText().toString().trim();
            final String name_val=name;
            final String photo_val=photo;
            // bl=new Blog(title_val,desc_val,mImageUri.toString());
         //   if ( !TextUtils.isEmpty(desc_val) && mImageUri != null) {
                if(mImageUri!=null) {
                    StorageReference filepath = mStorage.child("Blog_Images").child(mImageUri.getLastPathSegment());
                    filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            DatabaseReference newPost = mDatabase.push();
                            newPost.setValue(new Blogmodel(desc_val, downloadUrl.toString(), name_val, photo_val));
                       /* newPost.child("title").setValue(title_val);
                        newPost.child("desc").setValue(desc_val);
                        newPost.child("image").setValue(downloadUrl.toString());
                        newPost.child("uname").setValue(name_val);
                        newPost.child("profpic").setValue(photo);*/
                            mProgress.dismiss();
                            startActivity(new Intent(Blog.this, MainActivity.class));
                            finish();
                        }
                    });

                }else{
                    DatabaseReference newPost=mDatabase.push();
                    newPost.setValue(new Blogmodel(desc_val,null,name_val,photo_val));
                    mProgress.dismiss();
                    startActivity(new Intent(Blog.this, MainActivity.class));
                    finish();

                }
          //  }else{
             //   mProgress.dismiss();
            //    Toast.makeText(Blog.this,"Please add a image",Toast.LENGTH_LONG).show();
//
           // }
        }else{
            loadLoginView();
        }
    }
    private void loadLoginView() {
        Intent intent = new Intent(this, Signupactivity.class);
        startActivity(intent);
        finish();
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

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageUri);

                ImageView imageView = (ImageView) findViewById(R.id.mSelectImage);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
