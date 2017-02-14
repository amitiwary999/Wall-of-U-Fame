package com.example.amit.uniconnexample;

import android.app.ProgressDialog;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.validator.routines.EmailValidator;

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

  //  @BindView(R.id.email)
    EditText email;
    //@BindView(R.id.password)
    EditText password;
    TextView forgotpassword;
    private FirebaseAuth auth;
    //@BindView(R.id.log_in)
    Button login;
    //@BindView(R.id.signup)
    Button sign_up;
    ProgressDialog mProgress;
    SharedPreferences.Editor editor1;
   // @BindView(R.id.login_progress)
   // ProgressBar loginProgress;
    FirebaseUser user;
   private DatabaseReference mDatabasenotiflike;
    private FirebaseAuth.AuthStateListener mAuthListener;
    TextView signup;
    String TAG = "TAG";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
       // ButterKnife.bind(this);
        setTitle("Login");
        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        login=(Button)findViewById(R.id.log_in);
        sign_up=(Button)findViewById(R.id.signup);
        forgotpassword=(TextView)findViewById(R.id.forpass);
        auth = FirebaseAuth.getInstance();

        mProgress = new ProgressDialog(this);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
        editor1=getSharedPreferences("com.example.amit.uniconnexample",MODE_PRIVATE).edit();
        if(((App)this.getApplication()).getLogincheck()){
            Intent i = new Intent(Loginactivity.this, Tabs.class);
            startActivity(i);
            finish();
        }
        if (isNetworkConnected()) {
            forgotpassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new MaterialDialog.Builder(Loginactivity.this)
                .content("You will receive an email on your ID to reset the password:")
                .input("Enter your email", null, false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        String mail = dialog.getInputEditText().getText().toString();
                        auth.fetchProvidersForEmail(mail).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                            @Override
                            public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                                if(task.isSuccessful()){
                                    ///////// getProviders() will return size 1. if email ID is available.
                                   if( task.getResult().getProviders().size()==0)
                                    Toast.makeText(Loginactivity.this,"Email id is not registered",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                        if (EmailValidator.getInstance(false).isValid(mail)) {
                            auth.sendPasswordResetEmail(mail);
                            dialog.dismiss();
                        } else dialog.getInputEditText().setError("Enter a valid email");
                    }
                })
                .autoDismiss(false)
                .positiveText("Reset Password")
                .show();


                }
            });
            if(!((App)this.getApplication()).getLogincheck()) {
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
            }
        }else {
            Toast.makeText(Loginactivity.this, "No Internet connection", Toast.LENGTH_LONG).show();
        }
    }

   // @OnClick(R.id.signup)
    void signup(){
        if(auth.getCurrentUser()==null) {
            Intent i = new Intent(Loginactivity.this, Signupactivity.class);
            startActivity(i);
            //finish();
        }
    }

   // @OnClick(R.id.log_in)
    void login(){
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        mProgress.setMessage("***Logging you***");
        mProgress.show();
      //  loginProgress.setVisibility(View.VISIBLE);
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
                                    mDatabasenotiflike=FirebaseDatabase.getInstance().getReference().child("notificationdata").child("like");
                                    mDatabasenotiflike.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if(!dataSnapshot.hasChild(auth.getCurrentUser().getUid())){
                                                mDatabasenotiflike.child(auth.getCurrentUser().getUid()).child("count").setValue(0);

                                            }
                                           // loginProgress.setVisibility(View.GONE);
                                            editor1.putBoolean("isLoggedin",true);
                                            editor1.commit();
                                            mProgress.dismiss();
                                            Intent i = new Intent(Loginactivity.this, Tabs.class);

                                            startActivity(i);
                                            finish();
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
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
