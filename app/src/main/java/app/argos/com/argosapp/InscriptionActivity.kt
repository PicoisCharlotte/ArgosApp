package app.argos.com.argosapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import app.argos.com.argosapp.manager.MyUserManager
import kotlinx.android.synthetic.main.activity_inscription.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.net.URL

class InscriptionActivity : AppCompatActivity() {

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inscription)

        btn_inscription.setOnClickListener{
            createUser()
        }
        close_inscription.setOnClickListener{
            finish()
        }
    }

    private fun createUser(){
        var urlString = "https://argosapi.herokuapp.com/user/insert"
        val url = URL(urlString)

        val value = JSONObject()
        value.put("cellphone", user_cellphone.text)
        value.put("email", user_email.text)
        value.put("login", user_login.text)
        value.put("password", user_password.text)
        val JSON = MediaType.parse("application/json; charset=utf-8")
        val body = RequestBody.create(JSON, value.toString())

        val request = Request.Builder()
                .url(url)
                .post(body)
                .addHeader("access-token", MyUserManager.newInstance(this).getApiToken())
                .build()

        val call = client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {

                val intent = Intent(this@InscriptionActivity, ConnexionActivity::class.java)
                startActivity(intent)
            }
        })
    }
}
