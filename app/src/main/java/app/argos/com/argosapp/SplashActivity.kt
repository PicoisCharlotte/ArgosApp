package app.argos.com.argosapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.argos.com.argosapp.Model.Robot
import app.argos.com.argosapp.manager.MyUserManager
import kotlinx.android.synthetic.main.activity_splash.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.URL

class SplashActivity : AppCompatActivity() {

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        startMainActivity()
    }

    private fun startMainActivity() {
        loading_indicator.visibility = View.VISIBLE
        if(MyUserManager.newInstance(this@SplashActivity).getIdUser() > 0)
            getToken()
        val intent = Intent(this@SplashActivity, MainActivity::class.java)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()

        loading_indicator.visibility = View.INVISIBLE
    }

    private fun getToken(){
        var urlString = "https://argosapi.herokuapp.com/tokenUser/select/"
        urlString += MyUserManager.newInstance(this).getIdUser().toString()
        val url = URL(urlString)
        val request = Request.Builder()
                .url(url)
                .addHeader("access-token", MyUserManager.newInstance(this).getApiToken())
                .build()

        val callApi = client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                var body = response?.body()?.string()

                try {
                    val Jobject = JSONObject(body)
                    if(Jobject.has("token"))
                        MyUserManager.newInstance(this@SplashActivity).setApiToken(Jobject.getString("token"))

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        })
    }

    private fun setupSharePreferences(){
        //val prefs = SharedPreferences
        //val prefEditor = prefs.edit()
        //prefEditor.putString("user")
        //prefEditor.apply()
    }
}
