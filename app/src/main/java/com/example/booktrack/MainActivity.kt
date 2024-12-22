package com.example.booktrack

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.booktrack.utils.NotificationHelper
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Lifecycle", "MainActivity onCreate called")
        setContentView(R.layout.activity_main)

        // Créer le canal de notification au lancement de l'application
        NotificationHelper.createNotificationChannel(this)

        // Créer le canal de notification au lancement de l'application (si nécessaire)
        NotificationHelper.createNotificationChannel(this)

        // Envoie la notification après 5 secondes pour tester
        Handler(Looper.getMainLooper()).postDelayed({
            NotificationHelper.sendNotification(this, "C'est le moment de lire !")
        }, 5000) // Envoi après 5 secondes

        // Initialiser la Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Initialiser NavHostFragment et NavController
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Configurer l'AppBarConfiguration pour les fragments principaux
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.libraryFragment,
                R.id.searchFragment,
                R.id.settingsFragment
            )
        )


        // Configurer la navigation avec le BottomNavigationView
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        // Lier le BottomNavigationView avec le NavController
        NavigationUI.setupWithNavController(bottomNavigationView, navController)

        // Synchroniser la Toolbar avec le NavController
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }
                R.id.menu_back -> {
                    onBackPressed() // Ou la logique pour revenir en arrière
                    true
                }
                R.id.menu_library -> {
                    navController.navigate(R.id.libraryFragment)
                    true
                }
                R.id.menu_search_book -> {
                    navController.navigate(R.id.searchFragment)
                    true
                }
                R.id.menu_settings -> {
                    navController.navigate(R.id.settingsFragment)
                    true
                }
                else -> false
            }
        }
    }

    // Gérer la logique de retour au niveau du menu
    override fun onBackPressed() {
        if (!navController.navigateUp()) {
            super.onBackPressed() // Si aucun fragment ne peut être retourné, on appelle la méthode par défaut
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp()
    }


}
