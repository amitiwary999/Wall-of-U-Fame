/*
    This source code is result of my hardwork. If you want to use it, drop me a message.
 */

package com.example.amit.uniconnexample.Activity

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.Toolbar
import android.text.format.DateFormat
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.example.amit.uniconnexample.Chatstartmodel
import com.example.amit.uniconnexample.Message_model
import com.example.amit.uniconnexample.Notifmsgmodel
import com.example.amit.uniconnexample.R
import com.example.amit.uniconnexample.Others.UserData
import com.example.amit.uniconnexample.utils.Utils
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_startchat.*
import kotlinx.android.synthetic.main.chatoolbar.*

import java.util.Calendar

import timber.log.Timber

/**
 * Created by amit on 8/12/16.
 */

class Chatstart : AppCompatActivity() {
    private var mDatabase: DatabaseReference? = null
    private var auth: FirebaseAuth? = null
    internal var user: FirebaseUser? = null
    internal var msg: String  = ""
    internal var name: String = ""
    internal var bundle: Bundle? = null
    lateinit var userData: UserData
    internal var actionbar: ActionBar? = null
    internal var song: MediaPlayer? = null
    private var newMessage: DatabaseReference? = null
    private var newMesage: DatabaseReference? = null
    private var newReply: DatabaseReference? = null
    private var newSend: DatabaseReference? = null
    private var newSnd: DatabaseReference? = null
    private var checkBack: DatabaseReference? = null
    private var pdata: DatabaseReference? = null
    private var newrply: DatabaseReference? = null
    private var newnotifChat: DatabaseReference? = null
    private var newnotifchat: DatabaseReference? = null
    private var mChat: RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startchat)
        auth = FirebaseAuth.getInstance()
        bundle = intent.extras
        val handler = Handler()
        pdata = FirebaseDatabase.getInstance().reference
        userData = UserData()
        actionbar = supportActionBar
        if (bundle!!.getString("chat") != null)
            key = bundle!!.getString("chat")
        title = ""
        mChat = findViewById<View>(R.id.rv_chat_feed) as RecyclerView
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            //     startActivity(new Intent(Chatstart.this,Tabs.class));
            finish()
        }
        //Utils.setUpToolbarBackButton(this, toolbar);
        msg = et_message.text.toString()
        newnotifChat = FirebaseDatabase.getInstance().reference.child("notificationdata").child("chat").child(key!!).child(auth!!.currentUser!!.uid)
        newnotifchat = FirebaseDatabase.getInstance().reference.child("notificationdata").child("chat").child(auth!!.currentUser!!.uid).child(key!!)

        newSnd = FirebaseDatabase.getInstance().reference.child("Smessage").child(auth!!.currentUser!!.uid).child(key!!)
        newrply = FirebaseDatabase.getInstance().reference.child("Smessage").child(key!!).child(auth!!.currentUser!!.uid)
        checkBack = FirebaseDatabase.getInstance().reference.child("message")
        //  actionbar.setDisplayHomeAsUpEnabled(true);
        // Utils.setUpToolbarBackButton(Chatstart.this,toolbar);
        newMesage = FirebaseDatabase.getInstance().reference.child("Userdetail").child(key!!).child("photo")
        newMesage!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                spic = dataSnapshot.getValue(String::class.java)
                src.setImageBitmap(Utils.decodeBase64(dataSnapshot.getValue(String::class.java)))
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

        pdata!!.child("Userdetail").child(auth!!.currentUser!!.uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                userData = dataSnapshot.getValue<UserData>(UserData::class.java)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Timber.d("oncancelled")
            }
        })
        newMessage = FirebaseDatabase.getInstance().reference.child("Userdetail").child(key!!).child("name")
        newMessage!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                name = dataSnapshot.getValue(String::class.java)
                text.text = name

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

        newReply = FirebaseDatabase.getInstance().reference.child("message").child(key!!).child(auth!!.currentUser!!.uid)
        newSend = FirebaseDatabase.getInstance().reference.child("message").child(auth!!.currentUser!!.uid).child(key!!)
        mDatabase = FirebaseDatabase.getInstance().reference.child("message")
        newReply!!.keepSynced(true)
        mDatabase!!.keepSynced(true)
        newnotifchat!!.keepSynced(true)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.stackFromEnd = true
        mChat!!.layoutManager = linearLayoutManager
        mChat!!.setHasFixedSize(false)
        mChat!!.addOnLayoutChangeListener { view, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            if (bottom < oldBottom) {
                mChat!!.postDelayed({
                    var scrollTo = mChat!!.adapter!!.itemCount - 1
                    scrollTo = if (scrollTo >= 0) scrollTo else 0
                    mChat!!.scrollToPosition(scrollTo)
                }, 10)
            }
        }

        btnSend.setOnClickListener {
            val msge = et_message.text.toString()
            //  Toast.makeText(Chatstart.this,msge,Toast.LENGTH_LONG).show();
            if (msge.length != 0) {
                val newMessage = mDatabase!!.child(auth!!.currentUser!!.uid).child(key!!).push()
                newMessage.child("msg1").setValue(msge)
                newMessage.child("time1").setValue(currentTime)
                val newMesage = mDatabase!!.child(key!!).child(auth!!.currentUser!!.uid).push()
                newMesage.child("msg2").setValue(msge)
                newMesage.child("time2").setValue(currentTime)
                et_message.setText("")
                handler.postDelayed({
                    newnotifChat!!.setValue(Notifmsgmodel(msge, userData.name?:""))
                    newSnd!!.setValue(Message_model(spic?:"", msge, name, key?:""))
                    newrply!!.setValue(Message_model(userData.photo?:"", msge, userData.name?:"", auth!!.currentUser!!.uid))
                }, 200)

                computeothermessage()
            }
        }

    }

    override fun onStart() {
        super.onStart()
        newnotifchat!!.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String) {
                dataSnapshot.ref.setValue(null)
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String) {

            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {

            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String) {

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
        computeothermessage()
    }

    override fun onResume() {
        super.onResume()
        window.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        )
    }

    fun computeothermessage() {
        newnotifchat!!.setValue(null)
        val firebaserecycleradapter = object : FirebaseRecyclerAdapter<Chatstartmodel, Chatstartviewholder>(
                Chatstartmodel::class.java,
                R.layout.activity_chating_item,
                Chatstartviewholder::class.java,
                newReply
        ) {
            override fun populateViewHolder(viewHolder: Chatstartviewholder, model: Chatstartmodel, position: Int) {
                //  viewHolder.bindData(model);
                // if(model.getMsg1().length()!=0)
                newnotifchat!!.setValue(null)
                viewHolder.setMsg1(model.msg1)
                viewHolder.setTime1(model.time1)
                //   if(model.getMsg2().length()!=0)
                viewHolder.setMsg2(model.msg2)
                viewHolder.setTime2(model.time2)
            }


        }
        mChat!!.adapter = firebaserecycleradapter
    }

    class Chatreceiverholder(internal var mView: View) : RecyclerView.ViewHolder(mView) {
        fun setMsg1(msg: String) {
            val rMessage = mView.findViewById<View>(R.id.rMessage) as TextView
            rMessage.text = msg
        }
    }

    class Chatstartviewholder(internal var mView: View) : RecyclerView.ViewHolder(mView) {
        internal var newnotifchat: DatabaseReference
        internal var auth: FirebaseAuth

        init {
            auth = FirebaseAuth.getInstance()
            newnotifchat = FirebaseDatabase.getInstance().reference.child("notificationdata").child("chat").child(auth.currentUser!!.uid).child(key!!)

            //   newReply=FirebaseDatabase.getInstance().getReference().child("message").child(" ").child(auth.getCurrentUser().getUid());
        }

        fun setMsg1(msg: String?) {
            val sMessage = mView.findViewById<View>(R.id.rMessage) as TextView
            val receiver = mView.findViewById<View>(R.id.receiver) as LinearLayout
            receiver.visibility = View.GONE
            sMessage.visibility = View.GONE
            if (msg != null)
                receiver.visibility = View.VISIBLE
            sMessage.visibility = View.VISIBLE
            newnotifchat.setValue(null)
            sMessage.text = msg
        }

        fun setTime1(time: String?) {
            val time1 = mView.findViewById<View>(R.id.rTime) as TextView
            time1.visibility = View.GONE
            if (time != null) {
                time1.visibility = View.VISIBLE
                time1.text = time
            }
        }

        fun setMsg2(msg: String?) {
            val sMessage = mView.findViewById<View>(R.id.sMessage) as TextView
            val sender = mView.findViewById<View>(R.id.sender) as LinearLayout
            sender.visibility = View.GONE
            sMessage.visibility = View.GONE
            if (msg != null)
                sender.visibility = View.VISIBLE
            sMessage.visibility = View.VISIBLE
            newnotifchat.setValue(null)
            sMessage.text = msg
        }

        fun setTime2(time: String?) {
            val time1 = mView.findViewById<View>(R.id.sTime) as TextView
            time1.visibility = View.GONE
            if (time != null) {
                time1.visibility = View.VISIBLE
                time1.text = time
            }
        }
    }

    private inner class computeThread : Thread() {
        override fun run() {
            computeothermessage()
        }
    }

    companion object {
        var key: String? = null
        var spic: String ?= null
        var txt: String? = null
        val currentTime: String
            get() {
                val delegate = "hh:mm aaa"
                return DateFormat.format(delegate, Calendar.getInstance().time) as String
            }
    }


}
