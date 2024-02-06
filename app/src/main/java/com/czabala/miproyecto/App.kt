package com.czabala.miproyecto

import android.app.Application
import androidx.room.Room
import com.czabala.miproyecto.model.db.ArtistDatabase
import com.pjurado.firebase2324.core.AuthManager
import com.pjurado.firebase2324.core.FirestoreManager

class App: Application() {
    /*lateinit var db: ArtistDatabase
    override fun onCreate() {
        super.onCreate()
        db = Room
            .databaseBuilder(this,ArtistDatabase::class.java,"Artist-db")
            .build()
    }*/
    lateinit var auth: AuthManager
    lateinit var firestore: FirestoreManager

    override fun onCreate() {
        super.onCreate()
        auth = AuthManager(this)
        firestore = FirestoreManager(this)
    }

}