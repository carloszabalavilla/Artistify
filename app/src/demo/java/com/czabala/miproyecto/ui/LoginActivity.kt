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
import com.czabala.miproyecto.R
import com.google.android.material.button.MaterialButton

/** Offline entry screen, with density-independent spacing and scrollable large text. */
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = getString(R.string.app_name)
        fun dp(value: Int) = (value * resources.displayMetrics.density).toInt()
        val ink = Color.parseColor("#302541")
        val accent = Color.parseColor("#68469B")
        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(28), dp(40), dp(28), dp(32))
        }
        fun label(text: Int, size: Float, color: Int, space: Int, bold: Boolean = false) {
            content.addView(TextView(this).apply {
                setText(text); textSize = size; setTextColor(color)
                if (bold) setTypeface(typeface, Typeface.BOLD)
                setPadding(0, 0, 0, dp(space))
            })
        }
        label(R.string.demo_eyebrow, 12f, accent, 22, true)
        label(R.string.demo_title, 34f, ink, 20, true)
        label(R.string.demo_intro, 18f, ink, 32)
        val panel = TextView(this).apply {
            setText(R.string.demo_explanation)
            textSize = 16f; setTextColor(ink); setPadding(dp(22), dp(22), dp(22), dp(22))
            background = GradientDrawable().apply { setColor(Color.WHITE); cornerRadius = dp(18).toFloat() }
        }
        content.addView(panel)
        content.addView(MaterialButton(this).apply {
            setText(R.string.demo_enter)
            setTextColor(Color.WHITE); backgroundTintList = android.content.res.ColorStateList.valueOf(accent)
            cornerRadius = dp(12); minHeight = dp(56); isAllCaps = false
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply { topMargin = dp(28); bottomMargin = dp(20) }
            setOnClickListener { startActivity(Intent(this@LoginActivity, MainActivity::class.java)); finish() }
        })
        label(R.string.demo_footer, 12f, Color.parseColor("#6C6278"), 0)
        setContentView(ScrollView(this).apply { setBackgroundColor(Color.parseColor("#F6F3FA")); isFillViewport = true; addView(content) })
    }
}
