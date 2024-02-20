package com.czabala.miproyecto.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.czabala.miproyecto.core.AuthManager
import com.czabala.miproyecto.core.AuthRes
import com.czabala.miproyecto.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private val auth = AuthManager()
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        val googleSignLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            println("Result: $result")
            if (result.resultCode == Activity.RESULT_OK) {
                val accountTask = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = accountTask.getResult(ApiException::class.java)
                    if (account != null) {
                        // El inicio de sesión fue exitoso
                        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                        GlobalScope.launch {
                            try {
                                auth.googleSignInCredential(credential)
                                // Inicio de sesión con credenciales de Google exitoso
                                showSnackbar("Inicio de sesión correcto")
                                navigateToMainActivity()
                            } catch (e: Exception) {
                                // Error al iniciar sesión con credenciales de Google
                                showSnackbar("Error al iniciar sesión: ${e.message}")
                            }
                        }
                    } else {
                        // La cuenta está vacía, puede ser un problema
                        showSnackbar("La cuenta de Google está vacía")
                    }
                } catch (e: ApiException) {
                    // Error al obtener la cuenta de Google
                    showSnackbar("Error al iniciar sesión: ${e.message}")
                }
            } else {
                // El inicio de sesión fue cancelado por el usuario
                showSnackbar("Inicio de sesión cancelado")
            }
        }



        /*
                val googleSignLauncher = registerForActivityResult(
                    ActivityResultContracts.StartActivityForResult()
                ) { result ->
                    println("Result: $result")
                    when (val account =
                        auth.handleSignInResult(GoogleSignIn.getSignedInAccountFromIntent(result.data))) {
                        is AuthRes.Success -> {
                            val credential = GoogleAuthProvider.getCredential(account.data?.idToken, null)
                            GlobalScope.launch {
                                when (auth.googleSignInCredential(credential)) {
                                    is AuthRes.Success -> {
                                        showSnackbar("Inicio de sesión correcto")
                                        navigateToMainActivity()
                                    }

                                    is AuthRes.Error -> {
                                        showSnackbar("1.Error al iniciar sesión")
                                    }
                                }
                            }
                        }

                        is AuthRes.Error -> {
                            showSnackbar("2.Error al iniciar sesión")
                        }
                    }
                }
        */
        if (auth.getCurrentUser() != null) {
            navigateToMainActivity()
        }

        with(binding) {
            createAccount.setOnClickListener {
                val intent = Intent(this@LoginActivity, CreateAccountActivity::class.java)
                startActivity(intent)
            }

            loginButton.setOnClickListener {
                if (emailField.text.toString().isEmpty()) {
                    emailField.error = "El email no puede estar vacío"
                    return@setOnClickListener
                } else if (passwordField.text.toString().isEmpty()) {
                    passwordField.error = "La contraseña no puede estar vacía"
                    return@setOnClickListener
                }
                emailPassSignIn(emailField.text.toString(), passwordField.text.toString())
            }

            passwordForgotten.setOnClickListener {
                val intent = Intent(this@LoginActivity, ForgottenPasswordActivity::class.java)
                startActivity(intent)
            }

            googleLogin.setOnClickListener {
                println("Google login")
                auth.signInWithGoogle(googleSignLauncher, this@LoginActivity)
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun emailPassSignIn(eMail: String, password: String) {
        GlobalScope.launch(Dispatchers.IO) {
            when (auth.signInWithEmailAndPassword(
                eMail,
                password
            )) {
                is AuthRes.Success -> {
                    showSnackbar("Inicio de sesión correcto")
                    navigateToMainActivity()
                }

                is AuthRes.Error -> {
                    showSnackbar("Error al iniciar sesión")
                }
            }
        }
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}