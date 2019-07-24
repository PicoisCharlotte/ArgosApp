package app.argos.com.argosapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import app.argos.com.argosapp.Fragment.HomeFragment
import app.argos.com.argosapp.Fragment.RobotsFragment
import app.argos.com.argosapp.Model.User
import app.argos.com.argosapp.manager.MyUserManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.net.URL

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

    @SuppressLint("StringFormatInvalid")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userManager = MyUserManager(this)

        FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w("NEW_TOKEN", "getInstanceId failed", task.exception)
                        return@OnCompleteListener
                    }

                    // Get new Instance ID token
                    val token = task.result?.token

                    // Log and toast
                    val msg = getString(R.string.msg_token_fmt, token)
                    Log.d("NEW_TOKEN", msg)
                })

        FirebaseMessaging.getInstance().isAutoInitEnabled = true

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
