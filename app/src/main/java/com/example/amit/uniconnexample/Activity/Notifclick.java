package com.example.amit.uniconnexample.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amit.uniconnexample.Model.BlogModel;
import com.example.amit.uniconnexample.R;
import com.example.amit.uniconnexample.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tt.whorlviewlibrary.WhorlView;

/**
 * Created by amit on 28/1/17.
 */

public class Notifclick extends AppCompatActivity{
    ImageButton lk,unlk,chat;
    String check,desc,pic,nam,photo,time,date,key;
    int lke,unlke;
    BlogModel model;
    WhorlView whorlView;
    FirebaseAuth auth;
    FirebaseUser user;
    Bundle bundle;
    Toolbar toolbar;
    int lik,unlike,count;
    int unlik,lyk;
    private Boolean processlike=false,processunlike=false;
    Handler handler=new Handler();
    View rootview;
    DatabaseReference mDatabase,mDatabaseLike,mDatabaseunlike;
    TextView post_desc,post_name,txtLike,txtUnlike,txtTime,txtDate;
    ImageView pro_pic,post_image;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificationclick);

        whorlView = (WhorlView)findViewById(R.id.whorl2);
        bundle=getIntent().getExtras();
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //     startActivity(new Intent(Chatstart.this,Tabs.class));
                finish();
            }
        });
        if(bundle.getString("postkey")!=null) {
            key = bundle.getString("postkey");
            Log.d("key ","key is "+key);
        }
        post_desc=(TextView)findViewById(R.id.post_desc);
        post_image =(ImageView)findViewById(R.id.postimage);
        post_name=(TextView)findViewById(R.id.bname);
        pro_pic=(ImageView)findViewById(R.id.pimage);
        txtLike=(TextView)findViewById(R.id.txtlike);
        txtUnlike=(TextView)findViewById(R.id.txtunlike);
        //  TextView txtPlace=(TextView)mView.findViewById(R.id.txtPlace);
        txtTime=(TextView)findViewById(R.id.txtTime);
        txtDate=(TextView)findViewById(R.id.txtDate);
        lk=(ImageButton)findViewById(R.id.like);
        unlk=(ImageButton)findViewById(R.id.unlike);
        chat=(ImageButton)findViewById(R.id.chat);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        String n=auth.getCurrentUser().getEmail();
        check=n.substring(n.indexOf("@")+1,n.lastIndexOf("."));
       // Toast.makeText(this,key,Toast.LENGTH_LONG).show();

            mDatabase = FirebaseDatabase.getInstance().getReference().child("Posts").child(key);
            Log.d("check ", check+" "+key);
            model = new BlogModel();
            mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("like");
            mDatabaseunlike = FirebaseDatabase.getInstance().getReference().child("unlike");

            lk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    processlike = true;
                    mDatabaseLike.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (processlike) {
                                if (dataSnapshot.child(key).hasChild(user.getUid())) {

                                    // int
                                    if (model.getLike() > 0) {
                                        lik = model.getLike() - 1;
                                    } else {
                                        lik = 0;
                                    }
                                    mDatabaseLike.child(key).child(user.getUid()).removeValue();
                                    mDatabase.setValue(new BlogModel(model.getKey(), model.getDesc(), model.getImage(), model.getName(), model.getPropic(), lik, model.getUnlike(), model.getTime(), model.getDate(), model.getEmailflag(),model.getCityname()));

                                    //   processlike=true;
                                    processlike = false;

                                } else {
                                    mDatabaseunlike.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            // Toast.makeText(getActivity(),(String)dataSnapshot.child(post_key).child(user.getUid()).getValue(),Toast.LENGTH_LONG).show();
                                            if (dataSnapshot.child(key).hasChild(user.getUid())) {
                                                //   if( mDatabaseunlike.child(post_key).child(user.getUid()).equals("Unliked")){
                                                // int unlik=model.getUnlike();
                                                //  Toast.makeText(getActivity(),dataSnapshot.child(post_key).child(user.getUid()).getValue(String.class),Toast.LENGTH_LONG).show();
                                                if (model.getUnlike() > 0) {
                                                    unlik = model.getUnlike() - 1;
                                                } else {
                                                    unlik = 0;
                                                }
                                                mDatabaseunlike.child(key).child(user.getUid()).removeValue();
                                                mDatabase.setValue(new BlogModel(model.getKey(), model.getDesc(), model.getImage(), model.getName(), model.getPropic(), lik, unlik, model.getTime(), model.getDate(), model.getEmailflag(), model.getCityname()));


                                            }


                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                    lik = model.getLike() + 1;
                                    mDatabaseLike.child(key).child(user.getUid()).setValue("Liked");
                                    mDatabase.setValue(new BlogModel(model.getKey(), model.getDesc(), model.getImage(), model.getName(), model.getPropic(), lik, model.getUnlike(), model.getTime(), model.getDate(), model.getEmailflag(), model.getCityname()));
                                    //  final int

                                    //   processlike=true;

                                    processlike = false;
                                }
                                processlike=false;
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            });
            unlk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    processunlike=true;
                    //   unlike=model.getUnlike();
                    mDatabaseunlike.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(processunlike){
                                if(dataSnapshot.child(key).hasChild(user.getUid())){

                                    //  final int
                                    if(model.getUnlike()>0) {
                                        unlike = model.getUnlike() - 1;
                                    }else{
                                        unlike=0;
                                    }
                                    mDatabaseunlike.child(key).child(user.getUid()).removeValue();
                                    mDatabase.setValue(new BlogModel(model.getKey(),model.getDesc(),model.getImage(),model.getName(),model.getPropic(),model.getLike(),unlike,model.getTime(),model.getDate(), model.getEmailflag(), model.getCityname()));

                                    processunlike=false;
                                }else{

                                    mDatabaseLike.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            if(dataSnapshot.child(key).hasChild(user.getUid())){
                                                //   if( mDatabaseunlike.child(post_key).child(user.getUid()).equals("Unliked")){

                                                //   Toast.makeText(getActivity(),(String)dataSnapshot.child(post_key).child(user.getUid()).getValue(),Toast.LENGTH_LONG).show();
                                                if(model.getLike()>0) {
                                                    lyk = model.getLike() - 1;
                                                }else{
                                                    lyk=0;
                                                }
                                                mDatabaseLike.child(key).child(user.getUid()).removeValue();
                                                mDatabase.setValue(new BlogModel(model.getKey(),model.getDesc(),model.getImage(),model.getName(),model.getPropic(),lyk,unlike,model.getTime(),model.getDate(), model.getEmailflag(), model.getCityname()));

                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                    mDatabaseunlike.child(key).child(user.getUid()).setValue("Unliked");
                                    // final int
                                    unlike=model.getUnlike()+1;
                                     mDatabase.setValue(new BlogModel(model.getKey(),model.getDesc(),model.getImage(),model.getName(),model.getPropic(),model.getLike(),unlike,model.getTime(),model.getDate(), model.getEmailflag(), model.getCityname()));


                                    processunlike=false;
                                }

                                processunlike=false;
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            });


    }

    @Override
    protected void onStart() {
        super.onStart();
        if(key!=null) {
            whorlView.setVisibility(View.VISIBLE);
            whorlView.start();
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    setLiked(key);
                    setUnliked(key);
                    Log.d("flag ", "null "+dataSnapshot);
                    model = dataSnapshot.getValue(BlogModel.class);
                    post_desc.setText(model.getDesc());
                    desc = model.getDesc();
                    if (model.getImage() == null) {
                        post_image.setVisibility(View.GONE);
                        pic = model.getImage();
                    } else {
                        post_image.setVisibility(View.VISIBLE);
                        Picasso.with(getBaseContext()).load(model.getImage()).into(post_image);
                        pic = model.getImage();
                    }
                    if (model.getName() == null) {
                        post_name.setText("Anonyms");
                        nam = "Anonyms";
                    } else {
                        post_name.setText(model.getName());
                        nam = model.getName();
                    }
                    pro_pic.setImageBitmap(Utils.decodeBase64(model.getPropic()));
                    photo = model.getPropic();
                    String likE = Integer.toString(model.getLike());
                    txtLike.setText(likE);
                    String txtUnlik = Integer.toString(model.getUnlike());
                    txtUnlike.setText(txtUnlik);
                    lke = model.getLike();
                    unlke = model.getUnlike();
                    //   txtPlace.setText(model.getCityname());
                    txtTime.setText(model.getTime());
                    time = model.getTime();
                    txtDate.setText(model.getDate());
                    date = model.getDate();
                    whorlView.stop();
                    whorlView.setVisibility(View.GONE);


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public void setLiked(final String post_liked){
        mDatabaseLike.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(post_liked).hasChild(auth.getCurrentUser().getUid())){
                    lk.setColorFilter(getResources().getColor(R.color.Grenn));
                    mDatabaseunlike.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(!(dataSnapshot.child(post_liked).hasChild(auth.getCurrentUser().getUid()))){
                                unlk.setColorFilter(getResources().getColor(R.color.Black));
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }else{
                    lk.setColorFilter(getResources().getColor(R.color.Black));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setUnliked(final String post_key){
        mDatabaseunlike.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(post_key).hasChild(auth.getCurrentUser().getUid())){
                    unlk.setColorFilter(getResources().getColor(R.color.Grenn));
                    mDatabaseLike.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(!(dataSnapshot.child(post_key).hasChild(auth.getCurrentUser().getUid()))){
                                lk.setColorFilter(getResources().getColor(R.color.Black));
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }else{

                    unlk.setColorFilter(getResources().getColor(R.color.Black));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}

