package app.argos.com.argosapp

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import app.argos.com.argosapp.Fragment.HomeFragment
import app.argos.com.argosapp.Fragment.RobotsFragment
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {


    private val client = OkHttpClient()
    private var isConnected: Boolean = false

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
            R.id.connexion -> {

                val connexion = tabbar.menu.findItem(R.id.connexion)
                if (Statics.IS_CONNECTED) {
                    connexion.setTitle(resources.getString(R.string.connexion))
                    Statics.IS_CONNECTED = false;
                    val intent = Intent(this, ConnexionActivity::class.java)
                    startActivity(intent)
                } else {
                    val intent = Intent(this, ConnexionActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*val isConnected = savedInstanceState?.get("statics")
        if(isConnected != null){
            Statics.IS_CONNECTED = isConnected as Boolean
        }
        Log.d("MainAct", "RESTOOOOOORE !!!!!!! ")*/

        this.tabbar.setOnNavigationItemSelectedListener (mOnNavigationItemSelectedListener)

        val homeFragment = HomeFragment.newInstance()
        openFragment(homeFragment)


        val connexion = tabbar.menu.findItem(R.id.connexion)
        if (Statics.IS_CONNECTED) {
            connexion.setTitle(resources.getString(R.string.deconnexion))
        } else {
            connexion.setTitle(resources.getString(R.string.connexion))
        }
    }

    /*public override fun onSaveInstanceState(): Parcelable? {
        val savedState = android.app.Fragment.SavedState(super.onSaveInstanceState())
        savedState.isExpanded = isExpanded
        return savedState
    }
    public override fun onRestoreInstanceState(state: Parcelable) {
        if (state is android.app.Fragment.SavedState) {
            super.onRestoreInstanceState(state.
                    superState)
            isExpanded = state.isExpanded
        } else {
            super.onRestoreInstanceState(state)
        }
    }*/

    override fun onSaveInstanceState(outState: Bundle?){
        super.onSaveInstanceState(outState)
        outState?.putCharSequence("statics", Statics.IS_CONNECTED.toString())
        Log.d("MainAct", "ON SAVED INSTANCE !! ")
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        Statics.IS_CONNECTED = savedInstanceState?.get("statics") as Boolean
        Log.d("MainAct", "RESTOOOOOORE !!!!!!! ")
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
