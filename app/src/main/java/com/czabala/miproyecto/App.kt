package com.czabala.miproyecto

import android.app.Application
import com.czabala.miproyecto.core.AuthManager
import com.czabala.miproyecto.core.FirestoreManager


class App: Application() {
    lateinit var auth: AuthManager
    lateinit var firestore: FirestoreManager

    override fun onCreate() {
        super.onCreate()
        auth = AuthManager(this)
        firestore = FirestoreManager(this)
    }
}