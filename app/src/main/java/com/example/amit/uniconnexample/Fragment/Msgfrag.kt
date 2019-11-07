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
import com.example.amit.uniconnexample.Message_model
import com.example.amit.uniconnexample.Activity.NewTabActivity
import com.example.amit.uniconnexample.R
import com.example.amit.uniconnexample.utils.Utils
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_msg_frag.view.*

/**
 * Created by amit on 18/2/17.
 */

class Msgfrag : Fragment() {
    private var newsnd: DatabaseReference? = null
    private var auth: FirebaseAuth? = null
    lateinit var msgView: View
    private val isNetworkConnected: Boolean
        get() {
            val cm = activity!!.getSystemService(
                    Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return cm.activeNetworkInfo != null
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        msgView = inflater.inflate(R.layout.activity_msg_frag, container, false)
        auth = FirebaseAuth.getInstance()
        msgView.mchat_list.layoutManager = LinearLayoutManager(activity)
        newsnd = FirebaseDatabase.getInstance().reference.child("Smessage").child(auth!!.currentUser!!.uid)
        newsnd!!.keepSynced(true)
        msgView.refresh.setOnRefreshListener { refresh() }
        // if(isNetworkConnected()) {
        val firebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<Message_model, MessageViewHolder>(
                Message_model::class.java,
                R.layout.activity_messageitem,
                MessageViewHolder::class.java,
                newsnd
        ) {
            override fun populateViewHolder(viewHolder: MessageViewHolder, model: Message_model, position: Int) {
                val msg_key = getRef(position).key
                //    Toast.makeText(Message.this,msg_key,Toast.LENGTH_LONG).show();
                // mDatabase.add
                viewHolder.bindData(model)
                viewHolder.view.setOnClickListener {
                    if (isNetworkConnected) {
                        val i = Intent(activity, Chatstart::class.java)
                        i.putExtra("chat", msg_key)
                        startActivity(i)
                        // getActivity(). finish();
                    } else {
                        Toast.makeText(activity, "No internet connection", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
        msgView.mchat_list.adapter = firebaseRecyclerAdapter
        return view
    }

    fun refresh() {
        val firebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<Message_model, MessageViewHolder>(
                Message_model::class.java,
                R.layout.activity_messageitem,
                MessageViewHolder::class.java,
                newsnd
        ) {
            override fun populateViewHolder(viewHolder: MessageViewHolder, model: Message_model, position: Int) {
                val msg_key = getRef(position).key
                //    Toast.makeText(Message.this,msg_key,Toast.LENGTH_LONG).show();
                // mDatabase.add
                viewHolder.bindData(model)
                viewHolder.view.setOnClickListener {
                    if (isNetworkConnected) {
                        val i = Intent(activity, Chatstart::class.java)
                        i.putExtra("chat", msg_key)
                        startActivity(i)
                        //  getActivity().finish();
                    } else {
                        Toast.makeText(activity, "No internet connection", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
        msgView.mchat_list.adapter = firebaseRecyclerAdapter
        refreshcomplete()
    }

    fun refreshcomplete() {
        msgView.refresh.isRefreshing = false
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        (activity as NewTabActivity).setTitle("    Message     ")
    }

    class MessageViewHolder(internal var view: View) : RecyclerView.ViewHolder(view) {
        fun bindData(model: Message_model) {
            val tmsg = view.findViewById<View>(R.id.txt) as TextView
            val tname = view.findViewById<View>(R.id.txtname) as TextView
            val iview = view.findViewById<View>(R.id.photo) as ImageView
            tmsg.setText(model.msg)
            tname.setText(model.name)
            if (model.image != null)
                iview.setImageBitmap(Utils.decodeBase64(model.image?:""))
            else {
            }
        }
    }
}
