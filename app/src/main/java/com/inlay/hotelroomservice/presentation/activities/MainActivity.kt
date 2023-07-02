package com.inlay.hotelroomservice.presentation.activities

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
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

        setupHeader(false)

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
                    navController.navigate(R.id.hotelsFragment)
                    true
                }

                R.id.item_user_hotels -> {
                    binding.toolbar.title = "User hotels"
                    binding.fabSearch.visibility = View.GONE
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    navController.navigate(R.id.fragmentUserList)
                    true
                }

                R.id.item_settings -> {
                    binding.toolbar.title = "Settings"
                    binding.fabSearch.visibility = View.GONE
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    navController.navigate(R.id.fragmentSettings)
                    true
                }

                else -> false
            }
        }

        onBackPressedDispatcher.addCallback(this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        binding.drawerLayout.closeDrawer(GravityCompat.START)
                    } else if (navController.currentDestination?.id == R.id.hotelsFragment) {
                        finish()
                    } else {
                        binding.toolbar.title = "Hotels"
                        binding.fabSearch.visibility = View.VISIBLE
                        binding.navigationView.setCheckedItem(R.id.item_hotels)
                        navController.navigate(R.id.hotelsFragment)
                    }
                }
            }
        )
        binding.lifecycleOwner = this
    }

    private fun setupHeader(isUserLogged: Boolean) {
        val headerView = binding.navigationView.getHeaderView(0)
        val headerImage = headerView.findViewById<ImageView>(R.id.header_image)
        val headerUserName = headerView.findViewById<TextView>(R.id.tv_user_name)
        val headerMail = headerView.findViewById<TextView>(R.id.tv_user_mail)

        if (!isUserLogged) {
            headerImage.setImageResource(R.drawable.baseline_person_24)
            headerUserName.text = this.resources.getText(R.string.header_profile)
        }
        headerImage.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            binding.toolbar.title = "Profile"
            navController.navigate(R.id.fragmentProfile)
        }

        headerUserName.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            binding.toolbar.title = "Profile"
            navController.navigate(R.id.fragmentProfile)
        }
    }
}