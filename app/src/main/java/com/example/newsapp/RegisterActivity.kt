package com.example.newsapp

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class RegisterActivity : AppCompatActivity() {
    //widget declarations
    private lateinit var fullName: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var registerbtn: Button
    private lateinit var loginbtn: Button
    private lateinit var resetPassword: TextView

    private lateinit var userID: String

    //firebase/firestore authentication declarations
    private var mAuth: FirebaseAuth? = null

    private var fAuth: FirebaseFirestore? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //variable assignments
        fullName = findViewById(R.id.fullName)
        email = findViewById(R.id.emailtxt)
        password = findViewById(R.id.passwordtxt)
        registerbtn = findViewById(R.id.registerbtn)
        loginbtn = findViewById(R.id.logInbtn)
        resetPassword = findViewById(R.id.resetTextView)

        //firebase/firestore instances
        mAuth = FirebaseAuth.getInstance()
        fAuth = FirebaseFirestore.getInstance()



        //checks to see if a user is logged in.  bypass the loging pages
        if (mAuth!!.currentUser != null) {
            //creates intent to send the user to the main activity once user is registered
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }
        //handles the login button -> sends user to LoginActivity
        loginbtn.setOnClickListener {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
        }
        //handles the reset link click -> sends user to ResetEmail
        resetPassword.setOnClickListener {
            startActivity(Intent(applicationContext, ResetEmail::class.java))

        }
        registerbtn.setOnClickListener {
            //converting fields to string to check if they are empty when the button is clicked
            var sEmail = email.text.toString().trim()
            var cpassword = password.text.toString().trim()
            var sName = fullName.text.toString().trim()

            //substrings user email input to use as their username
            var subStringUserName: String = sEmail.substringBefore("@")
            //validation checks to make sure the user has entered the mandatory data before committing to firebase
            if (TextUtils.isEmpty(sEmail)) {
                email.error = "Email is required"
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(cpassword)) {
                password.error = "Password is a required field"
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(sName)) {
                fullName.error = "Full Name is a required field"
                return@setOnClickListener
            }
            //creates a username with email and password
            mAuth!!.createUserWithEmailAndPassword(sEmail, cpassword).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        userID = FirebaseAuth.getInstance().currentUser!!.uid
                        val user = hashMapOf(
                                "fullName" to sName,
                                "userID" to subStringUserName,
                        )
                        // Add a new document with a generated ID
                        fAuth!!.collection("users").document(userID)
                                .set(user)
                                .addOnSuccessListener { Log.d("Success", "DocumentSnapshot successfully written!") }
                                .addOnFailureListener { e -> Log.w("Failure", "Error writing document", e) }

                        // Sign in success, update UI with the signed-in user's information
                        Log.d("Success", "Authentication: Success")
                        startActivity(Intent(applicationContext, MainActivity::class.java))

                    } else {
                        email.error = "That email is already registered!"
                        // If sign in fails, display a message to the user.
                        Log.w("Failure", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                                baseContext, "No Account Found.",
                                Toast.LENGTH_SHORT

                        ).show()
                    }
            }
        }
    }
}
