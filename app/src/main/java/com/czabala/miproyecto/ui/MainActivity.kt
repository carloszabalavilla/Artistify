package com.czabala.miproyecto.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.czabala.miproyecto.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.baseline_music_note_24)
        }
    }
}