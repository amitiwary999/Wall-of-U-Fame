package com.example.amit.uniconnexample;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.example.amit.uniconnexample.Fragment.Msgfrag;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by amit on 24/1/17.
 */

public class Notificationservice extends Service {
    private DatabaseReference mDatabasenotif,newnotifchat;
    FirebaseUser user;
    ValueEventListener valueEventListener,valueeventListener;
    Boolean switchflag,switchvibrate;
    int m=0,flag=0,m1=2000;
    @Override
    public void onCreate() {
        super.onCreate();
        user= FirebaseAuth.getInstance().getCurrentUser();
        switchflag=((App)this.getApplication()).getFlag();
        switchvibrate=((App)this.getApplication()).getVib();
        mDatabasenotif= FirebaseDatabase.getInstance().getReference().child("notification").child("like");
        newnotifchat=FirebaseDatabase.getInstance().getReference().child("notificationdata").child("chat");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        
        if(!(Foreground.get().isForeground())){
            valueEventListener=new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        notifiy(++m1,snapshot.getRef(),snapshot,switchflag,switchvibrate);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            valueeventListener=new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for( final DataSnapshot snapshot:dataSnapshot.getChildren()) {
                        // handler1.postDelayed(new Runnable() {
                        //   @Override
                        //   public void run() {
                        snapshot.getRef().addListenerForSingleValueEvent(new ValueEventListener() {
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
                                    notification(++m,snapshot.getRef(),snapshot,switchflag,switchvibrate);
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
            newnotifchat.child(user.getUid()).addValueEventListener(valueEventListener);
            mDatabasenotif.child(user.getUid()).addValueEventListener(valueeventListener);
          /*  newnotifchat.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                   // Toast.makeText(Notificationservice.this,"service",Toast.LENGTH_SHORT).show();
                    //  msgcount=(int)dataSnapshot.getChildrenCount();
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        notifiy(++m1,snapshot.getRef(),snapshot,switchflag,switchvibrate);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });*/

           /* mDatabasenotif.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for( final DataSnapshot snapshot:dataSnapshot.getChildren()) {
                        // handler1.postDelayed(new Runnable() {
                        //   @Override
                        //   public void run() {
                        snapshot.getRef().addListenerForSingleValueEvent(new ValueEventListener() {
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
                                    notification(++m,snapshot.getRef(),snapshot,switchflag,switchvibrate);
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
            });*/
        }
        return START_REDELIVER_INTENT;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    public void notification(int m,DatabaseReference notify,DataSnapshot snapshot,Boolean switchflag,Boolean switchvibrate){
        //  song= MediaPlayer.create(this,R.raw.internetfriends0);
        //  song.start();
        //int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);

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

        notificationManager.notify(m,n.build());
        ++flag;
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
        Intent intent=new Intent(this, Msgfrag.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(Msgfrag.class);
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
       // ++msgcount;
        ref.setValue(null);
    }

    @Override
    public boolean stopService(Intent name) {
        newnotifchat.child(user.getUid()).removeEventListener(valueEventListener);
        mDatabasenotif.child(user.getUid()).removeEventListener(valueeventListener);
        return super.stopService(name);

    }
}
