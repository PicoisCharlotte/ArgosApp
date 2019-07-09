package app.argos.com.argosapp

import android.graphics.Point
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.view.Surface
import android.view.SurfaceHolder
import android.view.View.*
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.MediaController
import android.widget.Toast
import app.argos.com.argosapp.Fragment.VideoFragment
import app.argos.com.argosapp.Model.Robot
import app.argos.com.argosapp.Model.User
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_video.*
import kotlinx.android.synthetic.main.fragment_video.*
import kotlinx.android.synthetic.main.fragment_video.btn_down
import kotlinx.android.synthetic.main.fragment_video.btn_left
import kotlinx.android.synthetic.main.fragment_video.btn_right
import kotlinx.android.synthetic.main.fragment_video.btn_stop
import kotlinx.android.synthetic.main.fragment_video.btn_up
import kotlinx.android.synthetic.main.fragment_video.close
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.net.Socket
import java.net.URL
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import javax.net.ssl.KeyManager
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocket
import javax.net.ssl.TrustManager

class VideoActivity : AppCompatActivity(){//}, SurfaceHolder.Callback {
    /*override fun surfaceDestroyed(holder: SurfaceHolder?) {

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun surfaceCreated(holder: SurfaceHolder) {
        val surface = holder.surface
        setupMediaPlayer(surface)
        prepareMediaPlayer()
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
    }*/

    var id_robot = 0
    private val client = OkHttpClient()
    var json = JsonObject()
    private lateinit var mediaPlayer: MediaPlayer
    private var playbackPostition = 0
    private val url = "http://0d14dceb.eu.ngrok.io"

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        if(intent != null)
            id_robot = intent.getIntExtra("id_robot", 0)

        //clientGetVideo.sslSocketFactory()

        close.setOnClickListener {
            finish()
        }
        setOnClicks()
        createWebView()
        //setupMediaController();

        /*val holder = video.holder
        holder.addCallback(this)*/
    }

    companion object {
        fun newInstance(idRobot: Int): VideoActivity {
            val activity = VideoFragment()
            val args = Bundle()
            args.putInt("idRobot", idRobot)
            activity.arguments = args
            return VideoActivity()
        }
    }

    /*private fun setupMediaController(){
        progressBar.visibility = VISIBLE
        try {
            val mediaController = MediaController(this@VideoActivity)
            mediaController.setAnchorView(video)
            var uri = Uri.parse(url)
            video.setMediaController(mediaController)
            video.setVideoURI(uri)
        } catch(e: Exception){
            e.printStackTrace()
        }

        video.requestFocus()
        video.setOnPreparedListener (MediaPlayer.OnPreparedListener {
            fun onPrepare(mp: MediaPlayer){
                progressBar.visibility = INVISIBLE
                video.start()
            }
        })
    }*/

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun createWebView(){
        video.setWebViewClient(WebViewClient());
        video.getSettings().setJavaScriptEnabled(true);
        video.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        video.getSettings().setPluginState(WebSettings.PluginState.ON);
        video.getSettings().setMediaPlaybackRequiresUserGesture(false);
        video.setWebChromeClient(WebChromeClient());
        video.loadUrl("http://0d14dceb.eu.ngrok.io");
    }

    override fun onPause() {
        super.onPause()
        playbackPostition = mediaPlayer.currentPosition
    }

    override fun onStop() {
        super.onStop()
        mediaPlayer.stop()
        mediaPlayer.release()
    }

    /*@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun createAudioAttributes(): AudioAttributes{
        val builder = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MOVIE)
        return builder.build()

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setupMediaPlayer(surface: Surface){
        progressBar.visibility = VISIBLE
        mediaPlayer = MediaPlayer()
        mediaPlayer.setSurface(surface)
        val audioAttributes = createAudioAttributes()
        mediaPlayer.setAudioAttributes(audioAttributes)
        val uri = Uri.parse(url)
        try {
            mediaPlayer.setDataSource(this, uri)
        } catch(e: IllegalArgumentException){
            e.printStackTrace()

        }
    }

    private fun prepareMediaPlayer(){
        try {
            mediaPlayer.prepareAsync()
        } catch(e: IllegalStateException){
            e.printStackTrace()
        }
        mediaPlayer.setOnPreparedListener {
            progressBar.visibility = INVISIBLE
            mediaPlayer.seekTo(playbackPostition)
            mediaPlayer.start()
        }
        mediaPlayer.setOnVideoSizeChangedListener { player, width, height ->
            setSurfaceDimensions(player, width, height)
        }
    }

    private fun setSurfaceDimensions(player: MediaPlayer, width: Int, height: Int){
        if(width > 0 && height > 0){
            val screenDimensions = Point()
            windowManager.defaultDisplay.getSize(screenDimensions)
            val surfaceWidth = screenDimensions.x
            val surfaceHeight = screenDimensions.y
            val params = FrameLayout.LayoutParams(surfaceWidth, surfaceHeight)
            video.layoutParams = params
            /*val holder = video.holder
            player.setDisplay(holder)*/
        }
    }*/

    /*private val TLS_PROTOCOLS = arrayOf("TLSv1.1", "TLSv1.2")

    @Throws(KeyManagementException::class, NoSuchAlgorithmException::class)
    fun MySSLSocketFactory(keyManagers: Array<KeyManager>, trustManager: TrustManager){
        val sslContext = SSLContext.getInstance(TLS)
        sslContext.init(keyManagers, TrustManager[]{ trustManager }, null)
        sslContext.getSocketFactory();
        sslContext.init(keyManagers, arrayOf<TrustManager>(trustManager), null)
        // ...
    }

// ...

    private fun enableTLSOnSocket(socket: Socket): Socket {
        if (socket is SSLSocket) {
            (socket as SSLSocket).setEnabledProtocols(TLS_PROTOCOLS)
        }
        return socket
    }*/

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
