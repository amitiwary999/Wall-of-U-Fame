package com.example.amit.uniconnexample;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.amit.uniconnexample.utils.Utils;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tt.whorlviewlibrary.WhorlView;

import java.util.ArrayList;

import timber.log.Timber;

public class MainActivity extends Fragment {
    SharedPreferences sprfnc;
    private RecyclerView mBlogList;
    private DatabaseReference mDatabase,pdata,mDatabaselike,mDatabaseunlike,mDatabasenotif,mDatabasenotiflike,newMessage,mDatabasenotifdata;
    private FirebaseAuth auth;
    FirebaseUser user;
    String mal,check;
    UserData userdata;
    WhorlView whorlView;
    FloatingActionButton fab;
    int lik,unlike;
    int unlik,lyk;
    private Boolean processlike=false,processunlike=false;
    private ArrayList<String> keysArray;
    private ProgressDialog mProgress;
    Query ref;
    public MainActivity() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
         whorlView = (WhorlView) view.findViewById(R.id.whorl2);
        keysArray = new ArrayList<>();
      //  whorlView.setVisibility(View.VISIBLE);
      //  whorlView.start();
        userdata=new UserData();
        user = FirebaseAuth.getInstance().getCurrentUser();
        mProgress=new ProgressDialog(getActivity());
        pdata=FirebaseDatabase.getInstance().getReference();
//        Timber.d(user.name);
      //  mal=user.email;
        auth=FirebaseAuth.getInstance();
        fab=(FloatingActionButton)view.findViewById(R.id.fab);
        String n=auth.getCurrentUser().getEmail();
        check=n.substring(n.indexOf("@")+1,n.lastIndexOf("."));
       // mProgress.show();
        mDatabase= FirebaseDatabase.getInstance().getReference().child(check);
        //ref=mDatabase.orderByChild("date");
        mDatabaselike=FirebaseDatabase.getInstance().getReference().child("like");
        mDatabaseunlike=FirebaseDatabase.getInstance().getReference().child("unlike");
        mDatabasenotif=FirebaseDatabase.getInstance().getReference().child("notification").child("like");
        mDatabasenotifdata=FirebaseDatabase.getInstance().getReference().child("notificationdata").child("data");
        mDatabasenotiflike=FirebaseDatabase.getInstance().getReference().child("notificationdata").child("like");
        pdata.keepSynced(true);
        mDatabase.keepSynced(true);
        mDatabaselike.keepSynced(true);
        mDatabaselike.keepSynced(true);
        mBlogList=(RecyclerView)view.findViewById(R.id.mblog_list);
        mBlogList.setHasFixedSize(true);
        LinearLayoutManager lm=new LinearLayoutManager(getActivity());
     //   lm.setReverseLayout(true);
        lm.setStackFromEnd(true);
        mBlogList.setLayoutManager(lm);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeblog();
            }
        });
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        whorlView.setVisibility(View.VISIBLE);
        whorlView.start();
        if(auth.getCurrentUser()!=null) {
            pdata.child("Userdetail").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    userdata = dataSnapshot.getValue(UserData.class);
                  //  whorlView.stop();
                  //  whorlView.setVisibility(View.GONE);
                  //  mProgress.dismiss();
                 //   Toast.makeText(MainActivity.this,userdata.name,Toast.LENGTH_LONG).show();
                 //   mProgress.dismiss();
                    // updateUI();
                    // loading.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Timber.d("oncancelled");
                }
            });
            FirebaseRecyclerAdapter<Blogmodel, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blogmodel, BlogViewHolder>(
                    Blogmodel.class,
                    R.layout.blog_item,
                    BlogViewHolder.class,
                    mDatabase

            ) {

                @Override
                protected void populateViewHolder(final BlogViewHolder viewHolder, final Blogmodel model, int position) {
                    //  model=new Blogmodel();
                  //  viewHolder.setTitle(model.getTitle());
                    whorlView.stop();
                    whorlView.setVisibility(View.GONE);

                  final String post_key= getRef(position).getKey();
                    viewHolder.bindData(model);
                    viewHolder.setLiked(post_key);
                    viewHolder.setUnliked(post_key);
                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                         //   Toast.makeText(getActivity(),"hi"+post_key,Toast.LENGTH_LONG).show();
                        }
                    });

                    viewHolder.chat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                          mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                              @Override
                              public void onDataChange(DataSnapshot dataSnapshot) {
                                 String key= model.getKey();
                                  if(!key.equals(user.getUid())) {
                                   //   Toast.makeText(getActivity(), key, Toast.LENGTH_LONG).show();
                                      Intent i = new Intent(getActivity(), Chatstart.class);
                                      i.putExtra("chat", key);
                                      startActivity(i);
                                  }else{
                                      Toast.makeText(getActivity(),"You can't chat with yourself",Toast.LENGTH_LONG).show();
                                  }
                              }

                              @Override
                              public void onCancelled(DatabaseError databaseError) {

                              }
                          });
                        }
                    });

                    viewHolder.lk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            processlike=true;
                            mDatabaselike.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(processlike) {
                                        if (dataSnapshot.child(post_key).hasChild(user.getUid())) {

                                            // int
                                                     if(model.getLike()>0) {
                                                         lik = model.getLike() - 1;
                                                     }else{
                                                         lik=0;
                                                     }
                                            mDatabaselike.child(post_key).child(user.getUid()).removeValue();
                                            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                              /*  final MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                                                        .progress(true, 100)
                                                        .content("Saving..")
                                                        .show();*/
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    mDatabase.child(post_key).setValue(new Blogmodel(model.getKey(),model.getDesc(), model.getImage(), model.getName(), model.getPropic(), lik, model.getUnlike(), model.getTime(), model.getDate()),new DatabaseReference.CompletionListener(){

                                                        @Override
                                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                      //      dialog.dismiss();
                                                          //  Toast.makeText(getActivity(), "Saved successfullyll!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                  //  processlike=false;
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });

                                            //   processlike=true;
                                              processlike=false;

                                        } else {
                                           mDatabaseunlike.addListenerForSingleValueEvent(new ValueEventListener() {
                                               @Override
                                               public void onDataChange(DataSnapshot dataSnapshot) {
                                                  // Toast.makeText(getActivity(),(String)dataSnapshot.child(post_key).child(user.getUid()).getValue(),Toast.LENGTH_LONG).show();
                                                   if(dataSnapshot.child(post_key).hasChild(user.getUid())){
                                                    //   if( mDatabaseunlike.child(post_key).child(user.getUid()).equals("Unliked")){
                                                      // int unlik=model.getUnlike();
                                                     //  Toast.makeText(getActivity(),dataSnapshot.child(post_key).child(user.getUid()).getValue(String.class),Toast.LENGTH_LONG).show();
                                                       if(model.getUnlike()>0) {
                                                             unlik = model.getUnlike() - 1;
                                                       }else{
                                                           unlik=0;
                                                       }
                                                       mDatabaseunlike.child(post_key).child(user.getUid()).removeValue();
                                                           mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                                               @Override
                                                               public void onDataChange(DataSnapshot dataSnapshot) {
                                                                   mDatabase.child(post_key).setValue(new Blogmodel(model.getKey(),model.getDesc(),model.getImage(),model.getName(),model.getPropic(),lik,unlik,model.getTime(),model.getDate()),new DatabaseReference.CompletionListener(){

                                                                       @Override
                                                                       public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                                        //   Toast.makeText(getActivity(), "Saved successfully like!", Toast.LENGTH_SHORT).show();
                                                                       }
                                                                   });
                                                                  // processunlike=false;
                                                               }

                                                               @Override
                                                               public void onCancelled(DatabaseError databaseError) {

                                                               }
                                                           });
                                                     //  mDatabase.child(post_key).setValue(new Blogmodel(model.getDesc(),model.getImage(),model.getName(),model.getPropic(),model.getLike(),unlik,model.getTime(),model.getDate()));

                                                   }
                                               }

                                               @Override
                                               public void onCancelled(DatabaseError databaseError) {

                                               }
                                           });
                                            mDatabaselike.child(post_key).child(user.getUid()).setValue("Liked");
                                          //  final int
                                                    lik = model.getLike() + 1;
                                                     mDatabasenotif.child(model.getKey()).child(post_key).child(user.getUid()).setValue(userdata.name);
                                                     mDatabasenotiflike.child(model.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                         @Override
                                                         public void onDataChange(DataSnapshot dataSnapshot) {
                                                           int  count=dataSnapshot.child("count").getValue(Integer.class);
                                                             mDatabasenotiflike.child(model.getKey()).setValue(new Likemodel(++count));
                                                         }

                                                         @Override
                                                         public void onCancelled(DatabaseError databaseError) {

                                                         }
                                                     });
                                                     DatabaseReference newpost=mDatabasenotifdata.child(model.getKey()).push();
                                                     newpost.setValue(new Notificationmodel(userdata.photo,userdata.name+" liked your post"));
                                            //   processlike=true;
                                            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    mDatabase.child(post_key).setValue(new Blogmodel(model.getKey(),model.getDesc(), model.getImage(), model.getName(), model.getPropic(), lik, model.getUnlike(), model.getTime(), model.getDate()),new DatabaseReference.CompletionListener(){

                                                        @Override
                                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                           // Toast.makeText(getActivity(), "Saved successfullyli!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                    processlike=false;

                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }

                                            });
                                          /*  mDatabase.child(post_key).setValue(new Blogmodel(model.getDesc(), model.getImage(), model.getName(), model.getPropic(), lik, model.getUnlike(), model.getTime(), model.getDate()),new DatabaseReference.CompletionListener(){

                                                @Override
                                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                    Toast.makeText(getActivity(), "Saved successfullyli!", Toast.LENGTH_SHORT).show();
                                                }
                                            });*/
                                            processlike=false;
                                        }

                                     /*   mDatabase.addValueEventListener(new ValueEventListener() {
                                            final MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                                                    .progress(true, 100)
                                                    .content("Saving..")
                                                    .show();
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                mDatabase.child(post_key).setValue(new Blogmodel(model.getDesc(), model.getImage(), model.getName(), model.getPropic(), lik, model.getUnlike(), model.getTime(), model.getDate()),new DatabaseReference.CompletionListener(){

                                                    @Override
                                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                        dialog.dismiss();
                                                        Toast.makeText(getActivity(), "Saved successfullyl!", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                                processlike=false;
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });*/
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                          //  Toast.makeText(getActivity(),"hi"+post_key,Toast.LENGTH_LONG).show();

                        }
                    });
                    viewHolder.unlk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            processunlike=true;
                         //   unlike=model.getUnlike();
                            mDatabaseunlike.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(processunlike){
                                        if(dataSnapshot.child(post_key).hasChild(user.getUid())){

                                          //  final int
                                            if(model.getUnlike()>0) {
                                                unlike = model.getUnlike() - 1;
                                            }else{
                                                unlike=0;
                                            }
                                            mDatabaseunlike.child(post_key).child(user.getUid()).removeValue();
                                            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    mDatabase.child(post_key).setValue(new Blogmodel(model.getKey(),model.getDesc(),model.getImage(),model.getName(),model.getPropic(),model.getLike(),unlike,model.getTime(),model.getDate()),new DatabaseReference.CompletionListener(){

                                                        @Override
                                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                          //  Toast.makeText(getActivity(), "Saved successfullyul!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                    processunlike=false;
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                            processunlike=false;
                                        }else{

                                            mDatabaselike.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {

                                                    if(dataSnapshot.child(post_key).hasChild(user.getUid())){
                                                        //   if( mDatabaseunlike.child(post_key).child(user.getUid()).equals("Unliked")){

                                                     //   Toast.makeText(getActivity(),(String)dataSnapshot.child(post_key).child(user.getUid()).getValue(),Toast.LENGTH_LONG).show();
                                                        if(model.getLike()>0) {
                                                            lyk = model.getLike() - 1;
                                                        }else{
                                                            lyk=0;
                                                        }
                                                        mDatabaselike.child(post_key).child(user.getUid()).removeValue();
                                                        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                mDatabase.child(post_key).setValue(new Blogmodel(model.getKey(),model.getDesc(),model.getImage(),model.getName(),model.getPropic(),lyk,unlike,model.getTime(),model.getDate()),new DatabaseReference.CompletionListener(){

                                                                    @Override
                                                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                                     //   Toast.makeText(getActivity(), "Saved successfully!", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                                // processunlike=false;
                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });
                                                    /*    mDatabase.child(post_key).setValue(new Blogmodel(model.getDesc(),model.getImage(),model.getName(),model.getPropic(),lyk,model.getUnlike(),model.getTime(),model.getDate()),new DatabaseReference.CompletionListener(){

                                                            @Override
                                                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                                Toast.makeText(getActivity(), "Saved successfully!", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });*/
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                            mDatabaseunlike.child(post_key).child(user.getUid()).setValue("Unliked");
                                            // final int
                                            unlike=model.getUnlike()+1;
                                            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    mDatabase.child(post_key).setValue(new Blogmodel(model.getKey(),model.getDesc(),model.getImage(),model.getName(),model.getPropic(),model.getLike(),unlike,model.getTime(),model.getDate()),new DatabaseReference.CompletionListener(){

                                                        @Override
                                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                         //   Toast.makeText(getActivity(), "Saved successfullyu!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                    processunlike=false;
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                          /*  mDatabase.child(post_key).setValue(new Blogmodel(model.getDesc(),model.getImage(),model.getName(),model.getPropic(),model.getLike(),unlike,model.getTime(),model.getDate()),new DatabaseReference.CompletionListener(){

                                                @Override
                                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                    Toast.makeText(getActivity(), "Saved successfullyu!", Toast.LENGTH_SHORT).show();
                                                }
                                            });*/
                                            processunlike=false;
                                        }
                                     /*   mDatabase.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                mDatabase.child(post_key).setValue(new Blogmodel(model.getDesc(),model.getImage(),model.getName(),model.getPropic(),model.getLike(),unlike,model.getTime(),model.getDate()),new DatabaseReference.CompletionListener(){

                                                    @Override
                                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                        Toast.makeText(getActivity(), "Saved successfullyu!", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                                      processunlike=false;
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });*/
                                        processunlike=false;
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }
                    });
                  /*  viewHolder.setDesc(model.getDesc());
                    viewHolder.setName(model.getName());
                    viewHolder.setImage(getActivity().getApplicationContext(), model.getImage());

                //    Toast.makeText(MainActivity.this,"hi"+model.getName()+model.getDesc(),Toast.LENGTH_LONG).show();
                    viewHolder.setPropic(model.getPropic());
                    viewHolder.setLike(model.getLike());
                    viewHolder.setUnlike(model.getUnlike());*/

                }
              /*  @Override
                public Blogmodel getItem(int position) {
                    return super.getItem(getItemCount() - 1 - position);
                }*/
            };
            mBlogList.setAdapter(firebaseRecyclerAdapter);

        }else{
            loadLoginView();
       }
    }

    public void writeblog(){
        startActivity(new Intent(getActivity(),Blog.class).putExtra("user",userdata));
    }
    public  static class BlogViewHolder extends RecyclerView.ViewHolder {
        View mView;
        ImageButton lk,unlk,chat;
        private FirebaseAuth auth;
        DatabaseReference mDatabaseLike,mDatabaseunlike;
        String check,desc,pic,nam,photo,time,date;
        int lke,unlke;
        DatabaseReference df;
        public BlogViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
            auth=FirebaseAuth.getInstance();
         //   String n=auth.getCurrentUser().getEmail();
         //   check=n.substring(n.indexOf("@")+1,n.lastIndexOf("."));
       //     df=FirebaseDatabase.getInstance().getReference().child(check);
            mDatabaseLike=FirebaseDatabase.getInstance().getReference().child("like");
            mDatabaseunlike=FirebaseDatabase.getInstance().getReference().child("unlike");
             lk=(ImageButton)itemView.findViewById(R.id.like);
             unlk=(ImageButton)itemView.findViewById(R.id.unlike);
            chat=(ImageButton)itemView.findViewById(R.id.chat);
        }

        public void bindData(Blogmodel model){
            TextView post_desc=(TextView)mView.findViewById(R.id.post_desc);
            ImageView post_image =(ImageView)mView.findViewById(R.id.postimage);
            TextView post_name=(TextView)mView.findViewById(R.id.bname);
            ImageView pro_pic=(ImageView)mView.findViewById(R.id.pimage);
            TextView txtLike=(TextView)mView.findViewById(R.id.txtlike);
            TextView txtUnlike=(TextView)mView.findViewById(R.id.txtunlike);
          //  TextView txtPlace=(TextView)mView.findViewById(R.id.txtPlace);
            TextView txtTime=(TextView)mView.findViewById(R.id.txtTime);
            TextView txtDate=(TextView)mView.findViewById(R.id.txtDate);
            post_desc.setText(model.getDesc());
            desc=model.getDesc();
            if(model.getImage()==null){
                post_image.setVisibility(View.GONE);
                pic=model.getImage();
            }else{
                post_image.setVisibility(View.VISIBLE);
                Picasso.with(mView.getContext()).load(model.getImage()).into(post_image);
                pic=model.getImage();
            }
            if(model.getName()==null){
                post_name.setText("Anonyms");
                nam="Anonyms";
            }else{
                post_name.setText(model.getName());
                nam=model.getName();
            }
            pro_pic.setImageBitmap(Utils.decodeBase64(model.getPropic()));
            photo=model.getPropic();
            String likE=Integer.toString(model.getLike());
            txtLike.setText(likE);
            String txtUnlik=Integer.toString(model.getUnlike());
            txtUnlike.setText(txtUnlik);
            lke=model.getLike();
            unlke=model.getUnlike();
         //   txtPlace.setText(model.getCityname());
            txtTime.setText(model.getTime());
            time=model.getTime();
            txtDate.setText(model.getDate());
            date=model.getDate();

        }
        public void setLiked(final String post_liked){
                   mDatabaseLike.addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(DataSnapshot dataSnapshot) {
                         if(dataSnapshot.child(post_liked).hasChild(auth.getCurrentUser().getUid())){
                                        lk.setColorFilter(mView.getResources().getColor(R.color.Grenn));
                             mDatabaseunlike.addListenerForSingleValueEvent(new ValueEventListener() {
                                 @Override
                                 public void onDataChange(DataSnapshot dataSnapshot) {
                                     if(!(dataSnapshot.child(post_liked).hasChild(auth.getCurrentUser().getUid()))){
                                         unlk.setColorFilter(mView.getResources().getColor(R.color.Black));
                                     }
                                 }

                                 @Override
                                 public void onCancelled(DatabaseError databaseError) {

                                 }
                             });
                         }else{
                                     lk.setColorFilter(mView.getResources().getColor(R.color.Black));
                         }
                       }

                       @Override
                       public void onCancelled(DatabaseError databaseError) {

                       }
                   });
        }

        public void setUnliked(final String post_key){
            mDatabaseunlike.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(post_key).hasChild(auth.getCurrentUser().getUid())){
                          unlk.setColorFilter(mView.getResources().getColor(R.color.Grenn));
                        mDatabaseLike.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(!(dataSnapshot.child(post_key).hasChild(auth.getCurrentUser().getUid()))){
                                    lk.setColorFilter(mView.getResources().getColor(R.color.Black));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }else{

                       unlk.setColorFilter(mView.getResources().getColor(R.color.Black));
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        public   void  setDesc(String desc){
            TextView post_desc=(TextView)mView.findViewById(R.id.post_desc);
            post_desc.setText(desc);

        }
        public  void setImage(Context ctx, String image){
            ImageView post_image =(ImageView)mView.findViewById(R.id.postimage);
             if(image==null){
                 post_image.setVisibility(View.GONE);
             }else {
                 post_image.setVisibility(View.VISIBLE);
                 Picasso.with(ctx).load(image).into(post_image);
             }

        }
        public void setName(String name){
            if(name==null){
                TextView post_name=(TextView)mView.findViewById(R.id.bname);
                post_name.setText("Anonyms");
            }else {
                TextView post_name = (TextView) mView.findViewById(R.id.bname);
                post_name.setText(name);
            }
        }
        public void setPropic(String photo){
            ImageView pro_pic=(ImageView)mView.findViewById(R.id.pimage);
           // byte[] decodestring= Base64.decode(photo, Base64.DEFAULT);
           // Bitmap bitmap= BitmapFactory.decodeByteArray(decodestring,0,decodestring.length);
            pro_pic.setImageBitmap(Utils.decodeBase64(photo));
        }

        public void setLike(int like){
            TextView txt=(TextView)mView.findViewById(R.id.txtlike);
            String likE=Integer.toString(like);
            txt.setText(likE);
        }

        public void setUnlike(int unlike){
            TextView txt=(TextView)mView.findViewById(R.id.txtunlike);
            String unlikE=Integer.toString(unlike);
            txt.setText(unlikE);
        }


    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.action_add){
            startActivity(new Intent(MainActivity.this,Blog.class).putExtra("user",userdata));
        }
        if (item.getItemId()==R.id.profile){
            startActivity(new Intent(MainActivity.this,Profile.class).putExtra("user",userdata));

        }

         if(item.getItemId()==R.id.signout){
             FirebaseAuth.getInstance().signOut();
             Toast.makeText(MainActivity.this, "Logging out..", Toast.LENGTH_SHORT).show();
             startActivity(new Intent(MainActivity.this, Loginactivity.class));
             finish();
        }
        return super.onOptionsItemSelected(item);
    }*/
    private void loadLoginView() {
        Intent intent = new Intent(getActivity(), Loginactivity.class);
        startActivity(intent);
       getActivity(). finish();
    }
}
