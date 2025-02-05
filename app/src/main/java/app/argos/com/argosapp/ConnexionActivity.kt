package app.argos.com.argosapp

import android.os.Bundle

import kotlinx.android.synthetic.main.activity_connexion.*
import java.net.URL
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import app.argos.com.argosapp.Model.User
import app.argos.com.argosapp.manager.MyUserManager
import kotlinx.android.synthetic.main.loading_indicator.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import library.Outils


class ConnexionActivity : AppCompatActivity() {

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connexion)

        btn_inscription.setOnClickListener{
            applicationContext.startActivity(Intent(applicationContext, InscriptionActivity::class.java))
        }
        btn_connexion.setOnClickListener {
            loading_indicator.visibility = View.VISIBLE
            Handler().postDelayed({
                sendGet()
            }, 300)

        }
        close.setOnClickListener {
            finish()
        }
    }

    fun sendGet() {
        var urlString = "https://argosapi.herokuapp.com/user/select?credential="
        var logins = "[\"" + email.text + "\",\"" + password.text +"\"]"
        urlString += logins
        val url = URL(urlString)
        val request = Request.Builder()
                .url(url)
                .build()

        val callApi = client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {
                //userManager.connectUser(true)
                var body = response?.body()?.string()

                try {
                    if (!response.isSuccessful) {

                        try {
                            val Jobject = JSONObject(body)

                            Handler(Looper.getMainLooper()).postDelayed({

                                Outils.showDialog(applicationContext, getString(R.string.erreur), getString(R.string.mdp_or_login_incorrect))
                            }, 300)
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
                                val user = User(userJSON, true)
                                User.instance.id = user.id
                                User.instance.email = user.email
                                User.instance.cellphone = user.cellphone
                                MyUserManager.newInstance(this@ConnexionActivity).connectUser(true)
                                user.id?.let { MyUserManager.newInstance(this@ConnexionActivity).setIdUser(it) }
                            }

                            this@ConnexionActivity.runOnUiThread(java.lang.Runnable{
                                if(loading_indicator != null)
                                    loading_indicator.visibility = View.INVISIBLE
                            })
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
