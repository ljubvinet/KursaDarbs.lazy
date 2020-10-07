package net.ljubvi.kursadarbslazy.Activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Switch
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_settings.*
import net.ljubvi.kursadarbslazy.R

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val PREFERENCES_FILE = "net.ljubvi.lazy.kursadarbs.settings"
        val sharedPref = this?.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE)
        val defaultValue:Boolean = false
        switch1.isChecked = sharedPref.getBoolean(getString(R.string.show_unmarked), defaultValue)

        switch1.setOnClickListener {
            val sharedPref = this.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putBoolean(getString(R.string.show_unmarked), switch1.isChecked)
                apply()
            }

        }

    }

    override fun onBackPressed() {
        val intent = Intent()
        setResult(RESULT_OK, intent)
        finish()
    }


}