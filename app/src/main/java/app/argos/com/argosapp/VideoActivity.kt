package app.argos.com.argosapp

import android.app.Activity
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.webkit.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_video.*
import kotlinx.android.synthetic.main.fragment_video.btn_down
import kotlinx.android.synthetic.main.fragment_video.btn_left
import kotlinx.android.synthetic.main.fragment_video.btn_right
import kotlinx.android.synthetic.main.fragment_video.btn_stop
import kotlinx.android.synthetic.main.fragment_video.btn_up
import kotlinx.android.synthetic.main.fragment_video.close
import okhttp3.*
import org.json.JSONObject
import java.io.*
import java.net.URL

class VideoActivity : AppCompatActivity(){//}, SurfaceHolder.Callback {

    var id_robot = 0
    private val client = OkHttpClient()
    var json = JsonObject()
    private lateinit var mediaPlayer: MediaPlayer
    private var playbackPostition = 0
    private val url = "http://argos.eu.ngrok.io/"

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        if(intent != null)
            id_robot = intent.getIntExtra("id_robot", 0)

        close.setOnClickListener {
            finish()
        }
        setOnClicks()
        createWebView()
    }

    companion object {
        fun newInstance(): VideoActivity {
            val activity = VideoActivity()
            return VideoActivity()
        }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun createWebView(){
        video.setWebViewClient(WebViewClt(this))
        video.getSettings().setJavaScriptEnabled(true)
        video.getSettings().setJavaScriptCanOpenWindowsAutomatically(true)
        video.getSettings().setPluginState(WebSettings.PluginState.ON)
        video.getSettings().setMediaPlaybackRequiresUserGesture(false)
        video.loadUrl(url)
    }

    public fun loadErrorPage(webView: WebView){

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

            }
        })

    }
    class WebViewClt internal constructor(private val activity: Activity) : WebViewClient() {

        override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
            activity.video.loadUrl("file:///assets/error.html");
        }
    }

}
