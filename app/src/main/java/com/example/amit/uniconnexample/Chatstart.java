package com.example.amit.uniconnexample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by amit on 8/12/16.
 */

public class Chatstart extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private FirebaseAuth auth;
    FirebaseUser user;
    String msg;
    EditText message;
    DatabaseReference newMessage,newMesage;
    private RecyclerView mChat;
    ImageButton send;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startchat);
        mChat=(RecyclerView)findViewById(R.id. rv_chat_feed);
        send=(ImageButton)findViewById(R.id.btnSend);
        message=(EditText)findViewById(R.id.et_message);
        msg=message.getText().toString();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("message");
        mChat.setLayoutManager(new LinearLayoutManager(this));
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 newMessage=mDatabase.child(auth.getCurrentUser().getUid()).child("sent").child("").push();
                newMessage.setValue(msg);
                 newMesage=mDatabase.child(" ").child("receive").child(auth.getCurrentUser().getUid()).push();
                newMesage.setValue(msg);
            }
        });
    }

}
