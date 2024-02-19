package com.czabala.miproyecto.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.czabala.miproyecto.App
import com.czabala.miproyecto.core.AuthRes
import com.czabala.miproyecto.databinding.ActivityForgottenPasswordBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ForgottenPasswordActivity : AppCompatActivity() {

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityForgottenPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRecuperaContrasena.setOnClickListener {
            GlobalScope.launch {
                when ((application as App).auth.resetPassword(binding.etEmail.text.toString())) {
                    is AuthRes.Success -> {
                        Snackbar.make(
                            binding.root,
                            "Correo enviado correctamente",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        finish()
                    }

                    is AuthRes.Error -> {
                        Snackbar.make(
                            binding.root,
                            "Error al enviar el correo",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}