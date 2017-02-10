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
import android.media.MediaPlayer;
import android.media.audiofx.BassBoost;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * Created by amit on 27/11/16.
 */

public class Tabs extends AppCompatActivity {
    private TabLayout tabLayout,tablayoutbottom;
    private ViewPager viewPager;
    private CoordinatorLayout mainFrame;
    Likemodel likemodel;
    MediaPlayer song;
    SharedPreferences myPrefs;
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
        setContentView(R.layout.activity_tabs);
       // FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        user=FirebaseAuth.getInstance().getCurrentUser();
        tabLayout=(TabLayout)findViewById(R.id.tabLayout);
        likemodel=new Likemodel();
        settings=new Settings();
        editor1=getSharedPreferences("com.example.amit.uniconnexample",MODE_PRIVATE).edit();
       // startActivityForResult((new Intent(this, Settings.class)),2);

       // myPrefs=getSharedPreferences("com.example.amit.uniconnexample",MODE_PRIVATE);
      //  tablayoutbottom=(TabLayout)findViewById(R.id.tabLayoutbottom);
        mainFrame=(CoordinatorLayout)findViewById(R.id.coordinatorLayout);
        viewPager=(ViewPager)findViewById(R.id.viewPager);
        mDatabasenotiflike=FirebaseDatabase.getInstance().getReference().child("notificationdata").child("like");
        BottomBar bottomBar=(BottomBar)findViewById(R.id.bottomtab);
         bottomBarTab=bottomBar.getTabWithId(R.id.tab_notification);
         bottomBarTabmsg=bottomBar.getTabWithId(R.id.tab_message);
     //   startService(new Intent(this,Notificationservice.class));
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                      if(tabId==R.id.tab_home){

                      }
               else if(tabId==R.id.tab_account){
                    startActivity(new Intent(Tabs.this,Profile.class));
                }
               else if(tabId==R.id.tab_notification){
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
                          flag=0;
                      //    mDatanotiflike.setValue(new Likemodel(0));
                    startActivity(new Intent(Tabs.this,Notification.class));
                }
               else if(tabId==R.id.tab_message){
                          newnotifchat.removeEventListener(valueEventListener);
                          mDatabasenotif.child(user.getUid()).removeEventListener(valueventlistener);
                          bottomBarTabmsg.removeBadge();
                          msgcount=0;
                    startActivity(new Intent(Tabs.this,Message.class));
                }
               else if(tabId==R.id.tab_setting){
                    startActivity(new Intent(Tabs.this,Settings.class));
                }
            }
        });
        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                if(tabId==R.id.tab_home){

                }
               else if(tabId==R.id.tab_account){
                    startActivity(new Intent(Tabs.this,Profile.class));
                }
              else  if(tabId==R.id.tab_notification){
                    startActivity(new Intent(Tabs.this,Notification.class));
                }
               else if(tabId==R.id.tab_message){
                    newnotifchat.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            newnotifchat.removeEventListener(this);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    startActivity(new Intent(Tabs.this,Message.class));
                }
                else if(tabId==R.id.tab_setting){
                    startActivity(new Intent(Tabs.this,Settings.class));
                }
            }
        });
        setupViewPager(viewPager);
        newnotifchat=FirebaseDatabase.getInstance().getReference().child("notificationdata").child("chat").child(user.getUid());
        mDatabasenotif= FirebaseDatabase.getInstance().getReference().child("notification").child("like");
        mDatanotiflike=FirebaseDatabase.getInstance().getReference().child("notificationdata").child("like").child(user.getUid());
        newnotifchat.keepSynced(true);
        mDatabasenotif.keepSynced(true);
        mDatanotiflike.keepSynced(true);
        mDatabasenotiflike.keepSynced(true);
        tabLayout.setupWithViewPager(viewPager);
       // setupTabIconsBottom();
        setupTabIcons();
      //  bindWidgetsWithAnEvent();

      /*  mDatanotiflike.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
              //  count=dataSnapshot.child("count").getValue(Integer.class);
                //  if(count!=0){
                // Toast.makeText(Tabs.this,"Right",Toast.LENGTH_LONG).show();
             //   bottomBarTab.setBadgeCount(count);
                 mDatanotiflike.removeEventListener(this);
                //  }else{
                //   bottomBarTab.removeBadge();
                //  }
                //   Toast.makeText(Tabs.this,count+"Right",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
       /* mDatabasenotif.child(user.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                notification();

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
        });*/

    }

    @Override
    protected void onPause() {
        super.onPause();
       //Toast.makeText(Tabs.this, "checkpause", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onStart() {
        super.onStart();
        stopService(new Intent(Tabs.this,Notificationservice.class));
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
                   Toast.makeText(Tabs.this,"No internet connection",Toast.LENGTH_LONG).show();
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
        Toast.makeText(Tabs.this, "checkstop", Toast.LENGTH_SHORT).show();
        startService(new Intent(Tabs.this,Notificationservice.class));
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

    /*private void setupTabIconsBottom() {
        tablayoutbottom.addTab(tablayoutbottom.newTab().setIcon(R.drawable.home),true);
        tablayoutbottom.addTab(tablayoutbottom.newTab().setIcon(R.drawable.myaccount));
        tablayoutbottom.addTab(tablayoutbottom.newTab().setIcon(R.drawable.notifications));
        tablayoutbottom.addTab(tablayoutbottom.newTab().setIcon(R.drawable.message));
       // tablayoutbottom.addTab(tablayoutbottom.newTab().setIcon(R.drawable.chati));
        tablayoutbottom.addTab(tablayoutbottom.newTab().setIcon(R.drawable.settings));
       /* TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.tabtext, null);
        tabOne.setText("My Profile");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.myaccount, 0, 0);
        tablayoutbottom.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.tabtext, null);
        tabTwo.setText("Notification");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.notifications, 0, 0);
        tablayoutbottom.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.tabtext, null);
        tabThree.setText("Blackboard");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.message, 0, 0);
        tablayoutbottom.getTabAt(2).setCustomView(tabThree);

        TextView tabFour = (TextView) LayoutInflater.from(this).inflate(R.layout.tabtext, null);
        tabFour.setText("Settings");
        tabFour.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.settings, 0, 0);
        tablayoutbottom.getTabAt(3).setCustomView(tabFour);*/
        //  bottomtab();
    /*}

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
               // startActivity(new Intent(Tabs.this,Tabs.class));
                break;
            case 1 :
                startActivity(new Intent(Tabs.this,Profile.class));
                //finish();
                //replaceFragment(new Profile());
                break;
            case 2 :
                startActivity(new Intent(Tabs.this,Notification.class));
                //finish();
               // replaceFragment(new Message());
                break;
            case 3:
                startActivity(new Intent(Tabs.this,Message.class));
              //  finish();
                //replaceFragment(new Notification());
                break;
           /* case 4:
                startActivity(new Intent(Tabs.this,Chat.class));
                break;*/
        /*    case 4:
                startActivity(new Intent(Tabs.this,Settings.class));
               // finish();
               // replaceFragment(new Settings());
                break;
        }
    }*/

    public void replaceFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
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
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Drawable drawable = item.getIcon();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }
        if(item.getItemId()==R.id.signout){
           /* FirebaseAuth.getInstance().signOut();
            Toast.makeText(Tabs.this, "Logging out..", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Tabs.this, Loginactivity.class));
            finish();*/
            AlertDialog.Builder d = new AlertDialog.Builder(this);
            d.setMessage("Are you sure ?").
                    setCancelable(false).
                    setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(isNetworkConnected()) {
                                stopService(new Intent(Tabs.this, Notificationservice.class));
                                newnotifchat.removeEventListener(valueEventListener);
                                mDatabasenotif.child(user.getUid()).removeEventListener(valueventlistener);
                                handler1.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        FirebaseAuth.getInstance().signOut();
                                        Toast.makeText(Tabs.this, "Logging out..", Toast.LENGTH_SHORT).show();
                                        // myPrefs.edit().clear().commit();
                                        editor1.putBoolean("isLoggedin", false);
                                        editor1.commit();
                                        startActivity(new Intent(Tabs.this, Loginactivity.class));
                                        finish();
                                    }
                                }, 2000);

                            }else{
                                Toast.makeText(Tabs.this,"No internet connection",Toast.LENGTH_LONG).show();
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
        return super.onOptionsItemSelected(item);
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(
                Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
/*

 */