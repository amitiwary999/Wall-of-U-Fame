package com.example.amit.uniconnexample.Others

import android.app.ActivityManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder

import com.example.amit.uniconnexample.App
import com.example.amit.uniconnexample.Fragment.Msgfrag
import com.example.amit.uniconnexample.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * Created by amit on 24/1/17.
 */

class Notificationservice : Service() {
    private var mDatabasenotif: DatabaseReference? = null
    private var newnotifchat: DatabaseReference? = null
    internal var user: FirebaseUser? = null
    internal var valueEventListener: ValueEventListener
    internal var valueeventListener: ValueEventListener
    internal var switchflag: Boolean? = null
    internal var switchvibrate: Boolean? = null
    internal var m = 0
    internal var flag = 0
    internal var m1 = 2000
    override fun onCreate() {
        super.onCreate()
        user = FirebaseAuth.getInstance().currentUser
        switchflag = (this.application as App).getFlag()
        switchvibrate = (this.application as App).getVib()
        mDatabasenotif = FirebaseDatabase.getInstance().reference.child("notification").child("like")
        newnotifchat = FirebaseDatabase.getInstance().reference.child("notificationdata").child("chat")

    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        if (!Foreground.get().isForeground) {
            valueEventListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.children) {
                        notifiy(++m1, snapshot.ref, snapshot, switchflag!!, switchvibrate)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            }
            valueeventListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.children) {
                        // handler1.postDelayed(new Runnable() {
                        //   @Override
                        //   public void run() {
                        snapshot.ref.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                for (snapshot in dataSnapshot.children) {
                                    notification(++m, snapshot.ref, snapshot, switchflag!!, switchvibrate)
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {

                            }
                        })

                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            }
            newnotifchat!!.child(user!!.uid).addValueEventListener(valueEventListener)
            mDatabasenotif!!.child(user!!.uid).addValueEventListener(valueeventListener)
        }
        return Service.START_REDELIVER_INTENT
    }

    override fun onBind(intent: Intent): IBinder? {

        return null
    }

    fun notification(m: Int, notify: DatabaseReference, snapshot: DataSnapshot, switchflag: Boolean, switchvibrate: Boolean?) {
        //  song= MediaPlayer.create(this,R.raw.internetfriends0);
        //  song.start();
        //int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);

        val notificationManager = this.getSystemService(this.NOTIFICATION_SERVICE) as NotificationManager
        val n = NotificationCompat.Builder(this)
                .setContentTitle("UniConn Notification")
                .setContentText(snapshot.getValue(String::class.java) + " liked your post")
                .setSmallIcon(R.drawable.uniconn)
                .setAutoCancel(true)
        if (switchflag) {
            n.setSound(Uri.parse("android.resource://com.example.amit.uniconnexample/" + R.raw.notifsound))
        }
        if (switchvibrate!!) {
            n.setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
        }

        notificationManager.notify(m, n.build())
        ++flag
        notify.setValue(null)
    }

    fun notifiy(m: Int, ref: DatabaseReference, snapshot: DataSnapshot, switchflag: Boolean, switchvibrate: Boolean?) {
        val name = snapshot.child("name").getValue(String::class.java)
        val text = snapshot.child("txt").getValue(String::class.java)
        val n = NotificationCompat.Builder(this)
                .setContentTitle("Unread Message")
                .setContentText("$name : $text")
                .setSmallIcon(R.drawable.uniconn)
                .setAutoCancel(true)
        if (switchflag) {
            n.setSound(Uri.parse("android.resource://com.example.amit.uniconnexample/" + R.raw.notifsound))
        }
        if (switchvibrate!!) {
            n.setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
        }
        val intent = Intent(this, Msgfrag::class.java)
        val stackBuilder = TaskStackBuilder.create(this)
        stackBuilder.addParentStack(Msgfrag::class.java)
        stackBuilder.addNextIntent(intent)
        val resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        )
        n.setContentIntent(resultPendingIntent)

        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.notify(m, n.build())
        // ++msgcount;
        ref.setValue(null)
    }

    override fun stopService(name: Intent): Boolean {
        newnotifchat!!.child(user!!.uid).removeEventListener(valueEventListener)
        mDatabasenotif!!.child(user!!.uid).removeEventListener(valueeventListener)
        return super.stopService(name)

    }

    companion object {

        fun isAppIsInBackground(context: Context): Boolean {
            var isInBackground = true
            val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
                val runningProcesses = am.runningAppProcesses
                for (processInfo in runningProcesses) {
                    if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        for (activeProcess in processInfo.pkgList) {
                            if (activeProcess == context.packageName) {
                                isInBackground = false
                            }
                        }
                    }
                }
            } else {
                val taskInfo = am.getRunningTasks(1)
                val componentInfo = taskInfo[0].topActivity
                if (componentInfo.packageName == context.packageName) {
                    isInBackground = false
                }
            }

            return isInBackground
        }
    }
}
