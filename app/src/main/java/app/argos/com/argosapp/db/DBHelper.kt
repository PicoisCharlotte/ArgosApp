package app.argos.com.argosapp.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import app.argos.com.argosapp.Model.User
import java.io.IOException

class DBHelper (context: Context) :
        SQLiteOpenHelper(context, DATABASE_NAME,
                null, DATABASE_VERSION) {

    companion object {
        private val DB_NAME = "ArgosDB"
        private val DB_VERSIOM = 1;


        private val DATABASE_VERSION = 112
        private val DATABASE_NAME = "ArgosDB.db"
        private val DATABASE_PATH = "/data/data/com./databases/"

        public val TABLE_USER = "user"
        public val COLUMN_USER_ID = "id_user"
        public val COLUMN_USER_EMAIL = "email"
        public val COLUMN_USER_CELLPHONE = "cellphone"

        public val TABLE_ROBOT = "robot"
        public val COLUMN_ROBOT_ID = "id_robot"
        public val COLUMN_ROBOT_MODEL = "model"
        public val COLUMN_ROBOT_NAME = "name"
        public val COLUMN_ROBOT_USER_ID = "id_user_robot"

        private val SQL_CREATE_TABLE_USER = "CREATE TABLE " + TABLE_USER + " (" +
                COLUMN_USER_ID + " INT PRIMARY KEY," +
                COLUMN_USER_EMAIL + " TEXT," +
                COLUMN_USER_CELLPHONE + " TEXT)"

        private val SQL_CREATE_TABLE_ROBOT = "CREATE TABLE " + TABLE_ROBOT + " (" +
                COLUMN_ROBOT_ID + " INT PRIMARY KEY," +
                COLUMN_ROBOT_MODEL + " TEXT," +
                COLUMN_ROBOT_NAME + " TEXT," +
                COLUMN_ROBOT_USER_ID + " INT)"
    }

    private val SQL_CREATE_INDEX_TABLE_ROBOT = ("CREATE INDEX " + COLUMN_ROBOT_USER_ID
            + " ON " + TABLE_ROBOT + "(" + COLUMN_ROBOT_USER_ID + ")")

    override fun onCreate(db: SQLiteDatabase)
    {
        db.execSQL(SQL_CREATE_TABLE_USER)
        db.execSQL(SQL_CREATE_TABLE_ROBOT)
    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Called when the database needs to be upgraded
    }

    //Inserting (Creating) data
    fun addUser(user: User): Boolean {
        //Create and/or open a database that will be used for reading and writing.
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_USER_ID, user.id)
        values.put(COLUMN_USER_EMAIL, user.email)
        values.put(COLUMN_USER_CELLPHONE, user.cellphone)
        val _success = db.insert(TABLE_USER, null, values)
        db.close()
        return (Integer.parseInt("$_success") != -1)
    }

    //get all users
    fun getUser(idUser: Int): User {
        var user: User = User();
        val db = readableDatabase
        val selectALLQuery = "SELECT * FROM $TABLE_USER WHERE $COLUMN_USER_ID = $idUser"
        val cursor = db.rawQuery(selectALLQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                user = cursorToUser(cursor)
            }
        }
        cursor.close()
        db.close()
        return user
    }

    fun cursorToUser(cursor: Cursor): User{
        var user = User()

        if (cursor != null && cursor.getCount() > 0) {
            user.id = cursor.getInt(0)
            user.email = cursor.getString(1)
            user.cellphone = cursor.getString(2)
        }
        return user
    }
}