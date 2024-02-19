package com.czabala.miproyecto

import android.app.Application
import com.czabala.miproyecto.core.AuthManager
import com.czabala.miproyecto.core.FirestoreManager

//Incluir un modo desarrollador donde depurar los errores o mostrarlos
// a traves de toasta, y para activarlo ponerlo desde los 3 puntitos de la app.


class App : Application() {
    lateinit var auth: AuthManager
    lateinit var firestore: FirestoreManager

    override fun onCreate() {
        super.onCreate()
        auth = AuthManager(this)
        firestore = FirestoreManager()
    }
}