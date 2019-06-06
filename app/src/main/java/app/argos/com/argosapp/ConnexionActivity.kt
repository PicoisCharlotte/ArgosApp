package app.argos.com.argosapp

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_connexion.*
import java.net.HttpURLConnection
import java.net.URL
import android.content.Intent
import android.support.compat.R.id.text
import android.widget.Toast
import app.argos.com.argosapp.Model.User
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException


class ConnexionActivity : AppCompatActivity() {

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connexion)

        btn_connexion.setOnClickListener {
            sendGet()
        }

    }

    fun sendGet() {
        var urlString = "https://argosapi.herokuapp.com/user/select?action=selectAUser&credential="
        var logins = "[\"" + email.text + "\",\"" + password.text +"\"]"
        urlString += logins
        var message = " "
        val url = URL(urlString)
        val request = Request.Builder()
                .url(url)
                .build()

        val callApi = client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Toast.makeText(applicationContext,"FAIL",Toast.LENGTH_SHORT).show()
            }
            override fun onResponse(call: Call, response: Response) {
                Statics.IS_CONNECTED = true;
                var body = response?.body()?.string()

                val gson = GsonBuilder().create()
                gson.fromJson<User>(body, User::class.java)
                println(body)
            }
        })
    }

}
