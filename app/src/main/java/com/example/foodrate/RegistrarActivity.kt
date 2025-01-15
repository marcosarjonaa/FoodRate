package com.example.foodrate

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
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.auth

class RegistrarActivity : AppCompatActivity() {
    private lateinit var btnRegister: Button
    private lateinit var btnLastRegister: Button
    private lateinit var editUser: EditText
    private lateinit var editPassword: EditText
    private lateinit var editRepeatPassword: EditText
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        init()
        start()
    }

    private fun init() {
        btnRegister = findViewById(R.id.btn_register_in_register)
        btnLastRegister = findViewById(R.id.btn_last_register)
        editUser = findViewById(R.id.edit_user_register)
        editPassword = findViewById(R.id.edit_pass_register)
        editRepeatPassword = findViewById(R.id.pass_register_repeat_in_register)

        auth = Firebase.auth

    }


    private fun start() {
        btnRegister.setOnClickListener {
            val email = editUser.text.toString()
            val pass = editPassword.text.toString()
            val repeatPass = editRepeatPassword.text.toString()
            if (pass != repeatPass
                || email.isEmpty()
                || pass.isEmpty()
                || repeatPass.isEmpty()
            )
                Toast.makeText(this, "Campos vacíos y/o password diferentes", Toast.LENGTH_LONG)
                    .show()
            else {
                registerUser(email, pass) { result, msg ->
                    Toast.makeText(this@RegistrarActivity, msg, Toast.LENGTH_LONG).show()
                    if (result)
                        startActivityLogin()
                }
            }
        }

        btnLastRegister.setOnClickListener {
            // Como la lambda tiene un parámetro view, el this es el Activity
                view ->
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun startActivityLogin() {
        //Tengo que lanzar un intent con el Activity a loguear.
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() //No quiero que sigua el Activity del registro.

    }

    private fun registerUser(email: String, pass: String, onResult: (Boolean, String) -> Unit) {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) { taskAssin ->
                if (taskAssin.isSuccessful) {
                    //enviaremos un email de confirmación
                    val user = auth.currentUser
                    user?.sendEmailVerification()
                        ?.addOnCompleteListener { taskVerification ->
                            var msg = ""
                            if (taskVerification.isSuccessful)
                                msg = "Usuario Registrado Ok. Verifique su correo"
                            else
                                msg =
                                    "Usuario Registrado Ok. ${taskVerification.exception?.message}"
                            auth.signOut() //tiene que verificar antes el email
                            onResult(true, msg)
                        }
                        ?.addOnFailureListener { exception ->
                            Log.e(
                                "ActivityRegister",
                                "Fallo al enviar correo de verificación: ${exception.message}"
                            )
                            onResult(
                                false,
                                "No se pudo enviar el correo de verificación: ${exception.message}"
                            )
                        }

                } else {
                    try {
                        throw taskAssin.exception ?: Exception("Error desconocido")
                    } catch (e: FirebaseAuthUserCollisionException) {
                        onResult(false, "Ese usuario ya existe")
                    } catch (e: FirebaseAuthWeakPasswordException) {
                        onResult(false, "La contraseña es débil: ${e.reason}")
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        onResult(false, "El email proporcionado, no es válido")
                    } catch (e: Exception) {
                        onResult(false, e.message.toString())
                    }

                }
            }


    }
}