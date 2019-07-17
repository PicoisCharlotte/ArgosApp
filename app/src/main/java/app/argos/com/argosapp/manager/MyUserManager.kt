package app.argos.com.argosapp.manager

import android.content.Context
import android.content.SharedPreferences
import app.argos.com.argosapp.Model.User
import app.argos.com.argosapp.Statics

class MyUserManager(context: Context) {

    public var currentUser = User()
    private var settings: SharedPreferences? = context.getSharedPreferences("ArgosApp", 0)

    val IS_CONNECTED: String = "isConnected"
    val ID_USER: String = "idUser"
    val LAST_USER_ID: String = "lastIdUser"
    val API_TOKEN: String = "apiToken"

    companion object {
        fun newInstance(contextInstance: Context): MyUserManager {
            return MyUserManager(contextInstance)
        }
    }

    fun setIdUser(id: Int){
        settings!!.edit().putInt(ID_USER, id).apply()
    }
    fun getIdUser(): Int {
        return settings!!.getInt(ID_USER, 0)
    }

    public fun getLastUser(): Int {
        return settings!!.getInt(LAST_USER_ID, 0)
    }
    public fun setLastUser(id: Int){
        settings!!.edit().putInt(LAST_USER_ID, id).apply()
    }

    public fun connectUser(isConnected: Boolean){
        settings!!.edit().putBoolean(IS_CONNECTED, isConnected).apply()
    }
    public fun isUserConnected(): Boolean{
        return settings!!.getBoolean(IS_CONNECTED, false)
    }

    public fun getApiToken(): String{
        return settings!!.getString(API_TOKEN, "")
    }
    public fun setApiToken(apiToken: String){
        settings!!.edit().putString(API_TOKEN, apiToken).apply()
    }
}