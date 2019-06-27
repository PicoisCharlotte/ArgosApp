package app.argos.com.argosapp.Interfaces

import app.argos.com.argosapp.Model.Robot

interface AdapterCallbackRobot {

    fun onClickItem(robot: Robot)
    fun goToVideo(idRobot: Int?)
}