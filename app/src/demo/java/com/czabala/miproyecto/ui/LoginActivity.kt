package com.czabala.miproyecto.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/** The demo uses local fictional data and never signs in to Firebase. */
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 80, 48, 48)
        }
        layout.addView(TextView(this).apply {
            text = "Artistify · Demo local\n\nCatálogo ficticio sin Firebase ni RapidAPI. Los cambios se guardan en este dispositivo."
            textSize = 22f
        })
        layout.addView(Button(this).apply {
            text = "Entrar en la demo"
            setOnClickListener {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
        })
        setContentView(layout)
    }
}
