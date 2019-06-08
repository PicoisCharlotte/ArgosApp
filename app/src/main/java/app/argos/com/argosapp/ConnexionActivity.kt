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
import android.util.Log
import android.widget.Toast
import app.argos.com.argosapp.Model.User
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
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
                Toast.makeText(applicationContext, "FAIL", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call, response: Response) {
                Statics.IS_CONNECTED = true;
                var body = response?.body()?.string()

                try {
                    if (!response.isSuccessful) {

                        try {
                            val Jobject = JSONObject(body)


                            if (Jobject.getBoolean("success")) {
                            } else {
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    } else {

                        response.body()!!.close()
                        try {
                            val Jobject = JSONObject(body)

                            if (Jobject.getString("data") != null && !Jobject.getString("data").equals("")) {
                                val userJSONArray = JSONArray(Jobject.getString("data"))
                                val userJSON = JSONObject(userJSONArray.get(0).toString())
                                val user = User(userJSON)
                                User.instance.id = user.id
                                User.instance.email = user.email
                                User.instance.cellphone = user.cellphone
                            }
                            if(Jobject.getString("token") != null && !Jobject.getString("token").equals("")){
                                Statics.API_TOKEN = Jobject.getString("token")
                            }

                            applicationContext.startActivity(Intent(applicationContext, MainActivity::class.java))
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        })
    }

}
