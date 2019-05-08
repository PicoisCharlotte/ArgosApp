package app.argos.com.argosapp.Fragment

import android.app.Fragment
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import app.argos.com.argosapp.Model.User
import app.argos.com.argosapp.R
import app.argos.com.argosapp.Statics
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

/**
 * Created by charlotte on 06.05.2019.
 */
class ProfilFragment : Fragment() {

    companion object {
        fun newInstance(): ProfilFragment {
            return ProfilFragment()
        }
    }
    lateinit var _db: DatabaseReference

    override fun onCreateView(inflater: LayoutInflater,
                          container: ViewGroup?,
                          savedInstanceState: Bundle?): View? {

        _db = FirebaseDatabase.getInstance().reference

        return inflater.inflate(R.layout.fragment_profil, container, false)

    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun addUser(){
        val user = User.create()
        user.cellphone = "1111111111"
        user.email = "testemail@email.fr"
        user.id = 3

        val newUser = _db.child(Statics.FIREBASE_USER).push()
        newUser.setValue(user)

        Toast.makeText(getContext(), "Task added to the list successfully" + user.id, Toast.LENGTH_SHORT).show()

    }
}