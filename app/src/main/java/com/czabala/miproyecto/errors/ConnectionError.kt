package com.czabala.miproyecto.errors

import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext

//Tratar de pasar el contexto de un fragment(si lo hubiera)
class ConnectionError : Exception(), ErrorLauncher() {
    override fun throwToast() {
        Toast.makeText(
            requireContext(),
            "Artista encontrado",
            Toast.LENGTH_LONG
        ).show()
    }


}