package com.example.amit.uniconnexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amit.uniconnexample.utils.Utils;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by amit on 7/12/16.
 */

public class Chat extends AppCompatActivity {
    private RecyclerView mChat;
    private DatabaseReference mDatabase;
    private FirebaseAuth auth;
    private TabLayout tablayoutbottom;
    FirebaseUser user;
    String check;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        tablayoutbottom=(TabLayout)findViewById(R.id.tabLayoutbottom);
        mChat=(RecyclerView)findViewById(R.id.mchat_list);
        user = FirebaseAuth.getInstance().getCurrentUser();
        auth=FirebaseAuth.getInstance();
        String n=auth.getCurrentUser().getEmail();
        check=n.substring(n.indexOf("@")+1,n.lastIndexOf("."));
        mDatabase= FirebaseDatabase.getInstance().getReference().child(check+"chat");
        mChat.setLayoutManager(new LinearLayoutManager(this));
        setupTabIconsBottom();
        bindWidgetsWithAnEvent();
        //return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Chatusermodel,Chatviewholder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Chatusermodel, Chatviewholder>(
                Chatusermodel.class,
                R.layout.activity_chatitem,
                Chatviewholder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(Chatviewholder viewHolder, Chatusermodel model, int position) {
                if(!mDatabase.equals(auth.getCurrentUser().getUid()))
                viewHolder.bindData(model);
               final String key=getRef(position).getKey();
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(Chat.this,key,Toast.LENGTH_LONG).show();
                        Intent i=new Intent(Chat.this,Chatstart.class);
                        i.putExtra("chat",key);
                        startActivity(i);
                    }
                });
            }
        };
        mChat.setAdapter(firebaseRecyclerAdapter);
    }

    private void setupTabIconsBottom() {
        tablayoutbottom.addTab(tablayoutbottom.newTab().setIcon(R.drawable.home), true);
        tablayoutbottom.addTab(tablayoutbottom.newTab().setIcon(R.drawable.myaccount));
        tablayoutbottom.addTab(tablayoutbottom.newTab().setIcon(R.drawable.notifications));
        tablayoutbottom.addTab(tablayoutbottom.newTab().setIcon(R.drawable.message));
        tablayoutbottom.addTab(tablayoutbottom.newTab().setIcon(R.drawable.chati));
        tablayoutbottom.addTab(tablayoutbottom.newTab().setIcon(R.drawable.settings));
    }

    private void bindWidgetsWithAnEvent()
    {
        tablayoutbottom.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setCurrentTabFragment(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                setCurrentTabFragment(tab.getPosition());
            }
        });

    }

    private void setCurrentTabFragment(int tabPosition)
    {
        switch (tabPosition)
        {
            case 0:
                startActivity(new Intent(Chat.this,Tabs.class));
                finish();
                break;
            case 1 :
                startActivity(new Intent(Chat.this,Profile.class));
                finish();
                //replaceFragment(new Profile());
                break;
            case 2 :
                startActivity(new Intent(Chat.this,Notification.class));
                finish();
                // replaceFragment(new Message());
                break;
            case 3:
                 startActivity(new Intent(Chat.this,Message.class));
                //replaceFragment(new Notification());
                break;
            case 4:
              //  startActivity(new Intent(Chat.this,Chat.class));
             //   break;
            case 5:
                startActivity(new Intent(Chat.this,Settings.class));
                finish();
                // replaceFragment(new Settings());
                break;
        }
    }

    public static class Chatviewholder extends RecyclerView.ViewHolder{
        View mView;
        public Chatviewholder(View itemView) {
            super(itemView);
            mView=itemView;
        }
        public void bindData(Chatusermodel model){
            TextView post_name=(TextView)mView.findViewById(R.id.bname);
            ImageView pro_pic=(ImageView)mView.findViewById(R.id.pimage);
            if(model.getName()==null){
                post_name.setText("Anonyms");
               // nam="Anonyms";
            }else{
                post_name.setText(model.getName());
               // nam=model.getName();
            }
            if(model.getPropic()!=null)
            pro_pic.setImageBitmap(Utils.decodeBase64(model.getPropic()));
        }
    }
}
