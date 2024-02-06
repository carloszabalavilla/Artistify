package com.czabala.miproyecto.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.czabala.miproyecto.App
import com.czabala.miproyecto.R
import com.czabala.miproyecto.databinding.ActivityCrearCuentaBinding
import com.google.android.material.snackbar.Snackbar
import com.pjurado.firebase2324.core.AuthRes
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CrearCuenta : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityCrearCuentaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        with(binding) {
            btnRegistrar.setOnClickListener {
                signUp(etEmail.text.toString(), etPassword.text.toString())
            }
            tvIniciaSesion.setOnClickListener {
                finish()
            }
        }
    }

    private fun ActivityCrearCuentaBinding.signUp(eMail: String, password: String) {
        if (!eMail.isNullOrEmpty() && !password.isNullOrEmpty()) {
            GlobalScope.launch {
                when ((application as App).auth.createUserWithEmailAndPassword(
                    eMail,
                    password
                )){
                    is AuthRes.Success<*> -> {
                        Snackbar.make(root, "Usuario creado correctamente", Snackbar.LENGTH_SHORT)
                            .show()
                        finish()
                    }
                    is AuthRes.Error -> {
                        Snackbar.make(root, "Error al crear el usuario", Snackbar.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
        else{
            Snackbar.make(root, "Debes llenar todos los campos", Snackbar.LENGTH_SHORT).show()
        }
    }
}