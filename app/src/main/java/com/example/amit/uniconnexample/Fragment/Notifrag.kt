package com.example.amit.uniconnexample.Fragment

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import com.example.amit.uniconnexample.Activity.Chatstart
import com.example.amit.uniconnexample.Activity.NewTabActivity
import com.example.amit.uniconnexample.Activity.Notifclick
import com.example.amit.uniconnexample.Notificationmodel
import com.example.amit.uniconnexample.R
import com.example.amit.uniconnexample.utils.Utils
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_notif_frag.view.*

/**
 * Created by amit on 18/2/17.
 */

class Notifrag : Fragment() {
    lateinit var auth: FirebaseAuth
    internal var key: String? = null
    var post_key: String ?= null
    private var mDatabasenotifdata: DatabaseReference? = null
    private var mDatanotiflike: DatabaseReference? = null
    lateinit var notifFragView: View
    private val isNetworkConnected: Boolean
        get() {
            val cm = activity!!.getSystemService(
                    Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return cm.activeNetworkInfo != null
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        notifFragView = inflater.inflate(R.layout.activity_notif_frag, container, false)
        auth = FirebaseAuth.getInstance()
        mDatanotiflike = FirebaseDatabase.getInstance().reference.child("notificationdata").child("like").child(FirebaseAuth.getInstance().currentUser!!.uid)
        //  Toast.makeText(this,"check",Toast.LENGTH_LONG).show();

        mDatabasenotifdata = FirebaseDatabase.getInstance().reference.child("notificationdata").child("data").child(FirebaseAuth.getInstance().currentUser!!.uid)
        // Utils.setUpToolbarBackButton(Notification.this, toolbar);
        val lm = LinearLayoutManager(activity)
        notifFragView.mnotification_list.layoutManager = lm
        mDatanotiflike!!.keepSynced(true)
        mDatabasenotifdata!!.keepSynced(true)
        notifFragView.refresh.setOnRefreshListener { refresh() }
        return notifFragView
    }

    override fun onStart() {
        super.onStart()
        val firebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<Notificationmodel, NotificationViewHolder>(
                Notificationmodel::class.java,
                R.layout.activity_notificationitem,
                NotificationViewHolder::class.java,
                mDatabasenotifdata
        ) {
            override fun populateViewHolder(viewHolder: NotificationViewHolder, model: Notificationmodel, position: Int) {
                viewHolder.view.setOnClickListener {
                    //  mDatanotiflike.child("count").setValue(0);
                }
                viewHolder.iview.setOnClickListener {
                    val key = model.key
                    if (key != auth.currentUser!!.uid) {
                        //   Toast.makeText(getActivity(), key, Toast.LENGTH_LONG).show();
                        val i = Intent(activity, Chatstart::class.java)
                        i.putExtra("chat", key)
                        startActivity(i)
                        // getActivity().finish();
                    } else {
                        Toast.makeText(activity, "You can't chat with yourself", Toast.LENGTH_LONG).show()
                    }
                }
                viewHolder.tname.setOnClickListener {
                    //   android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                    post_key = model.post_key?:""
                    //  fragmentManager.beginTransaction().add(R.id.content_frame,new Notifclickfrag(),tag).commit();
                    //   Notifclickfrag notifclickfrag=new Notifclickfrag();
                    val i = Intent(activity, Notifclick::class.java)
                    i.putExtra("postkey", post_key)
                    startActivity(i)
                }
                viewHolder.bindData(model)

            }
        }
        notifFragView.mnotification_list.adapter = firebaseRecyclerAdapter
    }

    override fun onResume() {
        super.onResume()
        (activity as NewTabActivity).setTitle("    Notification")
    }

    class NotificationViewHolder(internal var view: View) : RecyclerView.ViewHolder(view) {
        internal var iview: ImageView
        internal var tname: TextView
        private val mDatabasenotifdata: DatabaseReference? = null
        private val mDatanotiflike: DatabaseReference

        init {
            tname = view.findViewById<View>(R.id.bname) as TextView
            iview = view.findViewById<View>(R.id.pimage) as ImageView
            mDatanotiflike = FirebaseDatabase.getInstance().reference.child("notificationdata").child("like").child(FirebaseAuth.getInstance().currentUser!!.uid)
        }

        fun bindData(model: Notificationmodel) {
            // TextView tname=(TextView)view.findViewById(R.id.bname);
            // ImageView iview=(ImageView)view.findViewById(R.id.pimage);
            tname.setText(model.txt)
            if (model.img != null) {
                iview.setImageBitmap(Utils.decodeBase64(model.img?:""))
            } else {
                iview.setImageResource(R.drawable.account)
            }
            // mDatanotiflike.child("count").setValue(0);
        }
    }

    fun refresh() {

        val firebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<Notificationmodel, NotificationViewHolder>(
                Notificationmodel::class.java,
                R.layout.activity_notificationitem,
                NotificationViewHolder::class.java,
                mDatabasenotifdata
        ) {
            override fun populateViewHolder(viewHolder: NotificationViewHolder, model: Notificationmodel, position: Int) {
                viewHolder.view.setOnClickListener {
                    //  mDatanotiflike.child("count").setValue(0);
                }
                viewHolder.iview.setOnClickListener {
                    val key = model.key
                    if (key != auth.currentUser!!.uid) {
                        //   Toast.makeText(getActivity(), key, Toast.LENGTH_LONG).show();
                        val i = Intent(activity, Chatstart::class.java)
                        i.putExtra("chat", key)
                        startActivity(i)
                        //   getActivity().finish();
                    } else {
                        Toast.makeText(activity, "You can't chat with yourself", Toast.LENGTH_LONG).show()
                    }
                }
                viewHolder.tname.setOnClickListener {
                    //   android.support.v4.app.FragmentManager fragmentManager = getChildFragmentManager();
                    post_key = model.post_key?:""
                    //  fragmentManager.beginTransaction().add(R.id.content_frame,new Notifclickfrag(),tag).commit();
                    //   Notifclickfrag notifclickfrag=new Notifclickfrag();
                    val i = Intent(activity, Notifclick::class.java)
                    i.putExtra("postkey", post_key)
                    startActivity(i)
                }
                viewHolder.bindData(model)

            }
        }
        notifFragView.mnotification_list.adapter = firebaseRecyclerAdapter
        refreshcomplete()
    }

    fun refreshcomplete() {
        notifFragView.refresh.isRefreshing = false
    }
}
