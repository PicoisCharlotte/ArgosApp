package app.argos.com.argosapp.Model

import org.json.JSONObject

/**
 * Created by charlotte on 08.05.2019.
 */
class User {

    companion object {
        val instance = User()
    }

    var id: Int? = null
    var email: String? = null
    var cellphone: String? = null

    constructor(id: Int?, email: String?, cellphone: String?){
        this.id = id
        this.email = email
        this.cellphone = cellphone
    }

    constructor()

    constructor(JObject: JSONObject){
        if(JObject.has("id_user"))
            this.id = JObject.getInt("id_user")
        if(JObject.has("email"))
            this.email = JObject.getString("email")
        if(JObject.has("cellphone"))
            this.cellphone = JObject.getString("cellphone")

    }

}