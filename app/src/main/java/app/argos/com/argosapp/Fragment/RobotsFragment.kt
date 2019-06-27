package app.argos.com.argosapp.Fragment

import android.content.Intent
import android.support.v4.app.Fragment
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import app.argos.com.argosapp.DetailRobotActivity
import app.argos.com.argosapp.Interfaces.AdapterCallbackRobot
import app.argos.com.argosapp.Model.Robot
import app.argos.com.argosapp.Model.User
import app.argos.com.argosapp.R
import app.argos.com.argosapp.Statics
import com.goot.gootdistri.Adapters.RobotAdapter
import kotlinx.android.synthetic.main.fragment_robots.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.URL

/**
 * Created by charlotte on 06.05.2019.
 */
class RobotsFragment : Fragment(), AdapterCallbackRobot {

    lateinit var listRobot: MutableList<Robot>
    private val client = OkHttpClient()
    lateinit var mAdapter: RobotAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    companion object {

        fun newInstance(): RobotsFragment {
            return RobotsFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAdapter = RobotAdapter(context, this)
        listRobot = mutableListOf<Robot>()
        linearLayoutManager = LinearLayoutManager(context)
    }

    override fun onCreateView(inflater: LayoutInflater?, content: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        return inflater!!.inflate(R.layout.fragment_robots, content, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        reloadRobot()
    }

    override fun onClickItem(robot: Robot) {
        activity?.let{
            val intent = Intent (it, DetailRobotActivity::class.java)
            intent.putExtra("id_robot", robot.idRobot)
            intent.putExtra("id_user", robot.idUserRobot)
            intent.putExtra("name", robot.name)
            intent.putExtra("model", robot.model)
            intent.putExtra("nb_capteur", robot.nbCaptor)
            it.startActivity(intent)
        }
    }

    override fun goToVideo(idRobot: Int?) {
        val videoViewFragment = VideoFragment.newInstance(idRobot!!)
        openFragment(videoViewFragment)
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.content, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
    fun reloadRobot(){

        list_robots.layoutManager = linearLayoutManager
        this.list_robots.adapter = mAdapter
        Handler().postDelayed({
            listRobot = sendGet()
            mAdapter.setData(listRobot)
        }, 5000)
    }

    fun sendGet() : MutableList<Robot> {
        var urlString = "https://argosapi.herokuapp.com/robot/select?action=selectWhereRobot&idUserRobot="
        var idUserRobot = User.instance.id
        urlString += idUserRobot
        var message = " "
        val url = URL(urlString)
        val request = Request.Builder()
                .url(url)
                .addHeader("access-token", Statics.API_TOKEN)
                .build()

        val callApi = client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Toast.makeText(context, "FAIL", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call, response: Response) {
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
                            val JArray = JSONArray(body)
                            for (i in 0..JArray.length() - 1){
                                var robotJSON = JArray.getJSONObject(i)
                                val robot = Robot(robotJSON)
                                listRobot.add(robot)
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        })
        return listRobot
    }
}
