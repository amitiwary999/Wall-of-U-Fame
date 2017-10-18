package com.example.amit.uniconnexample.Fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.amit.uniconnexample.Activity.NewTabActivity;
import com.example.amit.uniconnexample.R;
import com.example.amit.uniconnexample.Others.UserData;
import com.example.amit.uniconnexample.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import timber.log.Timber;

/**
 * Created by amit on 18/2/17.
 */

public class Profilefrag extends Fragment {
    EditText name,email,phn,clg;
    ImageView photo;
    FirebaseUser user;
    UserData userData;
    Button save;
    DatabaseReference mDatabase;
    SwipeRefreshLayout refresh;
    ProgressDialog mProgress;
    public Profilefrag() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_prof_frag,container,false);
        name=(EditText)view.findViewById(R.id.name);
        email=(EditText)view.findViewById(R.id.email);
        phn=(EditText)view.findViewById(R.id.phone);
        clg=(EditText)view.findViewById(R.id.clg);
        photo=(ImageView)view.findViewById(R.id.photo);
        save=(Button)view.findViewById(R.id.save);
        email.setKeyListener(null);
        refresh=(SwipeRefreshLayout)view.findViewById(R.id.refresh);
        mProgress=new ProgressDialog(getActivity());
        mProgress.setMessage("***Loading***");
        mProgress.show();
        //  FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userData = new UserData();
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setCursorVisible(true);
            }
        });
      //  DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Userdetail").child(user.getUid());
        mDatabase.keepSynced(true);
        if(isNetworkConnected()) {
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    userData = dataSnapshot.getValue(UserData.class);
                    // Toast.makeText(Profile.this,userData.name,Toast.LENGTH_LONG).show();
                    mProgress.dismiss();
                    updateUI();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Timber.d("oncancelled");
                }
            });
            //  }else{
            //    Toast.makeText(Profile.this,"No internet connection",Toast.LENGTH_LONG).show();
        }else{
            mProgress.dismiss();
            Toast.makeText(getActivity(),"No internet connection",Toast.LENGTH_LONG).show();
        }
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickPhoto();
            }
        });
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        ((NewTabActivity)getActivity()).setTitle("    Profile        ");
       getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }

    void updateUI(){
        String nam,phon,phot,mail,clgname;
        nam=userData.name;
        phon=userData.phone;
        phot=userData.photo;
        mail=userData.email;
        clgname=userData.clg;
        name.setText(nam);
        phn.setText(phon);
        photo.setImageBitmap(Utils.decodeBase64(phot));
        email.setText(mail);
        clg.setText(clgname);
    }
 //   @OnClick(R.id.photo)
    void pickPhoto() {
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M) {
            EasyImage.openChooserWithGallery(this, "Pick profile image", 0);
            //  Toast.makeText(Profile.this, "Check", Toast.LENGTH_LONG).show();
        }
        else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},12345);
                return;
            }
            EasyImage.openChooserWithGallery(this, "Pick profile image", 0);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},12345);
            return;
        }
        EasyImage.handleActivityResult(requestCode, resultCode, data, getActivity(), new DefaultCallback() {
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
  //  @OnClick(R.id.save)
    void save() {
        if(isNetworkConnected()) {
            writeUserData(user.getUid());
        }else{
            Toast.makeText(getActivity(), "No Internet connection", Toast.LENGTH_LONG).show();
        }

    }

    private void writeUserData(String uid) {
        final MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .progress(true, 100)
                .content("Saving..")
                .show();
        userData.name=name.getText().toString();
        userData.phone=phn.getText().toString();
        userData.clg=clg.getText().toString();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child("Userdetail").child(uid).setValue(userData, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                dialog.dismiss();
                Toast.makeText(getActivity(), "Saved successfully!", Toast.LENGTH_SHORT).show();
             //   startActivity(new Intent(Profile.this, Tabs.class));
               // finish();
            }
        });
    }
    public void refresh(){
        mProgress.show();
        if(isNetworkConnected()) {
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    userData = dataSnapshot.getValue(UserData.class);
                    // Toast.makeText(Profile.this,userData.name,Toast.LENGTH_LONG).show();
                    mProgress.dismiss();
                    updateUI();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Timber.d("oncancelled");
                }
            });
            //  }else{
            //    Toast.makeText(Profile.this,"No internet connection",Toast.LENGTH_LONG).show();
        }else{
            mProgress.dismiss();
            Toast.makeText(getActivity(),"No internet connection",Toast.LENGTH_LONG).show();
        }
        refreshcomplete();
    }
    public void refreshcomplete(){
        refresh.setRefreshing(false);
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(
                Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

}
