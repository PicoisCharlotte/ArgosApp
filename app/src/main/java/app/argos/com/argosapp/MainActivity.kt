package app.argos.com.argosapp

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import app.argos.com.argosapp.Fragment.HomeFragment
import app.argos.com.argosapp.Fragment.RobotsFragment
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {


    private val client = OkHttpClient()

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.accueil -> {
                if(Statics.IS_CONNECTED) {
                    val homeFragment = HomeFragment.newInstance()
                    openFragment(homeFragment)
                } else {
                    val intent = Intent(this, ConnexionActivity::class.java)
                    startActivity(intent)
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.robots -> {
                if(Statics.IS_CONNECTED) {
                    val robotsFragment = RobotsFragment.newInstance()
                    openFragment(robotsFragment)
                } else {
                    val intent = Intent(this, ConnexionActivity::class.java)
                    startActivity(intent)
                }
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.tabbar.setOnNavigationItemSelectedListener (mOnNavigationItemSelectedListener)

        val homeFragment = HomeFragment.newInstance()
        openFragment(homeFragment)
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.content, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    public fun maskTabBar(){
        tabbar.visibility = View.GONE
    }
}
