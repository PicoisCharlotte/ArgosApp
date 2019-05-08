package app.argos.com.argosapp.Fragment

import android.app.Fragment
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.argos.com.argosapp.R

/**
 * Created by charlotte on 06.05.2019.
 */
class RobotsFragment : Fragment() {

    companion object {

        fun newInstance(): ProfilFragment {
            return ProfilFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_robots, container, false)

    }
}