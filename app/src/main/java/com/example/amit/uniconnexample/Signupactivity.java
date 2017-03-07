package com.example.amit.uniconnexample;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.commons.validator.routines.EmailValidator;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.internal.Utils;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import timber.log.Timber;

import static com.example.amit.uniconnexample.R.drawable.user;
import static com.example.amit.uniconnexample.R.id.photo;

/**
 * Created by amit on 29/10/16.
 */

public class Signupactivity extends AppCompatActivity {
   @BindView(R.id.name)
   TextInputLayout name;
   @BindView(R.id.email)
   TextInputLayout email;
   @BindView(R.id.password)
   TextInputLayout password;
   @BindView(R.id.phone)
   TextInputLayout phone;
    @BindView(R.id.confirmpassword)
    TextInputLayout confrmpassword;
    @BindView(R.id.iview)
    ImageView iv;
    @BindView(R.id.clg)
    TextInputLayout clg;
    @BindView(R.id.sign_up)
    Button signup;
    @BindView(R.id.person)
     ImageView person;
    @BindView(R.id.msg)
    ImageView msg;
    @BindView(R.id.lock1)
    ImageView lock1;
    @BindView(R.id.lock2)
    ImageView lock2;
    @BindView(R.id.college)
    ImageView college;
    @BindView(R.id.phn)
    ImageView phonen;
    FirebaseUser user;
    private ProgressDialog mProgress;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    UserData userData;
    DatabaseReference mDatabasenotiflike;
    String mal,pass,confrmpass,phn,nam,clgname;
    int flag=0;
    String check;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        mProgress = new ProgressDialog(this);
        userData = new UserData();
        mDatabasenotiflike=FirebaseDatabase.getInstance().getReference().child("notificationdata").child("like");
        phn=phone.getEditText().getText().toString().trim();
        pass=password.getEditText().getText().toString().trim();
        confrmpass=confrmpassword.getEditText().getText().toString().trim();
        nam=name.getEditText().getText().toString().trim();
        mal=email.getEditText().getText().toString().trim();
        clgname=clg.getEditText().getText().toString().trim();
        mAuth = FirebaseAuth.getInstance();
        int tabIconColor = ContextCompat.getColor(getBaseContext(), R.color.md_orange_600);
        person.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
        msg.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
        lock1.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
        lock2.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
        college.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
        phonen.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
     //    user=FirebaseAuth.getInstance().getCurrentUser();
       mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                 user = firebaseAuth.getCurrentUser();
                if (user != null) {                    // User is signed in
                    Toast.makeText(Signupactivity.this, "Successfully signed up", Toast.LENGTH_SHORT).show();

                    writeUserData(user.getUid());
              //      mDatabasenotiflike.child(user.getUid()).setValue(new Likemodel(0));
                    Intent intent=new Intent(Signupactivity.this, Loginactivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                           // .putExtra("user", userData));
                    //finish();
                } else {                    // User is signed out
                    Timber.d("User is signed out");
                }
            }
        };
    }
    private void writeUserData(String uid) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        DatabaseReference myref=database.getReference();
        myRef.child("Userdetail").child(uid).setValue(userData);
        myref.child(check+"chat").child(uid).setValue(new Chatusermodel(userData.name,userData.photo));
    }

    @OnClick(R.id.iview)
    void pickPhoto(){
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M) {
            EasyImage.openChooserWithGallery(this, "Pick profile image", 0);
            //  Toast.makeText(Profile.this, "Check", Toast.LENGTH_LONG).show();
        }
        else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(Signupactivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(Signupactivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Signupactivity.this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},12345);
                return;
            }
            EasyImage.openChooserWithGallery(this, "Pick profile image", 0);
        }
    }
 @OnClick(R.id.sign_up)
    void sup(){
     InputMethodManager inputManager = (InputMethodManager)
             getSystemService(Context.INPUT_METHOD_SERVICE);

     inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
             InputMethodManager.HIDE_NOT_ALWAYS);
     pass=password.getEditText().getText().toString().trim();
     confrmpass=confrmpassword.getEditText().getText().toString().trim();
     if (EmailValidator.getInstance(false).isValid(email.getEditText().getText().toString().trim())) {
         email.setError(null);
         if (password.getEditText().getText().toString().trim().length()>= 6) {
             password.setError(null);
             if(confrmpass.equals(pass)) {
                 if (name.getEditText().getText().toString().trim().length() >= 4) {
                     name.setError(null);
                     if (phone.getEditText().getText().toString().trim().length() == 10) {
                         phone.setError(null);
                         if(clg.getEditText().getText().toString().trim().length()!=0){
                             clg.setError(null);
                         }else{
                             clg.setError("Enter college name");
                         }
                         attemptSignup(password.getEditText().getText().toString().trim(), email.getEditText().getText().toString().trim());
                         signup.setEnabled(false);
                     } else
                         phone.setError("Enter 10 digits");
                 } else
                     name.setError("Enter at least 4 characters");
             }else{
                 AlertDialog.Builder d = new AlertDialog.Builder(Signupactivity.this);
                 d.setMessage("Password is not matching").
                         setCancelable(true);
                 AlertDialog alert = d.create();
                 alert.setTitle("Oops...!");
                 alert.show();
             }
         } else
             password.setError("Not long enough");
     } else {
         Toast.makeText(Signupactivity.this,email.getEditText().getText().toString().trim(),Toast.LENGTH_LONG).show();
         email.setError("Invalid email");
     }
 }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ContextCompat.checkSelfPermission(Signupactivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(Signupactivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Signupactivity.this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA},12345);
            return;
        }
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                //Handle the image
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_4444;
                Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
                bitmap = getResizedBitmap(bitmap, 100);
                Timber.d("xxx" + bitmap.getByteCount());
                iv.setImageBitmap(bitmap);
                flag=1;
                Timber.d("flag"+flag);
                userData.photo = com.example.amit.uniconnexample.utils.Utils.encodeToBase64(bitmap, Bitmap.CompressFormat.PNG, 100);

            }
        });
    }
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
    void attemptSignup(String pass, String id) {
      //  mProgress.setMessage("Loading....");
     //   mProgress.show();
        userData.phone = phone.getEditText().getText().toString();
        userData.name = name.getEditText().getText().toString();
        userData.email=email.getEditText().getText().toString();
        String n=userData.email;
         check=n.substring(n.indexOf("@")+1,n.lastIndexOf("."));
        userData.clg=clg.getEditText().getText().toString();
        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.drawable.user);
        if(flag!=1) {
            userData.photo = com.example.amit.uniconnexample.utils.Utils.encodeToBase64(icon, Bitmap.CompressFormat.PNG, 100);
        }
        Timber.d(userData.photo);
        Timber.d("flag"+flag);
        phn=phone.getEditText().getText().toString().trim();
      //  pass=password.getEditText().getText().toString().trim();
       // nam=name.getEditText().getText().toString().trim();
       // mal=email.getEditText().getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(id, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            signup.setEnabled(true);
                            Toast.makeText(Signupactivity.this, "Signup failed." + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                       /* else{
                            Toast.makeText(Signupactivity.this, "Successfully signed up", Toast.LENGTH_SHORT).show();
                            writeUserData(user.getUid());
                            mProgress.dismiss();
                            startActivity(new Intent(Signupactivity.this, MainActivity.class)
                                    .putExtra("user", userData));

                            finish();
                        }*/
                    }
                });
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}
