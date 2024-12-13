package com.example.foodrate

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodrate.controller.Controller
import com.example.foodrate.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var ficheroCompartido : SharedPreferences
    lateinit var controller: Controller
    lateinit var bindingMain : ActivityMainBinding
    private lateinit var btnLogout: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindingMain = ActivityMainBinding.inflate(layoutInflater);
        setContentView(bindingMain.root);
        iniciarPreferenciasCompartidas();
        init();

        btnLogout = findViewById(R.id.btn_logout)

        btnLogout.setOnClickListener {
            logout()
        }

    }
    private fun logout() {
        val sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun iniciarPreferenciasCompartidas(){
        val nombreFicheroCompartido = getString(R.string.ficheroCompartido)
        this.ficheroCompartido = getSharedPreferences(nombreFicheroCompartido, MODE_PRIVATE)
    }

    private fun init(){
        initRecyclerView()
        controller = Controller(this)
        controller.setAdapter()
    }

    private fun initRecyclerView() {
        bindingMain.myRecyclerView.layoutManager = LinearLayoutManager(this)
    }
}