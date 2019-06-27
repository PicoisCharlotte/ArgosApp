package app.argos.com.argosapp.Fragment

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import app.argos.com.argosapp.DetailRobotActivity
import app.argos.com.argosapp.Model.Move
import app.argos.com.argosapp.Model.MoveRequest
import app.argos.com.argosapp.Model.Robot
import app.argos.com.argosapp.Model.User

import app.argos.com.argosapp.R
import app.argos.com.argosapp.Statics
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_detail_robot.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_video.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.xml.sax.Parser
import java.io.DataOutputStream
import java.io.IOException
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class VideoFragment : Fragment() {

    companion object {
        fun newInstance(idRobot: Int): VideoFragment {
            val fragment = VideoFragment()
            val args = Bundle()
            args.putInt("idRobot", idRobot)
            fragment.arguments = args
            return VideoFragment()
        }
    }

    var id_robot = 0
    private val client = OkHttpClient()
    var json = JsonObject();

    override fun onCreateView(inflater: LayoutInflater,
                              content: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        if(arguments != null)
            id_robot = arguments.getInt("id_robot")


        return inflater.inflate(R.layout.fragment_video, content, false)

    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClicks()
    }

    fun setOnClicks(){

        btn_up.setOnClickListener {
            val data = 5
            postRequest(data);
        }
        btn_left.setOnClickListener {
            val data = 8
            postRequest(data);
        }
        btn_stop.setOnClickListener {
            val data = 6
            postRequest(data);
        }
        btn_right.setOnClickListener {
            val data = 10
            postRequest(data);
        }
        btn_down.setOnClickListener {
            val data = 13
            postRequest(data);
        }
    }

    fun sendGet(data: Int) {

        val gson = Gson()
        val stringJsonObject = gson.toJson( Move(data) )

        var urlString = "https://io.adafruit.com/api/v2/Titi78/feeds/argos-feed.robotaction/data"
        val url = URL(urlString)
        val request = url.openConnection() as HttpURLConnection
        request.requestMethod = "POST"
        request.connectTimeout = 300000
        request.doOutput = true

        request.setRequestProperty("Content-type", "application/json")
        request.setRequestProperty("X-AIO-Key", Statics.ADAFRUIT_KEY)

        try{
            val outputStream: DataOutputStream = DataOutputStream(request.outputStream)
            outputStream.writeBytes(stringJsonObject)
            outputStream.flush()
        } catch(e: Exception){

        }
    }
    fun postRequest(data: Int){
        var urlString = "https://io.adafruit.com/api/v2/Titi78/feeds/argos-feed.robotaction/data"
        val url = URL(urlString)

        /*val gson = Gson()
        //val stringJsonObject = gson.toJson( Move(data) )
        //val stringJsonObject = "{\"value\": " +  data + " }}"

        /*var jsonDatum = JsonObject()
        jsonDatum.addProperty("value", data)

        val jsonString = json.toString();
        json.add("datum", jsonDatum);
        var body = RequestBody.create(
                JSON, jsonString);*/

        /*val json = """
        "datum":{
            "value":"${data}"
        }
        """.trimIndent()

        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
*/



        val moveRequest = MoveRequest(
                move = Move(datum = data))

        /*var requestBody: RequestBody = FormBody.Builder()
        .add("datum", stringJsonObject)
        .build();*/
        val json = Gson().toJson(moveRequest)
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)


        var request: Request =  Request.Builder()
        .url(url)
        .post(body)
        .addHeader("X-AIO-Key", Statics.ADAFRUIT_KEY)
        .build()*/
        //val valueString = "{\"value\": " +  data + " }"
        //val value = Gson().toJson(valueString)
        //var dict: HashMap<String, String> = HashMap()
        //dict.put("datum", valueString)

        val value = JSONObject()
        value.put("value", data)
        val datum = JSONObject()
        datum.put("datum", value)
        val JSON = MediaType.parse("application/json; charset=utf-8")
        val body = RequestBody.create(JSON, datum.toString())

        val request = Request.Builder()
                .url(url)
                .post(body)
                .addHeader("X-AIO-Key", Statics.ADAFRUIT_KEY)
                .build()

        Toast.makeText(context, "DATA -> " + data, Toast.LENGTH_SHORT).show()

        val call = client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Toast.makeText(context, "FAIL", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call, response: Response) {
                var body = response?.body()?.string()

                try {
                    if (!response.isSuccessful) {


                    } else {


                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        })

    }

}
