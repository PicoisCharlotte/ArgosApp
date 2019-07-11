package app.argos.com.argosapp.Fragment

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.argos.com.argosapp.Model.User
import app.argos.com.argosapp.R
import app.argos.com.argosapp.db.DBHelper
import app.argos.com.argosapp.manager.MyUserManager
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * Created by charlotte on 08.05.2019.
 */
class HomeFragment : Fragment() {

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    var textUser = ""

    var currentUser: User = User()
    lateinit var mDatabase: DBHelper


    override fun onCreateView(inflater: LayoutInflater,
                              content: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        if(User.instance.id != null)
            textUser = "Hello " + User.instance.id + ", " + User.instance.email + ", " + User.instance.cellphone
        else
            textUser = "You should connect to access the application contents !"

        if(user != null) {
            user.setText(textUser)
            user.setTextColor(Color.RED)
        }


        return inflater.inflate(R.layout.fragment_home, content, false)

    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDatabase = DBHelper(context)
        val id = MyUserManager.newInstance(context).getIdUser()
        if(id != 0 && id != null)
            currentUser = mDatabase.getUser(id)
    }
}