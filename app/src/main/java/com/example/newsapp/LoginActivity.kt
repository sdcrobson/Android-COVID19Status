package com.example.newsapp

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    //widget declarations
    private lateinit var signUpbtn: Button
    private lateinit var logInBtn: Button
    private lateinit var  emailTxt: TextView
    private lateinit var  passwordTxt: TextView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var resetLoginLink: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //variable assignments
        signUpbtn = findViewById(R.id.registerbtnLogin)
        logInBtn = findViewById(R.id.logInbtn)
        emailTxt = findViewById(R.id.emailtxt)
        passwordTxt = findViewById(R.id.passwordtxt)
        resetLoginLink = findViewById(R.id.loginResetTextView)
        mAuth = FirebaseAuth.getInstance()


        //handles the signup button click -> sends user to RegisterActivity
        signUpbtn.setOnClickListener {
            startActivity(Intent(applicationContext, RegisterActivity::class.java))

        }
        //handles the reset link click -> sends user to ResetEmail
        resetLoginLink.setOnClickListener {
            startActivity(Intent(applicationContext, ResetEmail::class.java))

        }
        //handles the login button click -> if successful, send user to MainActivity
        logInBtn.setOnClickListener {

            //converting fields to string to check if they are empty when the button is clicked
            var sEmail = emailTxt.text.toString().trim()

            var cpassword = passwordTxt.text.toString().trim()

            //validation checks to make sure the user has entered the mandatory data before committing to firebase
            if (TextUtils.isEmpty(sEmail)) {
                emailTxt.error = "Email is required"
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(cpassword)) {
                passwordTxt.error = "Password is a required field"
                return@setOnClickListener

            }
            //verifies the user has a correct email and password -> if successful, directs user to MainActivity
            mAuth.signInWithEmailAndPassword(sEmail, cpassword)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("Success", "Authentication: Success")
                        startActivity(Intent(applicationContext, MainActivity::class.java))

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("Failure", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

        }

    }

