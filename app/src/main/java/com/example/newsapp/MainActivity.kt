package com.example.newsapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.json.JSONException
import java.net.URL
import java.util.*

var myFont : Int = R.font.advent_pro_bold

var mDisplayName: TextView? = null
var mUserID:TextView? = null

class MainActivity : AppCompatActivity() {

    //private lateinit var mDisplayName: TextView
    //private lateinit var mUserID:TextView
    private var mRecyclerview: RecyclerView? = null
    private var mAdapter: Adapter? = null
    private var mList: ArrayList<Items>? = null
    private var mRequestQue: RequestQueue? = null
    private var logoutBtn: Button? = null
    private var mAuth: FirebaseAuth? = null
    private var fAuth: FirebaseFirestore? = null
    private lateinit var userID: String


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.appbar, menu)
        return true
    }

    //handles settings button selection in the app bar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //return super.onOptionsItemSelected(item);

        var returnVal = false
        when (item.itemId) {

            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivityForResult(intent, 1)
                returnVal = true
            }
        }
        return returnVal
    }

    override fun onResume(){
        val prefs = getSharedPreferences(myPrefsNews, MODE_PRIVATE)
        myFont = prefs.getInt("fontChoice", R.font.advent_pro_bold)
        mAdapter = Adapter(applicationContext, mList!!)
        mRecyclerview = findViewById(R.id.recycler_view)
        mRecyclerview!!.adapter = mAdapter
        mAdapter!!.notifyDataSetChanged()
        //updateFont()
        super.onResume()

    }

    /*fun updateFont(){
        mDisplayName?.setTextAppearance(myFont)
        mUserID?.setTextAppearance(myFont)

    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefs = getSharedPreferences(myPrefsNews, MODE_PRIVATE)
        myFont = prefs.getInt("fontChoice", R.font.advent_pro_bold)


        fAuth = FirebaseFirestore.getInstance()
        userID = FirebaseAuth.getInstance().currentUser!!.uid


        val docRef = fAuth!!.collection("users").document(userID)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("Success", "DocumentSnapshot data: ${document.id}")
                    val id = document.get("fullName")
                    val name = document.getString("userID")
                    mDisplayName!!.setText(""+name)
                    mUserID!!.setText(""+id)

                } else {
                    Log.d("success", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("failure", "get failed with ", exception)
            }


        mDisplayName = findViewById(R.id.userIdDisplay)
        mUserID = findViewById(R.id.profileNameDisplay)
        logoutBtn = findViewById(R.id.logOutMainbtn)
        logoutBtn!!.setOnClickListener(View.OnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        })
        mRecyclerview = findViewById(R.id.recycler_view)
        mRecyclerview!!.setHasFixedSize(true)
        mRecyclerview!!.layoutManager = LinearLayoutManager(this)
        mList = ArrayList()
        mRequestQue = Volley.newRequestQueue(this)
        parseJSON()
    }

    //function to parseJSON from a link using an Array request
    private fun parseJSON() {
        val url = "https://data.winnipeg.ca/resource/ndr6-96vi.json"
        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, url, null,
                { response ->
                    for (i in 0 until response.length()) {
                        try {
                            val jsonObject = response.getJSONObject(i)
                            val items = Items(jsonObject.getString("date"), jsonObject.getString("number_of_positive"))
                            mList!!.add(items)
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                    mAdapter = mList?.let { Adapter(this@MainActivity, it) }
                    mRecyclerview!!.adapter = mAdapter
                }
        ) { error -> error.printStackTrace() }
        mRequestQue!!.add(jsonArrayRequest)
    }
}