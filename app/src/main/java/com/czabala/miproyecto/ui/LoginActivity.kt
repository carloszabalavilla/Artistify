package com.czabala.miproyecto.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.czabala.miproyecto.core.AuthManager
import com.czabala.miproyecto.core.AuthRes
import com.czabala.miproyecto.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.DelicateCoroutinesApi

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private val auth = AuthManager(this)//(application as App).auth
    private lateinit var binding: ActivityLoginBinding
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        val googleSignLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
            when (val account =
                auth.handleSignInResult(GoogleSignIn.getSignedInAccountFromIntent(result.data))) {
                is AuthRes.Success -> {
                    val credential = GoogleAuthProvider.getCredential(account.data?.idToken, null)
                    GlobalScope.launch {
                        when (auth.googleSignInCredential(credential)) {
                            is AuthRes.Success -> {
                                Snackbar.make(
                                    binding.root,
                                    "Inicio de sesión correcto",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(intent)
                            }

                            is AuthRes.Error -> {
                                Snackbar.make(
                                    binding.root,
                                    "Error al iniciar sesión",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
                is AuthRes.Error -> {
                    Snackbar.make(binding.root, "Error al iniciar sesión", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        }


        if (auth.getCurrentUser() != null){
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
        }

        with(binding){
            createAccount.setOnClickListener {
                val intent = Intent(this@LoginActivity, CreateAccountActivity::class.java)
                startActivity(intent)
            }

            loginButton.setOnClickListener {
                emailPassSignIn(emailField.text.toString(), passwordField.text.toString())
            }
/*
            passwordForgotten.setOnClickListener {
                val intent = Intent(this@LoginActivity, RecuperaContrasena::class.java)
                startActivity(intent)
            }
*/
            googleLogin.setOnClickListener {
                auth.signInWithGoogle(googleSignLauncher)
            }
        }

    }

    private fun emailPassSignIn(eMail: String, password: String) {
        if (!eMail.isNullOrEmpty() && !password.isNullOrEmpty()) {
            GlobalScope.launch(Dispatchers.IO) {
                when (auth.signInWithEmailAndPassword(
                    eMail,
                    password
                )){
                    is AuthRes.Success -> {
                        Snackbar.make(binding.root, "Inicio de sesión correcto", Snackbar.LENGTH_SHORT).show()
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                    }
                    is AuthRes.Error -> {
                        Snackbar.make(binding.root, "Error al iniciar sesión", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}