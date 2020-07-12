package com.example.amit.uniconnexample.Fragment.Login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.amit.uniconnexample.Activity.NewTabActivity
import com.example.amit.uniconnexample.R
import com.example.amit.uniconnexample.View.BaseBottomSheetDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.login_fragment.*


/**
 * Created by Meera on 12,July,2020
 */
class LoginFragment: BaseBottomSheetDialog(){
    private val RC_SIGN_IN = 1001
    var googleSignInClient: GoogleSignInClient? = null
    private var firebaseAuth: FirebaseAuth? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view  = inflater.inflate(R.layout.login_fragment, container)
        configureGoogleClient()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sign_in_button?.setOnClickListener {
            signInToGoogle()
        }
    }

    fun signInToGoogle() {
        Log.d("sign in google","sign in")
        val signInIntent = googleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    fun configureGoogleClient() { // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN) // for the requestIdToken, this is in the values.xml file that
// is generated from your google-services.json
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(activity as NewTabActivity, gso)
        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("result ","req code "+requestCode)
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try { // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                account?.let {
                    firebaseAuthWithGoogle(it)
                }
            } catch (e: ApiException) { // Google Sign In failed, update UI appropriately
                Log.d("error gogole ",e.localizedMessage)
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth?.signInWithCredential(credential)?.addOnCompleteListener {
            Log.d("successful login ","val "+it)
            if(it.isSuccessful){
                Log.d("successful login ","val "+it.isSuccessful)
                dismiss()
            }else{
                Toast.makeText(activity, "Oops! Something went wrong. Please try again", Toast.LENGTH_SHORT).show()
            }
        }
    }
}