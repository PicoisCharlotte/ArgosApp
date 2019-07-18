package app.argos.com.argosapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import app.argos.com.argosapp.Fragment.HomeFragment
import app.argos.com.argosapp.Fragment.RobotsFragment
import app.argos.com.argosapp.Model.User
import app.argos.com.argosapp.manager.MyUserManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*

class MainActivity : AppCompatActivity() {


    private val client = OkHttpClient()
    private var isConnected: Boolean = false

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.accueil -> {
                if(MyUserManager.newInstance(this).isUserConnected()) {
                    val homeFragment = HomeFragment.newInstance()
                    openFragment(homeFragment)
                } else {
                    val intent = Intent(this, ConnexionActivity::class.java)
                    startActivity(intent)
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.robots -> {
                if(MyUserManager.newInstance(this).isUserConnected()) {
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
                if (MyUserManager.newInstance(this).isUserConnected()) {
                    connexion.setTitle(resources.getString(R.string.connexion))
                    val intent = Intent(this, ConnexionActivity::class.java)
                    MyUserManager.newInstance(this).connectUser(false)
                    MyUserManager.newInstance(this).setLastUser(0)
                    MyUserManager.newInstance(this).setIdUser(0)
                    startActivity(intent)
                } else {
                    connexion.setTitle(resources.getString(R.string.deconnexion))
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

        val userManager = MyUserManager(this)


        this.tabbar.setOnNavigationItemSelectedListener (mOnNavigationItemSelectedListener)

        val homeFragment = HomeFragment.newInstance()
        openFragment(homeFragment)

        if(MyUserManager.newInstance(this@MainActivity).getLastUser() == null)
            MyUserManager.newInstance(this).setLastUser(0)

        val connexion = tabbar.menu.findItem(R.id.connexion)
        if (MyUserManager.newInstance(this).isUserConnected()) {
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
