package com.example.newsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class ResetEmail : AppCompatActivity() {

    private lateinit var resetEmailInput: EditText
    private lateinit var loginbtn: Button
    private lateinit var singupbtn: TextView
    private lateinit var resetButton: Button
    private lateinit var mAuth: FirebaseAuth

    private lateinit var inputToString: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_email)

        loginbtn = findViewById(R.id.resetLogInbtn)
        singupbtn = findViewById(R.id.resetbtnSignup)
        resetEmailInput = findViewById(R.id.emailResetTxt)
        resetButton = findViewById(R.id.buttonResetClick)
        mAuth = FirebaseAuth.getInstance()

        loginbtn.setOnClickListener {

        }
        //handles the signup button click -> sends user to LoginActivity
        singupbtn.setOnClickListener {
            startActivity(Intent(applicationContext, RegisterActivity::class.java))
        }
        //handles the resetbutton click -> if successful, sends user a email with a link to change password
        resetButton.setOnClickListener {
            inputToString = resetEmailInput.text.toString()
            mAuth.sendPasswordResetEmail(inputToString).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Success", "Email Was Sent")
                    startActivity(Intent(applicationContext, LoginActivity::class.java))
                    Toast.makeText(
                        baseContext, "Password link has been sent to your email",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Failure", "email sent:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Email Failure",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}