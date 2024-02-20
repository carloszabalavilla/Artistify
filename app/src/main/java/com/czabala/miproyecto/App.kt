package com.czabala.miproyecto

import android.app.Application
import com.czabala.miproyecto.core.AuthManager
import com.czabala.miproyecto.core.FirestoreManager

//Incluir un modo desarrollador donde depurar los errores o mostrarlos
// a traves de toasta, y para activarlo ponerlo desde los 3 puntitos de la app.
//Agregar error de sin internet, y mostrarlo en el toast.


class App : Application() {
    val auth by lazy { AuthManager() }
    val firestore by lazy { FirestoreManager() }

}