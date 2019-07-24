package app.argos.com.argosapp.Model

import org.json.JSONObject

class Robot {

    var idRobot: Int? = null
    var idUserRobot: Int? = null
    var model: String? = null
    var name: String? = null
    var nbCaptor: Int? = null
    var urlCamera: String? = null

    constructor(id: Int, idUser: Int, model: String, name: String, nbCaptor: Int, urlCamera: String){
        this.idRobot = id
        this.idUserRobot = idUser
        this.model = model
        this.name = name
        this.nbCaptor = nbCaptor
        this.urlCamera = urlCamera
    }

    constructor(JObject: JSONObject){
        if(JObject.has("id_robot"))
            this.idRobot = JObject.getInt("id_robot")
        if(JObject.has("id_user_robot"))
            this.idUserRobot = JObject.getInt("id_user_robot")
        if(JObject.has("model"))
            this.model = JObject.getString("model")
        if(JObject.has("name"))
            this.name = JObject.getString("name")
        if(JObject.has("nb_capteur"))
            this.nbCaptor = JObject.getInt("nb_capteur")
        if(JObject.has("url_camera"))
            this.urlCamera = JObject.getString("url_camera")
    }
}