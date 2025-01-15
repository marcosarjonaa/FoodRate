package com.example.foodrate

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class LoginActivity : AppCompatActivity() {
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button
    private lateinit var btnRecoverPass: Button
    private lateinit var editUser: EditText
    private lateinit var editPassword: EditText
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = FirebaseAuth.getInstance()
        if (estaLogueado()) {
            navigateToMainActivity()
            finish()
        }
        init()
        start()
    }

    private fun init() {
        btnLogin = findViewById(R.id.btn_login_in_login)
        btnRegister = findViewById(R.id.btn_register_in_login)
        btnRecoverPass = findViewById(R.id.btn_recover_pass)
        editUser = findViewById(R.id.edit_user_login)
        editPassword = findViewById(R.id.edit_pass_login)
    }

    private fun start() {
        btnLogin.setOnClickListener {
            val user = editUser.text.toString()
            val pass = editPassword.text.toString()

            if (user.isNotEmpty() && pass.isNotEmpty())
                startLogin(user, pass) { result, msg ->
                    Toast.makeText(this@LoginActivity, msg, Toast.LENGTH_LONG).show()
                    if (result) {
                        salvarLogueo(user) // Save login state
                        navigateToMainActivity()
                        finish()
                    }
                }
            else
                Toast.makeText(this, "Tienes algún campo vacío", Toast.LENGTH_LONG).show()
        }

        btnRegister.setOnClickListener {
            val intent = Intent(this, RegistrarActivity::class.java)
            startActivity(intent)
        }

        btnRecoverPass.setOnClickListener {
            val user = editUser.text.toString()
            if (user.isNotEmpty())
                recoverPassword(user) { result, msg ->
                    Toast.makeText(this@LoginActivity, msg, Toast.LENGTH_LONG).show()
                    if (!result) editUser.setText("")
                }
            else
                Toast.makeText(this, "Debes rellenar el campo email", Toast.LENGTH_LONG).show()
        }
    }

    private fun recoverPassword(email: String, onResult: (Boolean, String) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { taskResetEmail ->
                if (taskResetEmail.isSuccessful) {
                    onResult(true, "Acabamos de enviarte un email con la nueva password")
                } else {
                    val msg = try {
                        throw taskResetEmail.exception ?: Exception("Error de reseteo inesperado")
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        "El formato del email es incorrecto"
                    } catch (e: Exception) {
                        e.message.toString()
                    }
                    onResult(false, msg)
                }
            }
    }

    private fun startLogin(user: String, pass: String, onResult: (Boolean, String) -> Unit) {
        auth.signInWithEmailAndPassword(user, pass)
            .addOnCompleteListener { taskAssin ->
                if (taskAssin.isSuccessful) {
                    val posibleUser = auth.currentUser
                    if (posibleUser?.isEmailVerified == true) {
                        onResult(true, "Usuario Logueado satisfactoriamente")
                    } else {
                        auth.signOut()
                        onResult(false, "Debes verificar tu correo antes de loguearte")
                    }
                } else {
                    val msg = try {
                        throw taskAssin.exception ?: Exception("Error desconocido")
                    } catch (e: FirebaseAuthInvalidUserException) {
                        "El usuario tiene problemas por haberse borrado o desabilitado"
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        if (e.message?.contains("There is no user record corresponding to this identifier") == true) {
                            "El usuario no existe"
                        } else "Contraseña incorrecta"
                    } catch (e: Exception) {
                        e.message.toString()
                    }
                    onResult(false, msg)
                }
            }
    }

    private fun salvarLogueo(email: String) {
        val sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("user_email", email)
        editor.putBoolean("is_logged_in", true)
        editor.apply()
    }

    private fun estaLogueado(): Boolean {
        val sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE)
        return sharedPreferences.getBoolean("is_logged_in", false)
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
