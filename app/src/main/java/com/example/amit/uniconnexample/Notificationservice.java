package com.example.amit.uniconnexample;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;

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
    private DatabaseReference mDatabasenotif;
    FirebaseUser user;
    int m=0,flag=0;
    @Override
    public void onCreate() {
        super.onCreate();
        user= FirebaseAuth.getInstance().getCurrentUser();
        mDatabasenotif= FirebaseDatabase.getInstance().getReference().child("notification").child("like");
        if(isAppIsInBackground(getBaseContext())){
            mDatabasenotif.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
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
                                    notification(++m,snapshot.getRef(),snapshot);
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
            });
        }
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

    public void notification(int m,DatabaseReference notify,DataSnapshot snapshot){
        //  song= MediaPlayer.create(this,R.raw.internetfriends0);
        //  song.start();
        //int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);

        NotificationManager notificationManager = (NotificationManager)
                this.getSystemService(this.NOTIFICATION_SERVICE);
        android.app.Notification n= new android.app.Notification.Builder(this).setContentTitle("UniConn Notification")
                .setContentText(snapshot.getValue(String.class)+" liked your post")
                .setSmallIcon(R.drawable.uniconn).setAutoCancel(true).build();
        notificationManager.notify(m,n);
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
}
