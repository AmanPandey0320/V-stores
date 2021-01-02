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
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUp : AppCompatActivity() {

    val RC_SIGN_IN:Int=1500
    val mAuth =FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        setFeilds(true)

        //goto signup
        goto_signin.setOnClickListener {
            startActivity(Intent(this,SignIn::class.java))
            finish()
        }

        //google signup
        google_signup_btn.setOnClickListener {
            SignInwithGoogle(this)
        }

        signup_btn.setOnClickListener {
            var e=email_txt_signup.text.toString()
            var p=password_txt_signup.text.toString()
            var r=confirm_password_txt_signup.text.toString()
             if(e.isEmpty()){
                 email_txt_signup.error="Required"
                 email_txt_signup.requestFocus()
             }
             if(p.isEmpty()) {
                 password_txt_signup.error = "Required"
                 password_txt_signup.requestFocus()
             }
            if(r.isEmpty()){
                confirm_password_txt_signup.error="Required"
                confirm_password_txt_signup.requestFocus()
            }

            if(!(e.isEmpty() || p.isEmpty() || r.isEmpty())){
                if(p.equals(r)){
                    //proceed to signup
                    EmailSignUp(e,p,this)

                }else{
                    confirm_password_txt_signup.error="Didn't match!!"
                    confirm_password_txt_signup.requestFocus()
                }
            }

        }

    }

    //function to send the verification email to the user
    private fun sendVerificationMail(view: View){
        setFeilds(false)
        mAuth.currentUser?.sendEmailVerification()?.addOnCompleteListener { task->
            if(task.isSuccessful){
                setFeilds(true)
                Toast.makeText(this,"Confirm your Email-ID!",Toast.LENGTH_SHORT).show()
                if(mAuth.currentUser != null)
                    mAuth.signOut()
                startActivity(Intent(this,SignIn::class.java))
                finish()
            }else{
                setFeilds(true)
                Snackbar.make(view,"Email not sent! :(",Snackbar.LENGTH_INDEFINITE).setAction("Retry"){
                    sendVerificationMail(view)
                }.setAction("No"){
                    if(mAuth.currentUser != null)
                        mAuth.signOut()
                }.show()
            }
        }
    }

    //function to email sign up
    private fun EmailSignUp(e:String,p:String,context: Context){
        setFeilds(false)
        mAuth.createUserWithEmailAndPassword(e,p).addOnCompleteListener {
            if(it.isSuccessful){
                sendVerificationMail(sign_up_view)
            }else{
                setFeilds(true)
                var view:View = findViewById(R.id.sign_up_view)
                Snackbar.make(view,"An Error Occured!!",Snackbar.LENGTH_LONG).setAction("Retry"){
                    EmailSignUp(e,p,context)
                }.show()
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

        var googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(context,gso)

        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                fireBAseAuthWithGoogle(account.idToken!!)
            }catch (e: ApiException){
                Log.d("google failed",e.toString())
            }
        }
    }

    private fun fireBAseAuthWithGoogle(idToken:String){
        val credential : AuthCredential = GoogleAuthProvider.getCredential(idToken,null)
        mAuth.signInWithCredential(credential).addOnCompleteListener {
            if(it.isSuccessful){

                startActivity(Intent(this, dashboard::class.java))
                finish()

            }else{
                Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    //function to make fields enable and disable
    fun setFeilds(status:Boolean){
        if(!status){
            progress_signup.visibility= View.VISIBLE
        }else {
            progress_signup.visibility= View.INVISIBLE
        }

        email_txt_signup.isEnabled=status
        password_txt_signup.isEnabled=status
        signup_btn.isEnabled=status
        signup_btn.isClickable=status
        confirm_password_txt_signup.isEnabled=status
        confirm_password_txt_signup.isClickable=status
        google_signup_btn.isEnabled=status

    }

    override fun onBackPressed() {
        if(mAuth.currentUser != null)
            mAuth.signOut()
        super.onBackPressed()
    }

    override fun onStop() {
        if(mAuth.currentUser != null)
            mAuth.signOut()
        super.onStop()
    }
}