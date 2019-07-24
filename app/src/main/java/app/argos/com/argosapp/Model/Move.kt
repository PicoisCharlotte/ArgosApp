package app.argos.com.argosapp.Model

import com.google.gson.annotations.SerializedName

data class Move(@SerializedName("value") val datum:Int)


data class MoveRequest(
        val move: Move
)

