package com.example.foodrate

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.foodrate.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var ficheroCompartido : SharedPreferences
    private lateinit var bindingMain : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        bindingMain = ActivityMainBinding.inflate(layoutInflater);
        setContentView(bindingMain.root);
        initSharedPreferences();
        mostrarUsuCon();
        
    }

    private fun mostrarUsuCon() {
        val usuario = intent.getStringExtra("usuario");
        bindingMain.tvUsuario.text = usuario;
        val contrasena = intent.getStringExtra("contrasena");
        bindingMain.tvContrasena.text = contrasena;
    }

    private fun initSharedPreferences() {
        val ficheroCompartidoNombre=getString(R.string.ficheroCompartido);
        this.ficheroCompartido=getSharedPreferences(ficheroCompartidoNombre, MODE_PRIVATE);
    }
}