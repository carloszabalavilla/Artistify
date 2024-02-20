package com.czabala.miproyecto

import android.app.Application
import com.czabala.miproyecto.core.AuthManager

class App : Application() {
    val auth by lazy { AuthManager() }
}