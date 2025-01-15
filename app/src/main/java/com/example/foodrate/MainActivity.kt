package com.example.foodrate

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.renderscript.ScriptGroup.Binding
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.foodrate.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var ficheroCompartido: SharedPreferences
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        iniciarPreferenciasCompartidas()
        auth = FirebaseAuth.getInstance()
        val toolbar = binding.appBarConfiguration.toolbar
        setSupportActionBar(toolbar)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.fragmentComidas, R.id.fragmentInicio, R.id.fragmentRestaurantes, R.id.fragmentConfiguration),
            binding.main
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.myNavView.setupWithNavController(navController)
        binding.myNavView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.fragmentInicio -> {
                    navController.navigate(R.id.fragmentInicio)
                    true
                }
                R.id.fragmentComidas -> {
                    navController.navigate(R.id.fragmentComidas)
                    true
                }
                R.id.fragmentRestaurantes -> {
                    navController.navigate(R.id.fragmentRestaurantes)
                    true
                }
                R.id.fragmentConfiguration -> {
                    navController.navigate(R.id.fragmentConfiguration)
                    true
                }
                R.id.login -> {
                    logout()
                    true
                }
                else -> false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.fragmentInicio -> {
                navController.navigate(R.id.fragmentInicio)
                true
            }
            R.id.fragmentConfiguration -> {
                navController.navigate(R.id.fragmentConfiguration)
                true
            }
            R.id.login -> {
                logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun iniciarPreferenciasCompartidas() {
        val nombreFicheroCompartido = getString(R.string.ficheroCompartido)
        ficheroCompartido = getSharedPreferences(nombreFicheroCompartido, MODE_PRIVATE)
    }

    private fun logout() {
        auth.signOut()
        val loginIntent = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(loginIntent)
    }
}