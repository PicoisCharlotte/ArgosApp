package app.argos.com.argosapp

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import app.argos.com.argosapp.Fragment.RobotsFragment
import app.argos.com.argosapp.Model.Robot
import kotlinx.android.synthetic.main.activity_detail_robot.*

class DetailRobotActivity : AppCompatActivity() {

    companion object {

        fun newInstance(): DetailRobotActivity {
            return DetailRobotActivity()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_robot)

        val robotId = intent.getStringExtra("id_robot")
        val userRobotId = intent.getStringExtra("id_user")
        val name = intent.getStringExtra("name")
        val model = intent.getStringExtra("model")
        val nbCaptor = intent.getStringExtra("nb_capteur")

        robot_user.text = userRobotId
        robot_model.text = model
        robot_name.text = name
        robot_nb_captor.text = nbCaptor

    }
}
