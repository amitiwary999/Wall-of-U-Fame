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

import com.example.amit.uniconnexample.utils.Utils;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tt.whorlviewlibrary.WhorlView;

import timber.log.Timber;

public class MainActivity extends Fragment {
    SharedPreferences sprfnc;
    private RecyclerView mBlogList;
    private DatabaseReference mDatabase,pdata;
    private FirebaseAuth auth;
    FirebaseUser user;
    String mal,check;
    UserData userdata;
    WhorlView whorlView;
    FloatingActionButton fab;
    private ProgressDialog mProgress;
    public MainActivity() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
         whorlView = (WhorlView) view.findViewById(R.id.whorl2);
        whorlView.setVisibility(View.VISIBLE);
        whorlView.start();
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
        mBlogList=(RecyclerView)view.findViewById(R.id.mblog_list);
        mBlogList.setHasFixedSize(true);
        mBlogList.setLayoutManager(new LinearLayoutManager(getActivity()));
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
        if(auth.getCurrentUser()!=null) {
            pdata.child("Userdetail").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    userdata = dataSnapshot.getValue(UserData.class);
                    whorlView.stop();
                    whorlView.setVisibility(View.GONE);
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
                protected void populateViewHolder(BlogViewHolder viewHolder, Blogmodel model, int position) {
                    //  model=new Blogmodel();
                  //  viewHolder.setTitle(model.getTitle());
                    viewHolder.bindData(model);
                  /*  viewHolder.setDesc(model.getDesc());
                    viewHolder.setName(model.getName());
                    viewHolder.setImage(getActivity().getApplicationContext(), model.getImage());

                //    Toast.makeText(MainActivity.this,"hi"+model.getName()+model.getDesc(),Toast.LENGTH_LONG).show();
                    viewHolder.setPropic(model.getPropic());
                    viewHolder.setLike(model.getLike());
                    viewHolder.setUnlike(model.getUnlike());*/

                }
            };
            mBlogList.setAdapter(firebaseRecyclerAdapter);
        }else{
            //loadLoginView();
        }
    }

    public void writeblog(){
        startActivity(new Intent(getActivity(),Blog.class).putExtra("user",userdata));
    }
    public  static class BlogViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        View mView;
        ImageButton lk,unlk;
        public BlogViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
             lk=(ImageButton)itemView.findViewById(R.id.like);
             unlk=(ImageButton)itemView.findViewById(R.id.unlike);
            itemView.setOnClickListener(this);
            lk.setOnClickListener(this);
            unlk.setOnClickListener(this);
        }

        public void bindData(Blogmodel model){
            TextView post_desc=(TextView)mView.findViewById(R.id.post_desc);
            ImageView post_image =(ImageView)mView.findViewById(R.id.postimage);
            TextView post_name=(TextView)mView.findViewById(R.id.bname);
            ImageView pro_pic=(ImageView)mView.findViewById(R.id.pimage);
            TextView txtLike=(TextView)mView.findViewById(R.id.txtlike);
            TextView txtUnlike=(TextView)mView.findViewById(R.id.txtunlike);
            TextView txtPlace=(TextView)mView.findViewById(R.id.txtPlace);
            TextView txtTime=(TextView)mView.findViewById(R.id.txtTime);
            TextView txtDate=(TextView)mView.findViewById(R.id.txtDate);
            post_desc.setText(model.getDesc());
            if(model.getImage()==null){
                post_image.setVisibility(View.GONE);
            }else{
                post_image.setVisibility(View.VISIBLE);
                Picasso.with(mView.getContext()).load(model.getImage()).into(post_image);
            }
            if(model.getName()==null){
                post_name.setText("Anonyms");
            }else{
                post_name.setText(model.getName());
            }
            pro_pic.setImageBitmap(Utils.decodeBase64(model.getPropic()));
            String likE=Integer.toString(model.getLike());
            txtLike.setText(likE);
            String txtUnlik=Integer.toString(model.getUnlike());
            txtUnlike.setText(txtUnlik);
            txtPlace.setText(model.getCityname());
            txtTime.setText(model.getTime());
            txtDate.setText(model.getDate());

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

        @Override
        public void onClick(View v) {
            int itemPosition = getLayoutPosition();
            Log.d("Right","show"+itemPosition);
            Toast.makeText(mView.getContext(),"right"+itemPosition,Toast.LENGTH_LONG).show();
        //   if(v==lk)
              // Toast.makeText(mView.getContext(),"right",Toast.LENGTH_LONG).show();
           // if(v==unlk)
              //  Toast.makeText(mView.getContext(),"unright",Toast.LENGTH_LONG).show();
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
  /*  private void loadLoginView() {
        Intent intent = new Intent(this, Loginactivity.class);
        startActivity(intent);
        finish();
    }*/
}
