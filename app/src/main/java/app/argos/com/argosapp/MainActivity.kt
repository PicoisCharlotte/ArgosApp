package app.argos.com.argosapp

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import app.argos.com.argosapp.Fragment.HomeFragment
import app.argos.com.argosapp.Fragment.ProfilFragment
import app.argos.com.argosapp.Fragment.RobotsFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {


    private val client = OkHttpClient()
    private var text = "salut test";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //setSupportActionBar(toolbar)

        /*val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()*/

        this.tabbar.setOnNavigationItemSelectedListener (mOnNavigationItemSelectedListener)

        val url = "https://argosapi.herokuapp.com/robot/select?action=selectWhereRobot&idUserRobot=1"
        val request = Request.Builder()
                .url(url)
                .addHeader("access-token", Statics.API_TOKEN)
                .build()

        val callApi = client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                text = "failed"
            }
            override fun onResponse(call: Call, response: Response) {
                text = "success"
            }
        })

        testapi.setText(text)

    }
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.accueil -> {
                val homeFragment = HomeFragment.newInstance()
                openFragment(homeFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.robots -> {
                val robotsFragment = RobotsFragment.newInstance()
                openFragment(robotsFragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun run(url: String) {
        val request = Request.Builder()
                .url(url)
                .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) = testapi.setText(response.body()?.string())
        })
    }
}
