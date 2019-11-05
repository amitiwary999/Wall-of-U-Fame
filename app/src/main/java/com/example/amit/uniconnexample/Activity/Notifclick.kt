package com.example.amit.uniconnexample.Activity

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

import com.example.amit.uniconnexample.Model.BlogModel
import com.example.amit.uniconnexample.R
import com.example.amit.uniconnexample.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.tt.whorlviewlibrary.WhorlView
import kotlinx.android.synthetic.main.activity_notificationclick.*

/**
 * Created by amit on 28/1/17.
 */

class Notifclick : AppCompatActivity() {
    internal var check: String ?= null
    internal var desc: String? = null
    internal var pic: String? = null
    internal var nam: String? = null
    internal var photo: String? = null
    internal var time: String ?= null
    internal var date: String ?= null
    internal var key: String? = null
    internal var lke: Int = 0
    internal var unlke: Int = 0
    lateinit var model: BlogModel
    internal var whorlView: WhorlView ?= null
    lateinit var auth: FirebaseAuth
    internal var user: FirebaseUser? = null
    internal var bundle: Bundle? = null
    internal var lik: Int = 0
    internal var unlike: Int = 0
    internal var count: Int = 0
    internal var unlik: Int = 0
    internal var lyk: Int = 0
    private var processlike: Boolean? = false
    private var processunlike: Boolean? = false
    internal var handler = Handler()
    internal var rootview: View? = null
    lateinit var mDatabase: DatabaseReference
    lateinit var mDatabaseLike: DatabaseReference
    lateinit var mDatabaseunlike: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notificationclick)

        whorlView = findViewById<View>(R.id.whorl2) as WhorlView
        bundle = intent.extras
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            //     startActivity(new Intent(Chatstart.this,Tabs.class));
            finish()
        }
        if (bundle!!.getString("postkey") != null) {
            key = bundle!!.getString("postkey")
            Log.d("key ", "key is " + key!!)
        }

        auth = FirebaseAuth.getInstance()
        user = auth.currentUser
        val n = auth.currentUser!!.email
        check = n!!.substring(n.indexOf("@") + 1, n.lastIndexOf("."))
        // Toast.makeText(this,key,Toast.LENGTH_LONG).show();

        mDatabase = FirebaseDatabase.getInstance().reference.child("Posts").child(key!!)
        Log.d("check ", "$check $key")
        model = BlogModel()
        mDatabaseLike = FirebaseDatabase.getInstance().reference.child("like")
        mDatabaseunlike = FirebaseDatabase.getInstance().reference.child("unlike")

        like.setOnClickListener {
            processlike = true
            mDatabaseLike.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (processlike!!) {
                        if (dataSnapshot.child(key!!).hasChild(user!!.uid)) {

                            // int
                            if (model.like > 0) {
                                lik = model.like - 1
                            } else {
                                lik = 0
                            }
                            mDatabaseLike.child(key!!).child(user!!.uid).removeValue()
                            mDatabase.setValue(BlogModel(model.desc?:"", model.image?:"", model.name?:"", model.propic?:"", lik, model.unlike, model.time, model.date, model.emailflag, model.cityname))

                            //   processlike=true;
                            processlike = false

                        } else {
                            mDatabaseunlike.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    // Toast.makeText(getActivity(),(String)dataSnapshot.child(post_key).child(user.getUid()).getValue(),Toast.LENGTH_LONG).show();
                                    if (dataSnapshot.child(key!!).hasChild(user!!.uid)) {
                                        //   if( mDatabaseunlike.child(post_key).child(user.getUid()).equals("Unliked")){
                                        // int unlik=model.getUnlike();
                                        //  Toast.makeText(getActivity(),dataSnapshot.child(post_key).child(user.getUid()).getValue(String.class),Toast.LENGTH_LONG).show();
                                        if (model.unlike > 0) {
                                            unlik = model.unlike - 1
                                        } else {
                                            unlik = 0
                                        }
                                        mDatabaseunlike.child(key!!).child(user!!.uid).removeValue()
                                        mDatabase.setValue(BlogModel(model.desc?:"", model.image?:"", model.name?:"", model.propic?:"", lik, unlik, model.time, model.date, model.emailflag, model.cityname))


                                    }


                                }

                                override fun onCancelled(databaseError: DatabaseError) {

                                }
                            })
                            lik = model.like + 1
                            mDatabaseLike.child(key!!).child(user!!.uid).setValue("Liked")
                            mDatabase.setValue(BlogModel(model.desc?:"", model.image?:"", model.name?:"", model.propic?:"", lik, model.unlike, model.time, model.date, model.emailflag, model.cityname))
                            //  final int

                            //   processlike=true;

                            processlike = false
                        }
                        processlike = false
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
        }
        unlike_btn.setOnClickListener {
            processunlike = true
            //   unlike=model.getUnlike();
            mDatabaseunlike.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (processunlike!!) {
                        if (dataSnapshot.child(key!!).hasChild(user!!.uid)) {

                            //  final int
                            if (model.unlike > 0) {
                                unlike = model.unlike - 1
                            } else {
                                unlike = 0
                            }
                            mDatabaseunlike.child(key!!).child(user!!.uid).removeValue()
                            mDatabase.setValue(BlogModel(model.desc?:"", model.image?:"", model.name?:"", model.propic?:"", model.like, unlike, model.time, model.date, model.emailflag, model.cityname))

                            processunlike = false
                        } else {

                            mDatabaseLike.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {

                                    if (dataSnapshot.child(key!!).hasChild(user!!.uid)) {
                                        //   if( mDatabaseunlike.child(post_key).child(user.getUid()).equals("Unliked")){

                                        //   Toast.makeText(getActivity(),(String)dataSnapshot.child(post_key).child(user.getUid()).getValue(),Toast.LENGTH_LONG).show();
                                        if (model.like > 0) {
                                            lyk = model.like - 1
                                        } else {
                                            lyk = 0
                                        }
                                        mDatabaseLike.child(key!!).child(user!!.uid).removeValue()
                                        mDatabase.setValue(BlogModel( model.desc?:"", model.image?:"", model.name?:"", model.propic?:"", lyk, unlike, model.time, model.date, model.emailflag, model.cityname))

                                    }
                                }

                                override fun onCancelled(databaseError: DatabaseError) {

                                }
                            })
                            mDatabaseunlike.child(key!!).child(user!!.uid).setValue("Unliked")
                            // final int
                            unlike = model.unlike + 1
                            mDatabase.setValue(BlogModel( model.desc?:"", model.image?:"", model.name?:"", model.propic?:"", model.like, unlike, model.time, model.date, model.emailflag, model.cityname))


                            processunlike = false
                        }

                        processunlike = false
                    }

                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
        }


    }

    override fun onStart() {
        super.onStart()
        if (key != null) {
            whorlView?.visibility = View.VISIBLE
            whorlView?.start()
            mDatabase.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    setLiked(key)
                    setUnliked(key)
                    Log.d("flag ", "null $dataSnapshot")
                    model = dataSnapshot.getValue(BlogModel::class.java)
                    post_desc.text = model.desc
                    desc = model.desc
                    if (model.image == null) {
                        postimage.visibility = View.GONE
                        pic = model.image
                    } else {
                        postimage.visibility = View.VISIBLE
                        Picasso.with(baseContext).load(model.image).into(postimage)
                        pic = model.image
                    }
                    if (model.name == null) {
                        bname.text = "Anonyms"
                        nam = "Anonyms"
                    } else {
                        bname.text = model.name
                        nam = model.name
                    }
                    pimage.setImageBitmap(Utils.decodeBase64(model.propic?:""))
                    photo = model.propic
                    val likE = Integer.toString(model.like)
                    txtlike.text = likE
                    val txtUnlik = Integer.toString(model.unlike)
                    txtunlike.text = txtUnlik
                    lke = model.like
                    unlke = model.unlike
                    //   txtPlace.setText(model.getCityname());
                    txtTime.text = model.time
                    time = model.time
                    txtDate.text = model.date
                    date = model.date
                    whorlView?.stop()
                    whorlView?.visibility = View.GONE


                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
        }
    }

    fun setLiked(post_liked: String?) {
        mDatabaseLike.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.child(post_liked!!).hasChild(auth.currentUser!!.uid)) {
                    like.setColorFilter(resources.getColor(R.color.Grenn))
                    mDatabaseunlike.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (!dataSnapshot.child(post_liked).hasChild(auth.currentUser!!.uid)) {
                                unlike_btn.setColorFilter(resources.getColor(R.color.Black))
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {

                        }
                    })
                } else {
                    like.setColorFilter(resources.getColor(R.color.Black))
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    fun setUnliked(post_key: String?) {
        mDatabaseunlike.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.child(post_key!!).hasChild(auth.currentUser!!.uid)) {
                    unlike_btn.setColorFilter(resources.getColor(R.color.Grenn))
                    mDatabaseLike.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (!dataSnapshot.child(post_key).hasChild(auth.currentUser!!.uid)) {
                                like.setColorFilter(resources.getColor(R.color.Black))
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {

                        }
                    })
                } else {

                    unlike_btn.setColorFilter(resources.getColor(R.color.Black))
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

}

