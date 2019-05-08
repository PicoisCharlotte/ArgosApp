package app.argos.com.argosapp.Model

/**
 * Created by charlotte on 08.05.2019.
 */
class User {

    companion object Factory {
        fun create(): User = User()
    }

    var id: Int? = null
    var email: String? = null
    var cellphone: String? = null
}