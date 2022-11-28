package com.example.pickupsports

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pickupsports.databinding.ActivityMainActivtyBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivty : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainActivtyBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainActivtyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.nav_host_fragment_content_main_activty)
        appBarConfiguration = AppBarConfiguration(navController.graph)

        auth = FirebaseAuth.getInstance()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment-> {
                    bottomNavigationView.visibility = View.GONE
                }
                R.id.registerFragment -> {
                    bottomNavigationView.visibility = View.GONE
                }
                else -> {
                    bottomNavigationView.visibility = View.VISIBLE
                }
            }
        }
        changeFragmentOnSelect()
    }

    fun changeFragmentOnSelect(){
        binding.bottomNavigationView.setOnItemSelectedListener {

            when(it.itemId){
                R.id.home -> navController.navigate(R.id.HomeFragment)
                R.id.message ->  navController.navigate(R.id.MessageFragment)
                R.id.create -> navController.navigate(R.id.CreateEvent)
                R.id.upcoming -> navController.navigate(R.id.UpcomingFragment)
                R.id.acc -> navController.navigate(R.id.profileFragment)

                else->{

                }
            }

            true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main_activty)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}