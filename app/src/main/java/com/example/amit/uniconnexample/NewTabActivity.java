package com.example.amit.uniconnexample;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amit.uniconnexample.Fragment.Mainfrag;
import com.example.amit.uniconnexample.Fragment.Msgfrag;
import com.example.amit.uniconnexample.Fragment.Notifrag;
import com.example.amit.uniconnexample.Fragment.Profilefrag;
import com.example.amit.uniconnexample.Fragment.Settingfrag;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * Created by amit on 19/2/17.
 */

public class NewTabActivity extends AppCompatActivity {
    ViewPager viewPager;
    Toolbar toolbar;
  //  BottomBarTab bottomBarTab,bottomBarTabmsg;
    BottomBar bottomBar;
    TextView title;
    ImageView img;
    TabLayout tabLayout;
    SharedPreferences.Editor editor1;
    Handler handler1 = new Handler();
    Handler handler2=new Handler();
    private DatabaseReference mDatabasenotif,mDatanotiflike,newnotifchat,mDatabasenotiflike;
    FirebaseUser user;
    ValueEventListener valueEventListener,valueventlistener;
    Settings settings;
    Boolean switchflag,switchvibrate;
    BottomBarTab bottomBarTab,bottomBarTabmsg;
    Uri alarmsound;
    boolean doubleBackToExitPressedOnce = false;
    int m=0,m1=500,count,flag=0;
    int msgcount=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tab);
        title=(TextView)findViewById(R.id.title);
        img=(ImageView)findViewById(R.id.img);
        user= FirebaseAuth.getInstance().getCurrentUser();
        editor1=getSharedPreferences("com.example.amit.uniconnexample",MODE_PRIVATE).edit();
        viewPager=(ViewPager)findViewById(R.id.viewPager);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        bottomBar=(BottomBar)findViewById(R.id.bottomtab);
     //   tabLayout=(TabLayout)findViewById(R.id.tabLayout);
        bottomBarTab=bottomBar.getTabWithId(R.id.tab_notification);
        bottomBarTabmsg=bottomBar.getTabWithId(R.id.tab_message);
        mDatabasenotiflike= FirebaseDatabase.getInstance().getReference().child("notificationdata").child("like");
        newnotifchat=FirebaseDatabase.getInstance().getReference().child("notificationdata").child("chat").child(user.getUid());
        mDatabasenotif= FirebaseDatabase.getInstance().getReference().child("notification").child("like");
        mDatanotiflike=FirebaseDatabase.getInstance().getReference().child("notificationdata").child("like").child(user.getUid());
        newnotifchat.keepSynced(true);
        mDatabasenotif.keepSynced(true);
        mDatanotiflike.keepSynced(true);
        mDatabasenotiflike.keepSynced(true);
      //  tabLayout.setupWithViewPager(viewPager);
      //  setupViewPager(viewPager);
        setSupportActionBar(toolbar);
      //  setupTabIcons();
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               signout(v);
            }
        });
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if(tabId==R.id.tab_home){
                    title.setText("    Home        ");
                    attachFragment(new Mainfrag());
                 //  viewPager.setVisibility(View.VISIBLE);
                }
                else if(tabId==R.id.tab_account){
                  //  viewPager.setVisibility(View.GONE);
                    title.setText("    Profile     ");
                    attachFragment(new Profilefrag());
                }
                else if(tabId==R.id.tab_notification){
                    if(isNetworkConnected()) {
                        newnotifchat.removeEventListener(valueEventListener);
                        mDatabasenotif.child(user.getUid()).removeEventListener(valueventlistener);
                       /*   mDatabasenotiflike.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                              @Override
                              public void onDataChange(DataSnapshot dataSnapshot) {
                                  mDatabasenotiflike.removeEventListener(this);
                              }

                              @Override
                              public void onCancelled(DatabaseError databaseError) {

                              }
                          });
                          mDatabasenotiflike.child(user.getUid()).setValue(0);*/
                        flag = 0;
                    }
                    //    mDatanotiflike.setValue(new Likemodel(0));
                  //  viewPager.setVisibility(View.GONE);
                    title.setText("    Notification");
                    attachFragment(new Notifrag());
                }
                else if(tabId==R.id.tab_message){
                    if(isNetworkConnected()) {
                        newnotifchat.removeEventListener(valueEventListener);
                        mDatabasenotif.child(user.getUid()).removeEventListener(valueventlistener);
                        bottomBarTabmsg.removeBadge();
                        msgcount = 0;
                    }
                  //  viewPager.setVisibility(View.GONE);
                    title.setText("    Message     ");
                    attachFragment(new Msgfrag());
                }
                else if(tabId==R.id.tab_setting){
                  //  viewPager.setVisibility(View.GONE);
                    title.setText("    Setting     ");
                    attachFragment(new Settingfrag());
                }
            }
        });
      //  bottomBarTab.se
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Toast.makeText(Tabs.this, "checkpause", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(isNetworkConnected()) {
            stopService(new Intent(NewTabActivity.this, Notificationservice.class));
        }
        //startService(new Intent(this,Notificationservice.class));
        switchflag=((App)this.getApplication()).getFlag();
        switchvibrate=((App)this.getApplication()).getVib();
        // Toast.makeText(Tabs.this,"hi"+switchflag,Toast.LENGTH_LONG).show();

       /* handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                bottomBarTab.setBadgeCount(flag);
                bottomBarTabmsg.setBadgeCount(msgcount);
            }
        },2000);*/
        // new Thread(){
        // public void run(){

        if(isNetworkConnected()) {
            valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        //  Toast.makeText(Tabs.this,"Tabs",Toast.LENGTH_LONG).show();
                        notifiy(++m1, snapshot.getRef(), snapshot, switchflag, switchvibrate);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            newnotifchat.addValueEventListener(valueEventListener);
                             /*  (new ValueEventListener() {
                                                               @Override
                                                               public void onDataChange(DataSnapshot dataSnapshot) {
                                                                   //  msgcount=(int)dataSnapshot.getChildrenCount();

                                                               }

                                                               @Override
                                                               public void onCancelled(DatabaseError databaseError) {

                                                               }
                                                           }

                );*/
            valueventlistener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // handler1.postDelayed(new Runnable() {
                        //   @Override
                        //   public void run() {
                        snapshot.getRef().addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                      /*  handler1.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                notification(++m,snapshot.getRef());
                                              //  snapshot.getRef().setValue(null);
                                                Toast.makeText(Tabs.this, snapshot.getRef().getKey(),Toast.LENGTH_LONG).show();
                                            }
                                        }, 2000);*/
                                    notification(++m, snapshot.getRef(), snapshot, switchflag, switchvibrate);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        // }
                        // },3000);

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mDatabasenotif.child(user.getUid()).addValueEventListener(valueventlistener);
                       /* (new ValueEventListener() {
                                                  @Override
                                                  public void onDataChange(DataSnapshot dataSnapshot) {
                                                      for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                          // handler1.postDelayed(new Runnable() {
                                                          //   @Override
                                                          //   public void run() {
                                                          snapshot.getRef().addValueEventListener(new ValueEventListener() {
                                                              @Override
                                                              public void onDataChange(DataSnapshot dataSnapshot) {
                                                                  for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                      /*  handler1.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                notification(++m,snapshot.getRef());
                                              //  snapshot.getRef().setValue(null);
                                                Toast.makeText(Tabs.this, snapshot.getRef().getKey(),Toast.LENGTH_LONG).show();
                                            }
                                        }, 2000);
                                                                      notification(++m, snapshot.getRef(), snapshot, switchflag, switchvibrate);
                                                                  }
                                                              }

                                                              @Override
                                                              public void onCancelled(DatabaseError databaseError) {

                                                              }
                                                          });
                                                          // }
                                                          // },3000);

                                                      }
                                                  }

                                                  @Override
                                                  public void onCancelled(DatabaseError databaseError) {

                                                  }
                                              }

                        );*/

            handler1.postDelayed(new Runnable() {
                @Override
                public void run() {
                                                   /*  mDatabasenotiflike.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                                                         @Override
                                                         public void onDataChange(DataSnapshot dataSnapshot) {
                                                             int count=dataSnapshot.child("count").getValue(Integer.class);
                                                             bottomBarTab.setBadgeCount(count);
                                                         }

                                                         @Override
                                                         public void onCancelled(DatabaseError databaseError) {

                                                         }
                                                     });*/
                    bottomBarTab.setBadgeCount(flag);
                    bottomBarTabmsg.setBadgeCount(msgcount);

                }
            }, 1000);
            // }
            //  }.start();

        }else{
            Toast.makeText(NewTabActivity.this,"No internet connection",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Toast.makeText(Tabs.this, "checkstop", Toast.LENGTH_SHORT).show();
        if(isNetworkConnected()) {
            newnotifchat.removeEventListener(valueEventListener);
            mDatabasenotif.child(user.getUid()).removeEventListener(valueventlistener);
        }
       /* newnotifchat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
      /*  mDatabasenotif.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDatabasenotif.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //    Toast.makeText(Tabs.this, "checkstop", Toast.LENGTH_SHORT).show();
        if(isNetworkConnected()) {
            startService(new Intent(NewTabActivity.this, Notificationservice.class));
        }
    }

    public void notification(int m,DatabaseReference notify,DataSnapshot snapshot,Boolean switchflag,Boolean switchvibrate){
        //  song= MediaPlayer.create(this,R.raw.internetfriends0);
        //  song.start();
        //int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        ++flag;
        NotificationManager notificationManager = (NotificationManager)
                this.getSystemService(this.NOTIFICATION_SERVICE);
        NotificationCompat.Builder n=new NotificationCompat.Builder(this)
                .setContentTitle("UniConn Notification")
                .setContentText(snapshot.getValue(String.class)+" liked your post")
                .setSmallIcon(R.drawable.uniconn)
                .setAutoCancel(true);
        if(switchflag){
            n.setSound(Uri.parse("android.resource://com.example.amit.uniconnexample/"+R.raw.notifsound));
        }
        if(switchvibrate){
            n.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
        }
       /* android.app.Notification n= new android.app.Notification.Builder(this).setContentTitle("UniConn Notification")
                .setContentText(snapshot.getValue(String.class)+" liked your post")
                .setSmallIcon(R.drawable.uniconn).setAutoCancel(true).build();*/
        notificationManager.notify(m,n.build());

        notify.setValue(null);
       /* song= MediaPlayer.create(getApplicationContext(),R.raw.internetfriends0);
        song.start();
        NotificationManager notificationManager = (NotificationManager)
                this.getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
        NotificationCompat.Builder n= new NotificationCompat.Builder(this).setContentTitle("Location notifier notice")
                .setContentText(" Just 1 km away from destination")
                .setSmallIcon(R.drawable.no).setAutoCancel(true).build();

        notificationManager.notify(0,n.build());*/
    }
    public void notifiy(int m,DatabaseReference ref,DataSnapshot snapshot,Boolean switchflag,Boolean switchvibrate){
        ++msgcount;
        String name=snapshot.child("name").getValue(String.class);
        String text=snapshot.child("txt").getValue(String.class);
        NotificationCompat.Builder n=new NotificationCompat.Builder(this)
                .setContentTitle("Unread Message")
                .setContentText(name+" : "+text)
                .setSmallIcon(R.drawable.uniconn)
                .setAutoCancel(true);

        if(switchflag){
            n.setSound(Uri.parse("android.resource://com.example.amit.uniconnexample/"+R.raw.notifsound));
        }
        if(switchvibrate){
            n.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
        }
        Intent intent=new Intent(this,Message.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(Message.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        n.setContentIntent(resultPendingIntent);

        // NotificationManager notificationManager=(NotificationManager)this.getSystemService(this.NOTIFICATION_SERVICE);
       /* NotificationCompat.Builder n=new NotificationCompat.Builder(this).setContentTitle("Unread Message")
                .setContentText(name+" : "+text)
                .setSmallIcon(R.drawable.uniconn).setAutoCancel(true).build();*/
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(m,n.build());

        ref.setValue(null);
    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toasty.info(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        // Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      //  getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

      /*  Drawable drawable = item.getIcon();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }
        if(item.getItemId()==R.id.signout){
           /* FirebaseAuth.getInstance().signOut();
            Toast.makeText(Tabs.this, "Logging out..", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Tabs.this, Loginactivity.class));
            finish();
            AlertDialog.Builder d = new AlertDialog.Builder(this);
            d.setMessage("Are you sure ?").
                    setCancelable(false).
                    setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(isNetworkConnected()) {
                                stopService(new Intent(NewTabActivity.this, Notificationservice.class));
                                newnotifchat.removeEventListener(valueEventListener);
                                mDatabasenotif.child(user.getUid()).removeEventListener(valueventlistener);
                                handler1.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        FirebaseAuth.getInstance().signOut();
                                        Toast.makeText(NewTabActivity.this, "Logging out..", Toast.LENGTH_SHORT).show();
                                        // myPrefs.edit().clear().commit();
                                        editor1.putBoolean("isLoggedin", false);
                                        editor1.commit();
                                        startActivity(new Intent(NewTabActivity.this, Loginactivity.class));
                                        finish();
                                    }
                                }, 2000);

                            }else{
                                Toast.makeText(NewTabActivity.this,"No internet connection",Toast.LENGTH_LONG).show();
                            }
                        }
                    }).
                    setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert = d.create();
            alert.setTitle("Logout");
            alert.show();
            Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
            nbutton.setTextColor(Color.BLACK);
            Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
            pbutton.setTextColor(Color.BLACK);
        }*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new MainActivity(), "My Campus");
        adapter.addFrag(new Global(), "Global");
        adapter.addFrag(new Trending(), "Trending");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void setupTabIcons() {
        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.tabtext, null);
        tabOne.setText("My Campus");
        //  tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.myaccount, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.tabtext, null);
        tabTwo.setText("Global");
        // tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.notifications, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.tabtext, null);
        tabThree.setText("Trending");
        //tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.message, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);

        //  bottomtab();
    }
    public void attachFragment(Fragment fm){
        Fragment fragment=fm;
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame,fragment);
        try {
            fragmentTransaction.commitAllowingStateLoss();
        }catch(IllegalStateException e) {
            fragmentTransaction.commit();
        }
    }
    public void signout(View v){
        AlertDialog.Builder d = new AlertDialog.Builder(NewTabActivity.this);
        d.setMessage("Are you sure ?").
                setCancelable(false).
                setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(isNetworkConnected()) {
                            stopService(new Intent(NewTabActivity.this, Notificationservice.class));
                            newnotifchat.removeEventListener(valueEventListener);
                            mDatabasenotif.child(user.getUid()).removeEventListener(valueventlistener);
                            handler1.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    FirebaseAuth.getInstance().signOut();
                                    Toast.makeText(NewTabActivity.this, "Logging out..", Toast.LENGTH_SHORT).show();
                                    // myPrefs.edit().clear().commit();
                                    editor1.putBoolean("isLoggedin", false);
                                    editor1.commit();
                                    startActivity(new Intent(NewTabActivity.this, Loginactivity.class));
                                    finish();
                                }
                            }, 2000);

                        }else{
                            Toast.makeText(NewTabActivity.this,"No internet connection",Toast.LENGTH_LONG).show();
                        }
                    }
                }).
                setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = d.create();
        alert.setTitle("Logout");
        alert.show();
        Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(Color.BLACK);
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(Color.BLACK);
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(
                Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
    public void setTitle(String tooltitle){
        title.setText(tooltitle);
    }
}