package com.dicoding.capstone.data.dbHelper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.dicoding.capstone.data.user.User

class DataBaseHelper(context: Context) :SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "user.db"
        private const val TABLE_USER = "table_user"
        private const val USER_ID = "user_id"
        private const val FULL_NAME = "full_name"
        private const val USERNAME = "username"
        private const val PASSWORD = "password"
    }

    private val createTableUser = "CREATE TABLE $TABLE_USER(" +
            "$USER_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "$FULL_NAME TEXT, " +
            "$USERNAME TEXT, " +
            "$PASSWORD TEXT)"

    private val dropTableUser = "DROP TABLE IF EXISTS $TABLE_USER"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(createTableUser)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(dropTableUser)
    }

    fun registUser(user: User){
        val db = this.writableDatabase
        val value = ContentValues()
        value.put(FULL_NAME, user.fullname)
        value.put(USERNAME, user.username)
        value.put(PASSWORD, user.password)

        db.insert(TABLE_USER, null, value)
        db.close()
    }

    fun loginUser(username: String, password: String): Boolean{
        val columns = arrayOf(USER_ID)
        val db = this.readableDatabase
        val selection = "$USERNAME = ? AND $PASSWORD = ?"
        val selectionArgs = arrayOf(username, password)
        val cursor = db.query(TABLE_USER,
            columns,
            selection,
            selectionArgs,
            null,
            null,
            null)

        val cursorCount = cursor.count
        cursor.close()
        db.close()

        return cursorCount > 0
    }
}