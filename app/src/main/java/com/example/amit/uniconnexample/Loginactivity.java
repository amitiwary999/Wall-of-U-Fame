package com.example.amit.uniconnexample;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

import static com.example.amit.uniconnexample.R.drawable.user;
import static com.facebook.internal.CallbackManagerImpl.RequestCodeOffset.Login;

/**
 * Created by amit on 29/10/16.
 */

public class Loginactivity extends AppCompatActivity{

    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.password)
    EditText password;
    private FirebaseAuth auth;
    @BindView(R.id.log_in)
    Button login;
    @BindView(R.id.signup)
    Button sign_up;
    @BindView(R.id.login_progress)
    ProgressBar loginProgress;
    FirebaseUser user;
    private FirebaseAuth.AuthStateListener mAuthListener;
    TextView signup;
    String TAG = "TAG";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setTitle("Login");
        auth = FirebaseAuth.getInstance();
        if (isNetworkConnected()) {
            if (auth.getCurrentUser() != null) {
             /*   signup = (TextView) findViewById(R.id.sign_up);
                signup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Loginactivity.this, Signupactivity.class);
                        startActivity(i);
                        finish();
                    }
                });

            }
        else*/

                    startActivity(new Intent(Loginactivity.this, Tabs.class));
                    finish();
                }
        }else {
            Toast.makeText(Loginactivity.this, "No Internet connection", Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.signup)
    void signup(){
        if(auth.getCurrentUser()==null) {
            Intent i = new Intent(Loginactivity.this, Signupactivity.class);
            startActivity(i);
            finish();
        }
    }

    @OnClick(R.id.log_in)
    void login(){
        final String cemail=email.getText().toString();
        String cpassword=password.getText().toString();
        Log.d("mail",cemail);
        Log.d("pass",cpassword);
        if(cemail.length()!=0) {
            if(cpassword.length()!=0) {
                if (isNetworkConnected()) {
                    login.setEnabled(false);
                    //   loginProgress.setVisibility(View.VISIBLE);
                    //attemptLogin(cpassword, cemail);
                    try {
                       // Toast.makeText(Loginactivity.this,cemail,Toast.LENGTH_LONG).show();
                        auth.signInWithEmailAndPassword(cemail, cpassword).addOnCompleteListener(Loginactivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    login.setEnabled(true);
                                    AlertDialog.Builder d = new AlertDialog.Builder(Loginactivity.this);
                                    d.setMessage("Id and Password combination may be wrong").
                                            setCancelable(true);
                                    AlertDialog alert = d.create();
                                    alert.setTitle("Oops...!");
                                    alert.show();
                                  //  email.setError("id may be wrong ");
                                  //  password.setError("password may be wrong");
                                } else {
                                    Intent i = new Intent(Loginactivity.this, Tabs.class);
                                    startActivity(i);
                                    finish();
                                }
                            }
                        });
                    }catch (Exception e){
                        e.printStackTrace();
                        //Toast.makeText(Loginactivity.this,cemail,Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(Loginactivity.this, "No Internet connection", Toast.LENGTH_LONG).show();
                }
            }else{
                AlertDialog.Builder d = new AlertDialog.Builder(Loginactivity.this);
                d.setMessage("Password field is empty").
                        setCancelable(true);
                AlertDialog alert = d.create();
                alert.setTitle("Attention...!");
                alert.show();
            }
        }else{
            AlertDialog.Builder d = new AlertDialog.Builder(Loginactivity.this);
            d.setMessage("Id field is empty").
                    setCancelable(true);
            AlertDialog alert = d.create();
            alert.setTitle("Attention...!");
            alert.show();
        }
    }
    @Override
    public void onStart() {
        super.onStart();

      //  auth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
      //  if (mAuthListener != null) {
       //     auth.removeAuthStateListener(mAuthListener);
       // }
    }





    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(
                Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
