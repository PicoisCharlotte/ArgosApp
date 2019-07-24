package app.argos.com.argosapp.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.argos.com.argosapp.*
import app.argos.com.argosapp.Interfaces.AdapterCallbackRobot
import app.argos.com.argosapp.Model.Robot
import app.argos.com.argosapp.manager.MyUserManager
import com.goot.gootdistri.Adapters.RobotAdapter
import kotlinx.android.synthetic.main.fragment_robots.*
import kotlinx.android.synthetic.main.fragment_robots.view.*
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
        mAdapter = RobotAdapter(requireContext(), this)
        listRobot = mutableListOf<Robot>()
        linearLayoutManager = LinearLayoutManager(context)
    }

    override fun onCreateView(inflater: LayoutInflater, content: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_robots, content, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        view.close.setOnClickListener {
            getFragmentManager()?.popBackStack()
        }
        view.btn_robot.setOnClickListener{
            val intent = Intent(context, AddRobotActivity::class.java)
            startActivity(intent)
        }
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

    override fun goToVideo(robot: Robot) {
        activity?.let{
            val intent = Intent (it, VideoActivity::class.java)
            intent.putExtra("id_robot", robot.idRobot)
            intent.putExtra("url_camera", robot.urlCamera)
            it.startActivity(intent)
        }
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = fragmentManager?.beginTransaction()
        transaction?.replace(R.id.content, fragment)
        transaction?.addToBackStack(null)
        transaction?.commit()
    }

    fun reloadRobot(){
        loading_indicator.visibility = View.VISIBLE
        list_robots?.setLayoutManager(GridLayoutManager(context, 2) as RecyclerView.LayoutManager)
        list_robots?.adapter = mAdapter
        sendGet()
    }

    fun sendGet() : MutableList<Robot> {
        var urlString = "https://argosapi.herokuapp.com/robot/select/"
        var idUserRobot = MyUserManager.newInstance(requireContext()).getIdUser()
        urlString += idUserRobot
        urlString += "?"
        var message = " "
        val url = URL(urlString)
        val request = Request.Builder()
                .url(url)
                .addHeader("access-token", MyUserManager.newInstance(requireContext()).getApiToken())
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

                            activity?.runOnUiThread(java.lang.Runnable {

                                loading_indicator.visibility = View.INVISIBLE
                                mAdapter.setData(listRobot)
                                mAdapter.notifyDataSetChanged()
                            })

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
