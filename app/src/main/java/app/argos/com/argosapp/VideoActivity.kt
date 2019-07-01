package app.argos.com.argosapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import app.argos.com.argosapp.Fragment.VideoFragment
import app.argos.com.argosapp.Model.Move
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.fragment_video.*
import okhttp3.*
import org.json.JSONObject
import java.io.DataOutputStream
import java.io.IOException
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class VideoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        if(intent != null)
            id_robot = intent.getIntExtra("id_robot", 0)

        close.setOnClickListener {
            finish()
        }
        setOnClicks()   }
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

        Toast.makeText(this, "DATA -> " + data, Toast.LENGTH_SHORT).show()

        val call = client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

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
