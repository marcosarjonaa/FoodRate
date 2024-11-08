package com.example.foodrate

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.foodrate.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var bindingLogin : ActivityLoginBinding
    private lateinit var intent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bindingLogin = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(bindingLogin.root)
        ViewCompat.setOnApplyWindowInsetsListener(bindingLogin.login) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        bindingLogin.entrar.setOnClickListener{
            val usuario = bindingLogin.editTextText.text.toString()
            val contrasena = bindingLogin.editTextTextPassword.text.toString()
            if(usuario=="marcos" && contrasena=="marcos"){
                intent = Intent(
                    this, MainActivity::class.java
                ).apply {
                    putExtra("usuario", usuario)
                    putExtra("contrasena", contrasena)
                }
                startActivity(intent)
            }else {
                Toast.makeText(this, "Has fallado", Toast.LENGTH_LONG).show()
            }
        }
    }
}