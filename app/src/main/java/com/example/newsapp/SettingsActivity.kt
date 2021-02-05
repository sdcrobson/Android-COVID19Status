package com.example.newsapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import kotlin.random.Random


val myPrefsNews: String = "MyPrefsFile"


class SettingsActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.settings, SettingsFragment())
                    .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            //val colorList = findPreference("colorChoice") as ListPreference?

            val fontList = findPreference("fontChoice") as ListPreference?

            fontList?.onPreferenceChangeListener = Preference.OnPreferenceChangeListener{_: Preference,value: Any ->
                when (value){
                    "Sans Serif Light" ->{
                        myFont = R.style.Sans_Serif_Light

                    }
                    "Default" ->{
                        myFont = R.style.Sans_mono

                    }
                }
                val editor = activity?.getSharedPreferences(myPrefsNews, MODE_PRIVATE)
                editor?.edit()?.putInt("fontChoice", myFont)?.apply()
                true
            }
        }
    }
}