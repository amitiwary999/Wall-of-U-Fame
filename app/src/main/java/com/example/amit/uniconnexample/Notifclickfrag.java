package com.example.amit.uniconnexample;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amit.uniconnexample.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * Created by amit on 28/1/17.
 */

public class Notifclickfrag extends Fragment {
    ImageButton lk,unlk,chat;
    String check,desc,pic,nam,photo,time,date,key;
    int lke,unlke;
    Blogmodel model;
    FirebaseAuth auth;
    Handler handler=new Handler();
     View rootview;
    DatabaseReference mDatabase,mDatabaseLike,mDatabaseunlike;
    TextView post_desc,post_name,txtLike,txtUnlike,txtTime,txtDate;
    ImageView pro_pic,post_image;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview=inflater.inflate(R.layout.activity_notificationclick,container,false);
        Notification notifclick=(Notification)getActivity();
        key=notifclick.post_key;
         post_desc=(TextView)rootview.findViewById(R.id.post_desc);
         post_image =(ImageView)rootview.findViewById(R.id.postimage);
         post_name=(TextView)rootview.findViewById(R.id.bname);
         pro_pic=(ImageView)rootview.findViewById(R.id.pimage);
        txtLike=(TextView)rootview.findViewById(R.id.txtlike);
        txtUnlike=(TextView)rootview.findViewById(R.id.txtunlike);
        //  TextView txtPlace=(TextView)mView.findViewById(R.id.txtPlace);
        txtTime=(TextView)rootview.findViewById(R.id.txtTime);
        txtDate=(TextView)rootview.findViewById(R.id.txtDate);
        lk=(ImageButton)rootview.findViewById(R.id.like);
        unlk=(ImageButton)rootview.findViewById(R.id.unlike);
        chat=(ImageButton)rootview.findViewById(R.id.chat);
        auth=FirebaseAuth.getInstance();
        String n=auth.getCurrentUser().getEmail();
        check=n.substring(n.indexOf("@")+1,n.lastIndexOf("."));
        Toast.makeText(getActivity(),key,Toast.LENGTH_LONG).show();
        if(key!=null) {
            mDatabase = FirebaseDatabase.getInstance().getReference().child(check).child(key);
            model = new Blogmodel();
            mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("like");
            mDatabaseunlike = FirebaseDatabase.getInstance().getReference().child("unlike");
            setLiked(key);
            setUnliked(key);
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    model = dataSnapshot.getValue(Blogmodel.class);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            post_desc.setText(model.getDesc());
                            desc = model.getDesc();
                            if (model.getImage() == null) {
                                post_image.setVisibility(View.GONE);
                                pic = model.getImage();
                            } else {
                                post_image.setVisibility(View.VISIBLE);
                                Picasso.with(getContext()).load(model.getImage()).into(post_image);
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
                        }
                    }, 1000);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        return rootview;
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
