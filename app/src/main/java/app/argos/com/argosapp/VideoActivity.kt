package app.argos.com.argosapp

import android.app.Activity
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.view.View
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
import android.R.interpolator.bounce
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Environment
import android.view.animation.AnimationUtils.loadAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import java.util.*


class VideoActivity : AppCompatActivity(){//}, SurfaceHolder.Callback {

    var id_robot = 0
    var url_camera = ""
    private val client = OkHttpClient()
    var json = JsonObject()

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        if(intent != null) {
            id_robot = intent.getIntExtra("id_robot", 0)
            url_camera = intent.getStringExtra("url_camera")
        }

        close.setOnClickListener {
            finish()
        }
        setOnClicks()
        if(!url_camera.equals("")) {
            video.visibility = View.VISIBLE
            createWebView()
        } else {
            view_no_url_robot.visibility = View.VISIBLE
            video.visibility = View.GONE
        }
    }

    companion object {
        fun newInstance(): VideoActivity {
            val activity = VideoActivity()
            return VideoActivity()
        }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun createWebView(){
        loading_indicator.visibility = View.VISIBLE
        video.setWebViewClient(WebViewClt(this))
        video.getSettings().setJavaScriptEnabled(true)
        video.getSettings().setJavaScriptCanOpenWindowsAutomatically(true)
        video.getSettings().setPluginState(WebSettings.PluginState.ON)
        video.getSettings().setMediaPlaybackRequiresUserGesture(false)
        video.loadUrl(url_camera)

        loading_indicator.visibility = View.GONE
    }

    fun setOnClicks(){

        btn_up.setOnClickListener {
            val data = 5
            Toast.makeText(this, "Avance", Toast.LENGTH_SHORT).show()
            val myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce)
            btn_up.startAnimation(myAnim)
            postRequest(data)
        }
        btn_left.setOnClickListener {
            val data = 8
            Toast.makeText(this, "Tourne à gauche", Toast.LENGTH_SHORT).show()
            val myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce)
            btn_left.startAnimation(myAnim)
            postRequest(data)
        }
        btn_stop.setOnClickListener {
            val data = 6
            Toast.makeText(this, "Stop", Toast.LENGTH_SHORT).show()
            btn_stop.animate().rotation(btn_stop.rotation -360).start()
            postRequest(data)
        }
        btn_right.setOnClickListener {
            val data = 10
            Toast.makeText(this, "Tourne à droite", Toast.LENGTH_SHORT).show()
            val myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce)
            btn_right.startAnimation(myAnim)
            postRequest(data)
        }
        btn_down.setOnClickListener {
            val data = 13
            Toast.makeText(this, "Recule", Toast.LENGTH_SHORT).show()
            val myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce)
            btn_down.startAnimation(myAnim)
            postRequest(data)
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


        val call = client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {

            }
        })

    }
    class WebViewClt internal constructor(private val activity: Activity) : WebViewClient() {

        override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
            activity.video.loadUrl("file:///assets/error.html")
        }
    }

}
