package app.argos.com.argosapp.Fragment

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.argos.com.argosapp.Model.User
import app.argos.com.argosapp.R
import app.argos.com.argosapp.db.DBHelper
import app.argos.com.argosapp.manager.MyUserManager
import kotlinx.android.synthetic.main.fragment_home.*
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.URL

/**
 * Created by charlotte on 08.05.2019.
 */
class HomeFragment : Fragment() {

    private val client = OkHttpClient()

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    var textUser = ""

    lateinit var mDatabase: DBHelper

    override fun onCreateView(inflater: LayoutInflater,
                              content: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        if(User.instance.id != null)
            textUser = "Hello " + User.instance.id + ", " + User.instance.email + ", " + User.instance.cellphone
        else
            textUser = "You should connect to access the application contents !"

        if(user != null) {
            user.setText(textUser)
            user.setTextColor(Color.RED)
        }

        return inflater.inflate(R.layout.fragment_home, content, false)

    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDatabase = DBHelper(context)
        val id = MyUserManager.newInstance(context).getIdUser()
        if(MyUserManager.newInstance(context).getIdUser() > 0)
            getToken()
    }

    private fun getToken(){
        var urlString = "https://argosapi.herokuapp.com/tokenUser/select/"
        urlString += MyUserManager.newInstance(context).getIdUser().toString()
        val url = URL(urlString)
        val request = Request.Builder()
                .url(url)
                .addHeader("access-token", MyUserManager.newInstance(context).getApiToken())
                .build()

        val callApi = client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                var body = response?.body()?.string()

                try {
                    val Jobject = JSONObject(body)
                    if(Jobject.has("token"))
                        MyUserManager.newInstance(context).setApiToken(Jobject.getString("token"))

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        })
    }
}