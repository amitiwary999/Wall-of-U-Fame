package com.example.amit.uniconnexample;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.amit.uniconnexample.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import timber.log.Timber;

import static com.example.amit.uniconnexample.R.drawable.user;

/**
 * Created by amit on 31/10/16.
 */

public class Profile extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.clg)
    EditText clg;
    @BindView(R.id.photo)
    ImageView photo;
    @BindView(R.id.loading)
    LinearLayout loading;
    FirebaseUser user;
    UserData userData;
    private TabLayout tablayoutbottom;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        email.setKeyListener(null);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userData = new UserData();
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setCursorVisible(true);
            }
        });

    /*   final AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(activity, R.drawable.ic_arrow_back_black_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.onBackPressed();
            }
        });*/
        Utils.setUpToolbarBackButton(Profile.this, toolbar);
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // mal=sprfnc.getString("mail"," ");
      //  SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
      //  mail=sharedPreferences.getString("email","");
      //  Toast.makeText(Profile.this,userData.name,Toast.LENGTH_LONG).show();
        mDatabase.child("Userdetail").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userData = dataSnapshot.getValue(UserData.class);
               // Toast.makeText(Profile.this,userData.name,Toast.LENGTH_LONG).show();
                updateUI();
                loading.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Timber.d("oncancelled");
            }
        });
        tablayoutbottom=(TabLayout)findViewById(R.id.tabLayoutbottom);
       setupTabIconsBottom();
        // setupTabIcons();
        bindWidgetsWithAnEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }

    void updateUI(){
        String nam,phn,phot,mail,clgname;
        nam=userData.name;
        phn=userData.phone;
        phot=userData.photo;
        mail=userData.email;
        clgname=userData.clg;
        name.setText(nam);
        phone.setText(phn);
        photo.setImageBitmap(Utils.decodeBase64(phot));
        email.setText(mail);
        clg.setText(clgname);
    }
    @OnClick(R.id.photo)
    void pickPhoto() {
      if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M) {
          EasyImage.openChooserWithGallery(this, "Pick profile image", 0);
        //  Toast.makeText(Profile.this, "Check", Toast.LENGTH_LONG).show();
      }
        else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
          if (ContextCompat.checkSelfPermission(Profile.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(Profile.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
              ActivityCompat.requestPermissions(Profile.this,
                      new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},12345);
              return;
          }
          EasyImage.openChooserWithGallery(this, "Pick profile image", 0);
      }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ContextCompat.checkSelfPermission(Profile.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(Profile.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Profile.this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},12345);
            return;
        }
        EasyImage.handleActivityResult(requestCode, resultCode, data, Profile.this, new DefaultCallback() {
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
                photo.setImageBitmap(bitmap);
                userData.photo = Utils.encodeToBase64(bitmap, Bitmap.CompressFormat.PNG, 100);
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
    @OnClick(R.id.save)
    void save() {
        writeUserData(user.getUid());

       // MainActivity.user = userData;
    }

    private void writeUserData(String uid) {
        final MaterialDialog dialog = new MaterialDialog.Builder(Profile.this)
                .progress(true, 100)
                .content("Saving..")
                .show();
        userData.name=name.getText().toString();
        userData.phone=phone.getText().toString();
        userData.clg=clg.getText().toString();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child("Userdetail").child(uid).setValue(userData, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                dialog.dismiss();
                Toast.makeText(Profile.this, "Saved successfully!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Profile.this, MainActivity.class));
                finish();
            }
        });
    }
    private void setupTabIconsBottom() {
        tablayoutbottom.addTab(tablayoutbottom.newTab().setIcon(R.drawable.home));
        tablayoutbottom.addTab(tablayoutbottom.newTab().setIcon(R.drawable.myaccount), true);
        tablayoutbottom.addTab(tablayoutbottom.newTab().setIcon(R.drawable.notifications));
        tablayoutbottom.addTab(tablayoutbottom.newTab().setIcon(R.drawable.message));
       // tablayoutbottom.addTab(tablayoutbottom.newTab().setIcon(R.drawable.chati));
        tablayoutbottom.addTab(tablayoutbottom.newTab().setIcon(R.drawable.settings));
    }

    private void bindWidgetsWithAnEvent()
    {
        tablayoutbottom.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setCurrentTabFragment(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                setCurrentTabFragment(tab.getPosition());
            }
        });

    }

    private void setCurrentTabFragment(int tabPosition)
    {
        switch (tabPosition)
        {
            case 0:
                startActivity(new Intent(Profile.this,Tabs.class));
                finish();
                break;
            case 1 :
              //  startActivity(new Intent(Profile.this,Profile.class));
                //replaceFragment(new Profile());
                break;
            case 2 :
                startActivity(new Intent(Profile.this,Notification.class));
                finish();
                // replaceFragment(new Message());
                break;
            case 3:
                startActivity(new Intent(Profile.this,Message.class));
                finish();
                //replaceFragment(new Notification());
                break;
          /*  case 4:
                startActivity(new Intent(Profile.this,Chat.class));
                finish();
                break;*/
            case 4:
                  startActivity(new Intent(Profile.this,Settings.class));
                finish();
                // replaceFragment(new Settings());
                break;
        }
    }

}
