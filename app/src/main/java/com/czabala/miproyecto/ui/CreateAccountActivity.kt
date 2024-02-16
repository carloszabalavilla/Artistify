package com.czabala.miproyecto.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.czabala.miproyecto.App
import com.czabala.miproyecto.core.AuthRes
import com.czabala.miproyecto.databinding.ActivityCreateAccountBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CreateAccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        with(binding) {
            signupButton.setOnClickListener {
                signUp(emailField.text.toString(), passwordField.text.toString())
            }
            backToLogin.setOnClickListener {
                finish()
            }
        }
    }

    private fun ActivityCreateAccountBinding.signUp(eMail: String, password: String) {
        if (eMail.isNotEmpty() && password.isNotEmpty()) {
            GlobalScope.launch {
                when ((application as App).auth.createUserWithEmailAndPassword(
                    eMail,
                    password
                )) {
                    is AuthRes.Success -> {
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
        } else {
            Snackbar.make(root, "Debes llenar todos los campos", Snackbar.LENGTH_SHORT).show()
        }
    }
}