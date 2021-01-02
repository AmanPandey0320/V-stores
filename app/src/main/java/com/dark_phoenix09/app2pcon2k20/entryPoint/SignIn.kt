package com.dark_phoenix09.app2pcon2k20.entryPoint

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.dark_phoenix09.app2pcon2k20.R
import com.dark_phoenix09.app2pcon2k20.Activities.dashboard
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignIn : AppCompatActivity() {

    var mAuth :FirebaseAuth = FirebaseAuth.getInstance()
    val RC_SIGN_IN:Int=1500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        setFeilds(true)

        //google
        google_signin_btn.setOnClickListener {
            SignInwithGoogle(this)
        }

        //goto signup
        goto_signup.setOnClickListener {
            startActivity(Intent(this,SignUp::class.java))
        }

        //reset
        reset_btn.setOnClickListener {
            setFeilds(false)
            if(email_txt_signin.text.toString().isEmpty()){
                email_txt_signin.error="Required!"
                email_txt_signin.requestFocus()
                setFeilds(true)
            }else{

                reset(email_txt_signin.text.toString(),this)
            }
        }

        //login
        signin_btn.setOnClickListener {

            //disabling fields so that user can't change the data
            setFeilds(false)

            //getting the email anf password entered in the respective textboxes
            var e:String =email_txt_signin.text.toString()
            var p:String =password_txt_signin.text.toString()

            if(e.isEmpty()){
                //enabling fields
                setFeilds(true)
                //giving error signals to the user to fill email
                email_txt_signin.requestFocus()
                email_txt_signin.error="Required!"
            }
            if(p.isEmpty()){
                //enabling fields
                setFeilds(true)
                //give error message to the user to fill the password
                password_txt_signin.requestFocus()
                password_txt_signin.error="Required!"
            }

            if(!(e.isEmpty() || p.isEmpty())){
                // if both fields are filled
                //fields are still disabled
                signIn(e,p,this);
            }

        }

    }

    //google signin function
    public fun SignInwithGoogle(context: Context){
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        var googleSignInClient:GoogleSignInClient=GoogleSignIn.getClient(context,gso)

        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RC_SIGN_IN){
            val task =GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                fireBAseAuthWithGoogle(account.idToken!!)
            }catch (e: ApiException){
                Log.d("google failed",e.toString())
            }
        }
    }

    private fun fireBAseAuthWithGoogle(idToken:String){
        val credential :AuthCredential = GoogleAuthProvider.getCredential(idToken,null)
        mAuth.signInWithCredential(credential).addOnCompleteListener {
            if(it.isSuccessful){

                startActivity(Intent(this, dashboard::class.java))
                finish()

            }else{
                Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }


    //login function
    private fun signIn(e:String, p:String, context: Context){
        mAuth.signInWithEmailAndPassword(e,p).addOnCompleteListener{ task ->
            if(task.isSuccessful){

                if(mAuth.currentUser!!.isEmailVerified){
                    setFeilds(true)
                    startActivity(Intent(context, dashboard::class.java))
                    finish()
                }else{
                    setFeilds(true)
                    Toast.makeText(context, "Email not verified!", Toast.LENGTH_SHORT).show()
                    mAuth.signOut()
                }

            }else{
                setFeilds(true)
                Toast.makeText(context, task.exception!!.message, Toast.LENGTH_SHORT).show()

            }
        }
    }

    //function to reset password
    private fun reset(e: String,context: Context){
        mAuth.sendPasswordResetEmail(e).addOnCompleteListener{
            if(it.isSuccessful){
                Toast.makeText(context, "Email sent!", Toast.LENGTH_SHORT).show()
                setFeilds(true)
            }else{
                Toast.makeText(context, it.exception!!.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    //function to make fields enable and disable
    fun setFeilds(status:Boolean){
        if(!status){
            progress_signin.visibility=View.VISIBLE
        }else {
            progress_signin.visibility=View.INVISIBLE
        }

        email_txt_signin.isEnabled=status
        password_txt_signin.isEnabled=status
        signin_btn.isEnabled=status
        signin_btn.isClickable=status
        reset_btn.isEnabled=status
        reset_btn.isClickable=status
        google_signin_btn.isEnabled=status
        goto_signup.isEnabled=status

    }
}