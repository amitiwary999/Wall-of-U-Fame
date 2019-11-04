package com.example.amit.uniconnexample.Fragment

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import com.bumptech.glide.Glide
import com.example.amit.uniconnexample.Activity.Loginactivity
import com.example.amit.uniconnexample.Activity.Blog
import com.example.amit.uniconnexample.Activity.Chatstart
import com.example.amit.uniconnexample.Likemodel
import com.example.amit.uniconnexample.Model.BlogModel
import com.example.amit.uniconnexample.Notificationmodel
import com.example.amit.uniconnexample.R
import com.example.amit.uniconnexample.Others.UserData
import com.example.amit.uniconnexample.utils.Utils
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.tt.whorlviewlibrary.WhorlView

import java.util.ArrayList

import timber.log.Timber

/**
 * Created by amit on 18/2/17.
 */

class Detailfrag : Fragment() {
    internal var sprfnc: SharedPreferences? = null
    private var mBlogList: RecyclerView? = null
    private var mDatabase: DatabaseReference? = null
    private var pdata: DatabaseReference? = null
    private var mDatabaselike: DatabaseReference? = null
    private var mDatabaseunlike: DatabaseReference? = null
    private var mDatabasenotif: DatabaseReference? = null
    private val mDatabas: DatabaseReference? = null
    private var mDatabasenotiflike: DatabaseReference? = null
    private val newMessage: DatabaseReference? = null
    private var mDatabasenotifdata: DatabaseReference? = null
    private var auth: FirebaseAuth? = null
    internal var user: FirebaseUser? = null
    internal var likemodel: Likemodel
    internal var mal: String? = null
    internal var check: String
    internal var checkmail: String
    internal var userdata: UserData
    internal var whorlView: WhorlView
    internal var fab: FloatingActionButton
    internal var lik: Int = 0
    internal var unlike: Int = 0
    internal var count: Int = 0
    internal var unlik: Int = 0
    internal var lyk: Int = 0
    internal var handler1 = Handler()
    private var processlike: Boolean? = false
    private var processunlike: Boolean? = false
    private var keysArray: ArrayList<String>? = null
    private var mProgress: ProgressDialog? = null
    internal var ref: Query? = null
    internal var refresh: SwipeRefreshLayout
    internal var query: Query
    private val isNetworkConnected: Boolean
        get() {
            val cm = activity!!.getSystemService(
                    Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return cm.activeNetworkInfo != null
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_main_frag, container, false)
        whorlView = view.findViewById<View>(R.id.whorl2) as WhorlView
        keysArray = ArrayList()
        refresh = view.findViewById<View>(R.id.refresh) as SwipeRefreshLayout
        //  whorlView.setVisibility(View.VISIBLE);
        //  whorlView.start();
        likemodel = Likemodel()
        userdata = UserData()

        user = FirebaseAuth.getInstance().currentUser
        mProgress = ProgressDialog(activity)
        pdata = FirebaseDatabase.getInstance().reference
        //        Timber.d(user.name);
        //  mal=user.email;
        auth = FirebaseAuth.getInstance()
        fab = view.findViewById<View>(R.id.fab) as FloatingActionButton
        val n = auth!!.currentUser!!.email
        check = n!!.substring(n.indexOf("@") + 1, n.lastIndexOf("."))
        checkmail = n.substring(n.indexOf("@") + 1, n.lastIndexOf(".")) + n.substring(n.lastIndexOf(".") + 1)
        // mProgress.show();
        //  mDatabase= FirebaseDatabase.getInstance().getReference().child(check);
        mDatabase = FirebaseDatabase.getInstance().reference.child("Posts")
        query = mDatabase!!.orderByChild("date").equalTo(checkmail, "emailflag")
        //ref=mDatabase.orderByChild("date");
        mDatabaselike = FirebaseDatabase.getInstance().reference.child("like")
        mDatabaseunlike = FirebaseDatabase.getInstance().reference.child("unlike")
        mDatabasenotif = FirebaseDatabase.getInstance().reference.child("notification").child("like")
        mDatabasenotifdata = FirebaseDatabase.getInstance().reference.child("notificationdata").child("data")
        mDatabasenotiflike = FirebaseDatabase.getInstance().reference.child("notificationdata").child("like")
        pdata!!.keepSynced(true)
        mDatabase!!.keepSynced(true)
        mDatabaselike!!.keepSynced(true)
        mDatabaselike!!.keepSynced(true)

        mBlogList = view.findViewById<View>(R.id.mblog_list) as RecyclerView
        mBlogList!!.setHasFixedSize(true)
        val lm = LinearLayoutManager(activity)
        //   lm.setReverseLayout(true);
        lm.stackFromEnd = true
        mBlogList!!.layoutManager = lm
        refresh.setOnRefreshListener { refreshitem() }
        mBlogList!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 && fab.isShown)
                    fab.hide()
                if (dy < 0 && fab.isEnabled)
                    fab.show()
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
        fab.setOnClickListener { writeblog() }
        return view
    }

    fun refreshitem() {
        whorlView.visibility = View.VISIBLE
        whorlView.start()

        if (auth!!.currentUser != null) {
            pdata!!.child("Userdetail").child(user!!.uid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    whorlView.stop()
                    userdata = dataSnapshot.getValue<UserData>(UserData::class.java)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Timber.d("oncancelled")
                }
            })
            // new Thread(){
            // public void run(){
            val firebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<BlogModel, Detailfrag.BlogViewHolder>(
                    BlogModel::class.java,
                    R.layout.blog_item,
                    Detailfrag.BlogViewHolder::class.java,
                    mDatabase!!.orderByChild("emailflag").equalTo(checkmail)

            ) {

                override fun populateViewHolder(viewHolder: Detailfrag.BlogViewHolder, model: BlogModel, position: Int) {
                    //  model=new Blogmodel();
                    //  viewHolder.setTitle(model.getTitle());

                    whorlView.visibility = View.GONE

                    val post_key = getRef(position).key
                    viewHolder.bindData(model)
                    viewHolder.setLiked(post_key)
                    viewHolder.setUnliked(post_key)

                    viewHolder.mView.setOnClickListener {
                        //   Toast.makeText(getActivity(),"hi"+post_key,Toast.LENGTH_LONG).show();
                    }

                }
            }
            mBlogList!!.adapter = firebaseRecyclerAdapter

        } else {
            loadLoginView()
        }
        refreshcomplete()
    }

    fun refreshcomplete() {
        refresh.isRefreshing = false
    }

    override fun onStart() {
        super.onStart()
        // if(isNetworkConnected()) {
        whorlView.visibility = View.VISIBLE
        whorlView.start()

        if (auth!!.currentUser != null) {
            pdata!!.child("Userdetail").child(user!!.uid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    whorlView.stop()
                    userdata = dataSnapshot.getValue<UserData>(UserData::class.java)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Timber.d("oncancelled")
                }
            })
            // new Thread(){
            // public void run(){
            val firebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<BlogModel, Detailfrag.BlogViewHolder>(
                    BlogModel::class.java,
                    R.layout.blog_item,
                    Detailfrag.BlogViewHolder::class.java,
                    mDatabase!!.orderByChild("emailflag").equalTo(checkmail)

            ) {

                override fun populateViewHolder(viewHolder: Detailfrag.BlogViewHolder, model: BlogModel, position: Int) {
                    //  model=new Blogmodel();
                    //  viewHolder.setTitle(model.getTitle());

                    whorlView.visibility = View.GONE

                    val post_key = getRef(position).key
                    viewHolder.bindData(model)
                    viewHolder.setLiked(post_key)
                    viewHolder.setUnliked(post_key)

                    viewHolder.mView.setOnClickListener {
                        //   Toast.makeText(getActivity(),"hi"+post_key,Toast.LENGTH_LONG).show();
                    }

                    viewHolder.chat.setOnClickListener {
                        if (isNetworkConnected) {
                            mDatabase!!.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    val key = model.key
                                    if (key != user!!.uid) {
                                        //   Toast.makeText(getActivity(), key, Toast.LENGTH_LONG).show();
                                        val i = Intent(activity, Chatstart::class.java)
                                        i.putExtra("chat", key)
                                        startActivity(i)
                                    } else {
                                        Toast.makeText(activity, "You can't chat with yourself", Toast.LENGTH_LONG).show()
                                    }
                                }

                                override fun onCancelled(databaseError: DatabaseError) {

                                }
                            })
                        } else {
                            Toast.makeText(activity, "No internet connection", Toast.LENGTH_LONG).show()
                        }
                    }

                    viewHolder.lk.setOnClickListener {
                        if (isNetworkConnected) {
                            processlike = true
                            mDatabaselike!!.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    if (processlike!!) {
                                        if (dataSnapshot.child(post_key).hasChild(user!!.uid)) {

                                            // int
                                            if (model.like > 0) {
                                                lik = model.like - 1
                                            } else {
                                                lik = 0
                                            }
                                            mDatabaselike!!.child(post_key).child(user!!.uid).removeValue()
                                            mDatabase!!.child(post_key).child("like").setValue(lik)
                                            processlike = false

                                        } else {
                                            mDatabaseunlike!!.addListenerForSingleValueEvent(object : ValueEventListener {
                                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                                    // Toast.makeText(getActivity(),(String)dataSnapshot.child(post_key).child(user.getUid()).getValue(),Toast.LENGTH_LONG).show();
                                                    if (dataSnapshot.child(post_key).hasChild(user!!.uid)) {
                                                        //   if( mDatabaseunlike.child(post_key).child(user.getUid()).equals("Unliked")){
                                                        // int unlik=model.getUnlike();
                                                        //  Toast.makeText(getActivity(),dataSnapshot.child(post_key).child(user.getUid()).getValue(String.class),Toast.LENGTH_LONG).show();
                                                        if (model.unlike > 0) {
                                                            unlik = model.unlike - 1
                                                        } else {
                                                            unlik = 0
                                                        }
                                                        mDatabaseunlike!!.child(post_key).child(user!!.uid).removeValue()
                                                        mDatabase!!.child(post_key).child("like").setValue(lik)
                                                        mDatabase!!.child(post_key).child("unlike").setValue(unlik)
                                                    }
                                                }

                                                override fun onCancelled(databaseError: DatabaseError) {

                                                }
                                            })
                                            lik = model.like + 1
                                            mDatabaselike!!.child(post_key).child(user!!.uid).setValue("Liked")
                                            mDatabase!!.child(post_key).child("like").setValue(lik)
                                            if (user!!.uid != model.key) {
                                                mDatabasenotif!!.child(model.key).child(post_key).child(user!!.uid).setValue(userdata.name)
                                                // Toast.makeText(getActivity(),model.getKey(),Toast.LENGTH_LONG).show();
                                                val newpost = mDatabasenotifdata!!.child(model.key).push()
                                                newpost.setValue(Notificationmodel(userdata.photo, userdata.name + " liked your post", user!!.uid, post_key))
                                            }
                                            processlike = false
                                        }
                                    }
                                }

                                override fun onCancelled(databaseError: DatabaseError) {

                                }
                            })
                            //  Toast.makeText(getActivity(),"hi"+post_key,Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(activity, "No internet connection", Toast.LENGTH_LONG).show()
                        }
                    }
                    viewHolder.unlk.setOnClickListener {
                        if (isNetworkConnected) {
                            processunlike = true
                            //   unlike=model.getUnlike();
                            mDatabaseunlike!!.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    if (processunlike!!) {
                                        if (dataSnapshot.child(post_key).hasChild(user!!.uid)) {

                                            //  final int
                                            if (model.unlike > 0) {
                                                unlike = model.unlike - 1
                                            } else {
                                                unlike = 0
                                            }
                                            mDatabaseunlike!!.child(post_key).child(user!!.uid).removeValue()
                                            mDatabase!!.child(post_key).child("unlike").setValue(unlike)
                                            processunlike = false
                                        } else {

                                            mDatabaselike!!.addListenerForSingleValueEvent(object : ValueEventListener {
                                                override fun onDataChange(dataSnapshot: DataSnapshot) {

                                                    if (dataSnapshot.child(post_key).hasChild(user!!.uid)) {
                                                        //   if( mDatabaseunlike.child(post_key).child(user.getUid()).equals("Unliked")){

                                                        //   Toast.makeText(getActivity(),(String)dataSnapshot.child(post_key).child(user.getUid()).getValue(),Toast.LENGTH_LONG).show();
                                                        if (model.like > 0) {
                                                            lyk = model.like - 1
                                                        } else {
                                                            lyk = 0
                                                        }
                                                        mDatabaselike!!.child(post_key).child(user!!.uid).removeValue()
                                                        mDatabase!!.child(post_key).child("like").setValue(lyk)
                                                        mDatabase!!.child(post_key).child("unlike").setValue(unlike)
                                                    }
                                                }

                                                override fun onCancelled(databaseError: DatabaseError) {

                                                }
                                            })
                                            mDatabaseunlike!!.child(post_key).child(user!!.uid).setValue("Unliked")
                                            // final int
                                            unlike = model.unlike + 1
                                            mDatabase!!.child(post_key).child("unlike").setValue(unlike)
                                            processunlike = false
                                        }
                                        processunlike = false
                                    }

                                }

                                override fun onCancelled(databaseError: DatabaseError) {

                                }
                            })
                        } else {
                            Toast.makeText(activity, "No internet connection", Toast.LENGTH_LONG).show()
                        }
                    }

                }
            }
            mBlogList!!.adapter = firebaseRecyclerAdapter

        } else {
            loadLoginView()
        }
    }

    fun writeblog() {
        startActivity(Intent(activity, Blog::class.java).putExtra("user", userdata))
    }

    class BlogViewHolder(var mView: View) : RecyclerView.ViewHolder(mView) {
        var lk: ImageButton
        var unlk: ImageButton
        var chat: ImageButton
        private val auth: FirebaseAuth
        internal var mDatabaseLike: DatabaseReference
        internal var mDatabaseunlike: DatabaseReference
        var check: String? = null
        var desc: String? = null
        var pic: String? = null
        var nam: String? = null
        var photo: String? = null
        var time: String
        var date: String
        internal var lke: Int = 0
        internal var unlke: Int = 0
        internal var df: DatabaseReference? = null

        init {
            auth = FirebaseAuth.getInstance()
            //   String n=auth.getCurrentUser().getEmail();
            //   check=n.substring(n.indexOf("@")+1,n.lastIndexOf("."));
            //     df=FirebaseDatabase.getInstance().getReference().child(check);
            mDatabaseLike = FirebaseDatabase.getInstance().reference.child("like")
            mDatabaseunlike = FirebaseDatabase.getInstance().reference.child("unlike")
            lk = mView.findViewById<View>(R.id.like) as ImageButton
            unlk = mView.findViewById<View>(R.id.unlike) as ImageButton
            chat = mView.findViewById<View>(R.id.chat) as ImageButton
        }

        fun bindData(model: BlogModel) {
            val post_desc = mView.findViewById<View>(R.id.post_desc) as TextView
            val post_image = mView.findViewById<View>(R.id.postimage) as ImageView
            val post_name = mView.findViewById<View>(R.id.bname) as TextView
            val pro_pic = mView.findViewById<View>(R.id.pimage) as ImageView
            val txtLike = mView.findViewById<View>(R.id.txtlike) as TextView
            val txtUnlike = mView.findViewById<View>(R.id.txtunlike) as TextView
            //  TextView txtPlace=(TextView)mView.findViewById(R.id.txtPlace);
            val txtTime = mView.findViewById<View>(R.id.txtTime) as TextView
            val txtDate = mView.findViewById<View>(R.id.txtDate) as TextView
            post_desc.text = model.desc
            desc = model.desc
            if (model.image == null) {
                post_image.visibility = View.GONE
                pic = model.image
            } else {
                post_image.visibility = View.VISIBLE
                Glide.with(mView.context).load(model.image).into(post_image)
                pic = model.image
            }
            if (model.name == null) {
                post_name.text = "Anonyms"
                nam = "Anonyms"
            } else {
                post_name.text = model.name
                nam = model.name
            }
            if (model.propic != null) {
                pro_pic.setImageBitmap(Utils.decodeBase64(model.propic))
            } else {
                pro_pic.setImageDrawable(ContextCompat.getDrawable(mView.context, R.drawable.user))
            }
            photo = model.propic
            val likE = Integer.toString(model.like)
            txtLike.text = likE
            val txtUnlik = Integer.toString(model.unlike)
            txtUnlike.text = txtUnlik
            lke = model.like
            unlke = model.unlike
            //   txtPlace.setText(model.getCityname());
            txtTime.text = model.time
            time = model.time
            txtDate.text = model.date
            date = model.date

        }

        fun setLiked(post_liked: String) {
            mDatabaseLike.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.child(post_liked).hasChild(auth.currentUser!!.uid)) {
                        lk.setColorFilter(mView.resources.getColor(R.color.Grenn))
                        mDatabaseunlike.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                if (!dataSnapshot.child(post_liked).hasChild(auth.currentUser!!.uid)) {
                                    unlk.setColorFilter(mView.resources.getColor(R.color.Black))
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {

                            }
                        })
                    } else {
                        lk.setColorFilter(mView.resources.getColor(R.color.Black))
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
        }

        fun setUnliked(post_key: String) {
            mDatabaseunlike.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.child(post_key).hasChild(auth.currentUser!!.uid)) {
                        unlk.setColorFilter(mView.resources.getColor(R.color.Grenn))
                        mDatabaseLike.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                if (!dataSnapshot.child(post_key).hasChild(auth.currentUser!!.uid)) {
                                    lk.setColorFilter(mView.resources.getColor(R.color.Black))
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {

                            }
                        })
                    } else {

                        unlk.setColorFilter(mView.resources.getColor(R.color.Black))
                    }

                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
        }

        fun setDesc(desc: String) {
            val post_desc = mView.findViewById<View>(R.id.post_desc) as TextView
            post_desc.text = desc

        }

        fun setImage(ctx: Context, image: String?) {
            val post_image = mView.findViewById<View>(R.id.postimage) as ImageView
            if (image == null) {
                post_image.visibility = View.GONE
            } else {
                post_image.visibility = View.VISIBLE
                Picasso.with(ctx).load(image).into(post_image)
            }

        }

        fun setName(name: String?) {
            if (name == null) {
                val post_name = mView.findViewById<View>(R.id.bname) as TextView
                post_name.text = "Anonyms"
            } else {
                val post_name = mView.findViewById<View>(R.id.bname) as TextView
                post_name.text = name
            }
        }

        fun setPropic(photo: String) {
            val pro_pic = mView.findViewById<View>(R.id.pimage) as ImageView
            // byte[] decodestring= Base64.decode(photo, Base64.DEFAULT);
            // Bitmap bitmap= BitmapFactory.decodeByteArray(decodestring,0,decodestring.length);
            pro_pic.setImageBitmap(Utils.decodeBase64(photo))
        }

        fun setLike(like: Int) {
            val txt = mView.findViewById<View>(R.id.txtlike) as TextView
            val likE = Integer.toString(like)
            txt.text = likE
        }

        fun setUnlike(unlike: Int) {
            val txt = mView.findViewById<View>(R.id.txtunlike) as TextView
            val unlikE = Integer.toString(unlike)
            txt.text = unlikE
        }


    }

    private fun loadLoginView() {
        val intent = Intent(activity, Loginactivity::class.java)
        startActivity(intent)
        activity!!.finish()
    }
}

