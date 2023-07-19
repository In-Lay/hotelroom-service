package com.inlay.hotelroomservice.presentation.activities

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.inlay.hotelroomservice.R
import com.inlay.hotelroomservice.databinding.ActivityMainBinding
import com.inlay.hotelroomservice.extensions.isNetworkAvailable
import com.inlay.hotelroomservice.presentation.DrawerProvider
import com.inlay.hotelroomservice.presentation.models.hotelsitem.DatesModel
import com.inlay.hotelroomservice.presentation.viewmodels.hotels.HotelsViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Calendar

class MainActivity : AppCompatActivity(), DrawerProvider {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val hotelsViewModel: HotelsViewModel by viewModel()
    private val dateFormat: SimpleDateFormat by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        binding.toolbar.title = "Hotels"
//        setSupportActionBar(binding.toolbar)

        setupNavigationDrawer()
        setupNavigation()
        setupHeader(false)

//        setupBackPressed()

        val dummyDates = getDummyDates()
        hotelsViewModel.getHotelsRepo(
            isNetworkAvailable(),
            "60763",
            dummyDates.checkInDate,
            dummyDates.checkOutDate
        )

        binding.fabSearch.setOnClickListener {
//            binding.navigationView.setCheckedItem(R.id.item_hotels)
//            supportActionBar?.hide()
            navController.navigate(R.id.fragmentSearch)
//            binding.fabSearch.visibility = View.GONE
        }

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
//            Log.d("destinationTag", "destination.route: ${destination.route}")
//            Log.d("destinationTag", "destination.displayName: ${destination.displayName}")
            when (destination.id) {
                R.id.hotelsFragment -> binding.fabSearch.visibility = View.VISIBLE
                else -> binding.fabSearch.visibility = View.GONE
            }
        }
        binding.lifecycleOwner = this
    }

    private fun setupNavigationDrawer() {
//        val drawerToggle = ActionBarDrawerToggle(
//            this,
//            binding.drawerLayout,
//            binding.toolbar,
//            R.string.nav_drawer_open,
//            R.string.nav_drawer_close
//        )
//        binding.drawerLayout.addDrawerListener(drawerToggle)
//        drawerToggle.syncState()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController = navHostFragment.navController

        binding.navigationView.setupWithNavController(navController)
//        binding.navigationView.setCheckedItem(R.id.item_hotels)
    }

//    private fun setupBackPressed() {
//        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
//                    binding.drawerLayout.closeDrawer(GravityCompat.START)
//                } else if (navController.currentDestination?.id == R.id.hotelsFragment) {
//                    finish()
//                } else if (navController.currentDestination?.id == R.id.fragmentSearch) {
//                    setSupportActionBar(binding.toolbar)
////                    binding.toolbar.title = "Hotels"
//                    supportActionBar?.show()
////                    setupNavigationDrawer()
////                    setupNavigation()
//                    binding.navigationView.setCheckedItem(R.id.item_hotels)
////                    binding.fabSearch.visibility = View.VISIBLE
//                    navController.navigate(R.id.hotelsFragment)
//                } else {
////                    binding.toolbar.title = "Hotels"
////                    binding.fabSearch.visibility = View.VISIBLE
//                    binding.navigationView.setCheckedItem(R.id.item_hotels)
//                    navController.navigate(R.id.hotelsFragment)
//                }
//            }
//        })
//    }

    private fun setupNavigation() {
        binding.navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item_hotels -> {
//                    binding.navigationView.setCheckedItem(R.id.item_hotels)
//                    binding.toolbar.title = "Hotels"
//                    binding.fabSearch.visibility = View.VISIBLE
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    navController.navigate(R.id.hotelsFragment)
                    true
                }

                R.id.item_user_stays -> {
//                    binding.navigationView.setCheckedItem(R.id.item_user_stays)
//                    binding.toolbar.title = "My Stays"
//                    binding.fabSearch.visibility = View.GONE
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    navController.navigate(R.id.fragmentUserStays)
                    true
                }

                R.id.item_settings -> {
//                    binding.navigationView.setCheckedItem(R.id.item_settings)
//                    binding.toolbar.title = "Settings"
//                    binding.fabSearch.visibility = View.GONE
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    navController.navigate(R.id.fragmentSettings)
                    true
                }

                else -> false
            }
        }
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
//            binding.toolbar.title = "Profile"
            navController.navigate(R.id.fragmentProfile)
        }

        headerUserName.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
//            binding.toolbar.title = "Profile"
            navController.navigate(R.id.fragmentProfile)
        }
    }

    override fun getDrawerLayout(): DrawerLayout = binding.drawerLayout

    private fun getDummyDates(): DatesModel {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val checkInDate = dateFormat.format(calendar.time)

        calendar.add(Calendar.DAY_OF_YEAR, 7)
        val checkOutDate = dateFormat.format(calendar.time)

        return DatesModel(checkInDate, checkOutDate)
    }

    val goToHotels: () -> Unit = {
//        binding.navigationView.setCheckedItem(R.id.item_hotels)
        navController.navigate(R.id.hotelsFragment)
//        binding.fabSearch.visibility = View.VISIBLE
    }

    val goToDetails: (String) -> Unit = {
        Toast.makeText(this, "Selected item: $it", Toast.LENGTH_SHORT).show()
    }
}