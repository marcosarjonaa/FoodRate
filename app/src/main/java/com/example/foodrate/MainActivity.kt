package com.example.foodrate

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodrate.controller.Controller
import com.example.foodrate.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    //private lateinit var ficheroCompartido : SharedPreferences
    lateinit var controller: Controller
    lateinit var bindingMain : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindingMain = ActivityMainBinding.inflate(layoutInflater);
        setContentView(bindingMain.root);
        init()
        
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