package com.example.foodrate.ui.views.activities.registrar

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.foodrate.R
import com.example.foodrate.data.usuarios.models.request.RequestRegisterUsuario
import com.example.foodrate.data.usuarios.network.models.request.RequestLoginUsuario
import com.example.foodrate.data.usuarios.network.service.UsuarioApiService
import com.example.foodrate.databinding.ActivityRegistrarBinding
import com.example.foodrate.ui.views.activities.login.LoginActivity
import com.example.foodrate.ui.views.activities.main.MainActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class RegistrarActivity : AppCompatActivity() {
    private lateinit var registrarBinding : ActivityRegistrarBinding

    @Inject
    lateinit var usuarioApiService: UsuarioApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registrarBinding = ActivityRegistrarBinding.inflate(layoutInflater)
        setContentView(registrarBinding.root)
        registerListeners()

    }

    private fun registerListeners(){
        registrarBinding.btnRegisterInRegister.setOnClickListener {
            val dni = registrarBinding.editUserRegister.text.toString().trim()
            val password = registrarBinding.editPassRegister.text.toString().trim()

            if (!dni.isNullOrEmpty() && !password.isNullOrEmpty()){
                registrarUsuario(dni, password)
            }else {
                Toast.makeText(this, "Alguno de los campos está vacio", Toast.LENGTH_SHORT).show()
            }
        }

        registrarBinding.btnLastRegister.setOnClickListener{
            val intentLogin = Intent(this, LoginActivity::class.java)
            startActivity(intentLogin)
            finish()
        }
    }

    private fun registrarUsuario(dni: String, password: String ) {
        val requestRegisterUsuario = RequestRegisterUsuario(dni, "marcos@gmail.com", password, "morco", "")

        lifecycleScope.launch {
            val resultado = usuarioApiService.register(requestRegisterUsuario)
            resultado.fold(
            onSuccess = { respuesta ->
                Toast.makeText(this@RegistrarActivity, "Te has registrado", Toast.LENGTH_SHORT).show()
                val intentMain = Intent(this@RegistrarActivity, LoginActivity::class.java)
                startActivity(intentMain)
            },
            onFailure = { fallo ->
                val mensajeError = fallo.message ?: "Fallo en el registro"
                Toast.makeText(this@RegistrarActivity, "El error es: $mensajeError", Toast.LENGTH_SHORT).show()
            }
            )
        }
    }
}