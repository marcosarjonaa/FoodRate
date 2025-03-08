package com.example.foodrate.ui.views.activities.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.foodrate.R
import com.example.foodrate.data.usuarios.network.models.request.RequestLoginUsuario
import com.example.foodrate.data.usuarios.network.service.UsuarioApiService
import com.example.foodrate.databinding.ActivityLoginBinding
import com.example.foodrate.domain.Usuarios.models.Usuario
import com.example.foodrate.ui.views.activities.main.MainActivity
import com.example.foodrate.ui.views.activities.registrar.RegistrarActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var loginBinding: ActivityLoginBinding

    @Inject
    lateinit var usuarioApiService: UsuarioApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)
        loginListener()
    }

    private fun loginListener() {
        loginBinding.btnLoginInLogin.setOnClickListener{
            val dni = loginBinding.editUserLogin.text.toString().trim() //Hago el trim porque necesito que esté limpio de espacios
            val password = loginBinding.editPassLogin.text.toString().trim()

            if (!dni.isNullOrEmpty() && !password.isNullOrEmpty()){
                startLogin(dni, password)
            } else {
                Toast.makeText(this, "Algún campo está vacio", Toast.LENGTH_SHORT).show()
            }
        }

        loginBinding.btnRegisterInLogin.setOnClickListener {
            val intent = Intent(this, RegistrarActivity::class.java)
            startActivity(intent)
        }

        loginBinding.btnRecoverPass.setOnClickListener {
            Toast.makeText(this, "Funcion no implementada", Toast.LENGTH_SHORT).show()
        }

    }

    private fun startLogin(dni: String, password: String) {
        val requestUsuario = RequestLoginUsuario(dni, password )
        lifecycleScope.launch {
            val result = usuarioApiService.login(requestUsuario)
            result.fold(
                onSuccess = {token ->
                    val preferencias = getSharedPreferences("Preferencias", MODE_PRIVATE).edit()
                    preferencias.putString("token", token.token)
                    preferencias.apply()
                    Usuario.token = token.token

                    Toast.makeText(this@LoginActivity, "Login realizado adecuadamente", Toast.LENGTH_SHORT).show()

                    val intentMainActivity = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intentMainActivity)
                    finish()
                },
                onFailure = { fallo ->
                    Toast.makeText(this@LoginActivity, "Fallo: ${fallo.message}", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}
