package com.czabala.miproyecto.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.czabala.miproyecto.R
import com.czabala.miproyecto.core.AuthManager
import com.czabala.miproyecto.core.FirestoreManager

class MainActivity : AppCompatActivity() {
    private val auth = AuthManager(this)
    private val firestoreManager = FirestoreManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.baseline_music_note_24)
        }
    }

    private fun checkFirestoreConnection() {
        firestoreManager.checkConnection(
            onSuccess = {
                // La conexión con Firestore y la web es exitosa
                showToast("Connection successful")
            },
            onFailure = {
                // La conexión con Firestore y la web ha fallado
                showToast("Connection failed")
            }
        )
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}