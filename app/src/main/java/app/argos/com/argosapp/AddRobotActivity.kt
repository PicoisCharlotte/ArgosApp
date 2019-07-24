package app.argos.com.argosapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.argos.com.argosapp.Fragment.RobotsFragment
import app.argos.com.argosapp.manager.MyUserManager
import kotlinx.android.synthetic.main.activity_add_robot.*
import kotlinx.android.synthetic.main.activity_inscription.*
import kotlinx.android.synthetic.main.fragment_robots.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.net.URL

class AddRobotActivity : AppCompatActivity() {

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_robot)

        btn_add_robot.setOnClickListener{
            createRobot()
        }
        close_add_robot.setOnClickListener{
            finish()
        }
    }

    private fun createRobot(){
        var urlString = "https://argosapi.herokuapp.com/robot/insert"
        val url = URL(urlString)

        val value = JSONObject()
        value.put("id_user_robot", MyUserManager.newInstance(this).getIdUser())
        value.put("model", robot_model.text)
        value.put("name", robot_name.text)
        value.put("nb_capteur", robot_nb_captor.text)
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
                finish()
            }
        })
    }
}
