package com.example.amit.uniconnexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.amit.uniconnexample.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by amit on 28/11/16.
 */

public class Message extends AppCompatActivity {
    private TabLayout tablayoutbottom;
    private Toolbar toolbar;
    RecyclerView rview;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager laymanager;
    private static ArrayList<Message_model> data;
    private DatabaseReference mDatabase,newmessage;
    String key,name,text,pic;
     long num;
    private FirebaseAuth auth;
    FirebaseUser user;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        rview=(RecyclerView)findViewById(R.id.mchat_list);
        rview.setLayoutManager(new LinearLayoutManager(this));
        mDatabase= FirebaseDatabase.getInstance().getReference().child("message").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        newmessage=FirebaseDatabase.getInstance().getReference().child("Userdetail");
        data=new ArrayList<Message_model>();
         setTitle("");
        tablayoutbottom=(TabLayout)findViewById(R.id.tabLayoutbottom);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        Utils.setUpToolbarBackButton(Message.this, toolbar);
        setupTabIconsBottom();
       // setupTabIcons();
        bindWidgetsWithAnEvent();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    key=snapshot.getKey();
                    Toast.makeText(Message.this,key,Toast.LENGTH_SHORT).show();
                    // newmessage=FirebaseDatabase.getInstance().getReference().child("Userdetail").child(key).child("photo");
                    newmessage.child(key).child("photo").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            pic= dataSnapshot.getValue(String.class);
                            //   Toast.makeText(Message.this,"ii  "+pic,Toast.LENGTH_LONG).show();
                            //    text.setText(name);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    newmessage.child(key).child("name").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                           name=dataSnapshot.getValue(String.class);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    num=snapshot.getChildrenCount();

                    mDatabase.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot snapShot:dataSnapshot.getChildren()) {

                                num= num-1;
                                if(num==0){

                                    Toast.makeText(Message.this,"ii  "+snapShot.hasChild("msg2"),Toast.LENGTH_SHORT).show();
                                    if(snapShot.hasChild("msg1")){
                                        text=snapShot.child("msg1").getValue(String.class);
                                        Toast.makeText(Message.this,"ii  "+text,Toast.LENGTH_SHORT).show();
                                    }else if(snapShot.hasChild("msg2")){
                                        text=snapShot.child("msg2").getValue(String.class);
                                        Toast.makeText(Message.this,"iii  "+text,Toast.LENGTH_SHORT).show();
                                    }
                                    final String msgt=text;
                                    final String pict=pic;
                                    final String namet=name;
                                    //final
                                    try{
                                        Toast.makeText(Message.this,"iiii  "+text,Toast.LENGTH_SHORT).show();
                                        Message_model model=new Message_model(pict,msgt,namet);
                                        data.add(model);
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                    break;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        adapter=new Messageadapter(data);
        rview.setAdapter(adapter);
    }

 /*   @Override
    protected void onStart() {
        super.onStart();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    key=snapshot.getKey();
                    Toast.makeText(Message.this,key,Toast.LENGTH_SHORT).show();
                    // newmessage=FirebaseDatabase.getInstance().getReference().child("Userdetail").child(key).child("photo");
                    newmessage.child(key).child("photo").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            pic= dataSnapshot.getValue(String.class);
                         //   Toast.makeText(Message.this,"ii  "+pic,Toast.LENGTH_LONG).show();
                            //    text.setText(name);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    num=snapshot.getChildrenCount();

                    mDatabase.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot snapShot:dataSnapshot.getChildren()) {

                                num= num-1;
                                if(num==0){

                                    Toast.makeText(Message.this,"ii  "+snapShot.hasChild("msg2"),Toast.LENGTH_SHORT).show();
                                    if(snapShot.hasChild("msg1")){
                                        text=snapShot.child("msg1").getValue(String.class);
                                        Toast.makeText(Message.this,"ii  "+text,Toast.LENGTH_SHORT).show();
                                    }else if(snapShot.hasChild("msg2")){
                                        text=snapShot.child("msg2").getValue(String.class);
                                        Toast.makeText(Message.this,"iii  "+text,Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    try{
                        data.add(new Message_model(pic,text));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    /*    mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                  key=dataSnapshot.getKey();
                    Toast.makeText(Message.this,key,Toast.LENGTH_SHORT).show();
                 // newmessage=FirebaseDatabase.getInstance().getReference().child("Userdetail").child(key).child("photo");
                    newmessage.child(key).child("photo").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            pic= dataSnapshot.getValue(String.class);
                        //    text.setText(name);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    num=dataSnapshot.getChildrenCount();
                    mDatabase.child(key).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            for(DataSnapshot snapShot:dataSnapshot.getChildren()) {
                               num= num-1;
                               if(num==1){
                                   if(snapShot.hasChild("msg1")){
                                       text=snapShot.child("msg1").getValue(String.class);
                                   }else if(snapShot.hasChild("msg2")){
                                       text=snapShot.child("msg2").getValue(String.class);
                                   }
                                   break;
                               }
                            }

                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                try{
                    data.add(new Message_model(pic,text));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }*/

    private void setupTabIconsBottom() {
        tablayoutbottom.addTab(tablayoutbottom.newTab().setIcon(R.drawable.home));
        tablayoutbottom.addTab(tablayoutbottom.newTab().setIcon(R.drawable.myaccount));
        tablayoutbottom.addTab(tablayoutbottom.newTab().setIcon(R.drawable.notifications));
        tablayoutbottom.addTab(tablayoutbottom.newTab().setIcon(R.drawable.message),true);
     //   tablayoutbottom.addTab(tablayoutbottom.newTab().setIcon(R.drawable.chati));
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
                startActivity(new Intent(Message.this,Tabs.class));
                finish();
                break;
            case 1 :
                startActivity(new Intent(Message.this,Profile.class));
                finish();
                //replaceFragment(new Profile());
                break;
            case 2 :
                startActivity(new Intent(Message.this,Notification.class));
                finish();
                // replaceFragment(new Message());
                break;
            case 3:
               // startActivity(new Intent(Message.this,Message.class));
                //replaceFragment(new Notification());
                break;
           /* case 4:
                startActivity(new Intent(Message.this,Chat.class));
                finish();
                break;*/
            case 5:
                startActivity(new Intent(Message.this,Settings.class));
                finish();
                // replaceFragment(new Settings());
                break;
        }
    }
}
