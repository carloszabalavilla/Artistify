package com.czabala.miproyecto.model.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        // Crea la tabla en la base de datos
        db.execSQL(CREATE_TABLE_QUERY)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Maneja las actualizaciones de la base de datos
    }

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "MyDatabase"

        private const val TABLE_NAME = "artist"
        private const val COLUMN_ID = "_id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_URL = "external_url"
        private const val COLUMN_IMG = "img_url"
        private const val COLUMN_SONG = "song_url"

        private const val CREATE_TABLE_QUERY =
            "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_NAME TEXT, $COLUMN_ID TEXT, $COLUMN_URL TEXT, $COLUMN_IMG TEXT, $COLUMN_SONG TEXT )"
    }

    fun getTableName(): String {
        return TABLE_NAME
    }
    fun getColumnId(): String {
        return COLUMN_ID
    }
    fun getColumnName(): String {
        return COLUMN_NAME
    }

    fun getColumnUrl(): String {
        return COLUMN_URL
    }

    fun getColumnImg(): String {
        return COLUMN_IMG
    }

    fun getColumnSong(): String {
        return COLUMN_SONG
    }
}