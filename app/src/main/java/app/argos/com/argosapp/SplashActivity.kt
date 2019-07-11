package app.argos.com.argosapp

import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        startMainActivity()
    }

    private fun startMainActivity() {
        loading_indicator.visibility = View.VISIBLE
        val intent = Intent(this@SplashActivity, MainActivity::class.java)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()

        loading_indicator.visibility = View.INVISIBLE
    }

    private fun setupSharePreferences(){
        //val prefs = SharedPreferences
        //val prefEditor = prefs.edit()
        //prefEditor.putString("user")
        //prefEditor.apply()
    }
}
