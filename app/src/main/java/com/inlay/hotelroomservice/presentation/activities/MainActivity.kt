package com.inlay.hotelroomservice.presentation.activities

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.inlay.hotelroomservice.R
import com.inlay.hotelroomservice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val drawerToggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.nav_drawer_open,
            R.string.nav_drawer_close
        )
        binding.drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController = navHostFragment.navController
        binding.navigationView.setupWithNavController(navController)

        binding.navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item_hotels -> {
                    binding.toolbar.title = "Hotels"
                    binding.fabSearch.visibility = View.VISIBLE
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.item_user_hotels -> {
                    binding.toolbar.title = "User hotels"
                    binding.fabSearch.visibility = View.GONE
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.item_settings -> {
                    binding.toolbar.title = "Settings"
                    binding.fabSearch.visibility = View.GONE
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                else -> false
            }
        }
    }
}