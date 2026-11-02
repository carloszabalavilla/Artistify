package com.czabala.miproyecto.ui

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

/** Offline entry screen, with density-independent spacing and scrollable large text. */
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Artistify"
        fun dp(value: Int) = (value * resources.displayMetrics.density).toInt()
        val ink = Color.parseColor("#302541")
        val accent = Color.parseColor("#68469B")
        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(28), dp(40), dp(28), dp(32))
        }
        fun label(text: String, size: Float, color: Int, space: Int, bold: Boolean = false) {
            content.addView(TextView(this).apply {
                this.text = text; textSize = size; setTextColor(color)
                if (bold) setTypeface(typeface, Typeface.BOLD)
                setPadding(0, 0, 0, dp(space))
            })
        }
        label("TU CATÁLOGO MUSICAL", 12f, accent, 22, true)
        label("La música empieza\npor sus historias.", 34f, ink, 20, true)
        label("Artistas, canciones y un lugar para descubrir sus detalles.", 18f, ink, 32)
        val panel = TextView(this).apply {
            text = "DEMO LOCAL\n\nUn catálogo ficticio que puedes consultar y editar. Los cambios se guardan en este dispositivo.\n\nFunciona sin cuenta y sin conexión."
            textSize = 16f; setTextColor(ink); setPadding(dp(22), dp(22), dp(22), dp(22))
            background = GradientDrawable().apply { setColor(Color.WHITE); cornerRadius = dp(18).toFloat() }
        }
        content.addView(panel)
        content.addView(MaterialButton(this).apply {
            text = "Entrar en la demo"
            setTextColor(Color.WHITE); backgroundTintList = android.content.res.ColorStateList.valueOf(accent)
            cornerRadius = dp(12); minHeight = dp(56); isAllCaps = false
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply { topMargin = dp(28); bottomMargin = dp(20) }
            setOnClickListener { startActivity(Intent(this@LoginActivity, MainActivity::class.java)); finish() }
        })
        label("Datos ficticios · sin servicios externos · sin reproducción de audio", 12f, Color.parseColor("#6C6278"), 0)
        setContentView(ScrollView(this).apply { setBackgroundColor(Color.parseColor("#F6F3FA")); isFillViewport = true; addView(content) })
    }
}
