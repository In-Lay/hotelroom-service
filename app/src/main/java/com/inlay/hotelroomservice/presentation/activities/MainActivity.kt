package com.inlay.hotelroomservice.presentation.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.inlay.hotelroomservice.R
import com.inlay.hotelroomservice.databinding.ActivityMainBinding
import com.inlay.hotelroomservice.extensions.isNetworkAvailable
import com.inlay.hotelroomservice.presentation.DrawerProvider
import com.inlay.hotelroomservice.presentation.models.details.HotelDetailsSearchModel
import com.inlay.hotelroomservice.presentation.viewmodels.hotels.HotelsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), DrawerProvider {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val hotelsViewModel: HotelsViewModel by viewModel()
    private var signInRememberState = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentUser = Firebase.auth.currentUser


        setupNavigationDrawer()
        setupNavigation()
//        Log.d(
//            "profileTag",
//            "onCreate before addAuthStateListener: currentUser: ${currentUser?.email}"
//        )
        Firebase.auth.addAuthStateListener {
//            Log.d(
//                "profileTag",
//                "onCreate inside addAuthStateListener: currentUser: ${currentUser?.email}"
//            )
            if (it.currentUser != null) {
                setupHeader(isUserLogged(it.currentUser), it.currentUser)
            } else setupHeader(isUserLogged(null), null)
        }
//        Log.d(
//            "profileTag",
//            "onCreate after addAuthStateListener: currentUser: ${currentUser?.email}"
//        )

        hotelsViewModel.initialize(isNetworkAvailable())

        binding.fabSearch.setOnClickListener {
            navController.navigate(R.id.fragmentSearch)
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.hotelsFragment -> binding.fabSearch.visibility = View.VISIBLE
                else -> binding.fabSearch.visibility = View.GONE
            }
        }
        binding.lifecycleOwner = this
    }

    private fun setupNavigationDrawer() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController = navHostFragment.navController

        binding.navigationView.setupWithNavController(navController)
    }

    private fun setupNavigation() {
        binding.navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item_hotels -> {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    navController.navigate(R.id.hotelsFragment)
                    true
                }

                R.id.item_user_stays -> {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    navController.navigate(R.id.fragmentUserStays)
                    true
                }

                R.id.item_settings -> {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    navController.navigate(R.id.fragmentSettings)
                    true
                }

                else -> false
            }
        }
    }

    private fun setupHeader(isUserLogged: Boolean, user: FirebaseUser?) {
        val headerView = binding.navigationView.getHeaderView(0)
        val headerImage = headerView.findViewById<ImageView>(R.id.header_image)
        val headerUserName = headerView.findViewById<TextView>(R.id.tv_user_name)
        val headerMail = headerView.findViewById<TextView>(R.id.tv_user_mail)

        if (!isUserLogged) {
            headerImage.load(R.drawable.baseline_person_24) {
                crossfade(true)
                transformations(CircleCropTransformation())
            }
            headerUserName.text = this.resources.getText(R.string.header_profile)
            headerMail.text = ""
        } else {
            //TODO After Register Username doesn't show up
            headerUserName.text = user?.displayName
            headerMail.text = user?.email
            if (user?.photoUrl == null) headerImage.load(R.drawable.baseline_person_24)
            else headerImage.load(user.photoUrl) {
                crossfade(true)
                transformations(CircleCropTransformation())
            }
        }

        headerImage.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            handleProfileNavigation(isUserLogged)
        }

        headerUserName.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            handleProfileNavigation(isUserLogged)
        }
    }

    override fun getDrawerLayout(): DrawerLayout = binding.drawerLayout

    private fun handleProfileNavigation(isUserLogged: Boolean) {
        if (isUserLogged) navController.navigate(R.id.fragmentProfile)
        else navController.navigate(R.id.fragmentLogin)
    }

    val goToHotels: () -> Unit = {
        navController.navigate(R.id.hotelsFragment)
    }

    override fun onDestroy() {
        //TODO On remember me false doesn't work
        Log.d("profileTag", "onDestroy: isChecked: $signInRememberState")
        if (!signInRememberState) {
            Log.d("profileTag", "onDestroy inside: isChecked: false")
            Firebase.auth.signOut()
            val user = Firebase.auth.currentUser
            Log.d("profileTag", "onDestroy inside: user: $user")
        }
        super.onDestroy()
    }

    val signOutOnRememberFalse: (Boolean) -> Unit = {
        Log.d("profileTag", "signOutOnRememberFalse: isChecked: $it")
        signInRememberState = it
    }

    val goToDetails: (HotelDetailsSearchModel) -> Unit = {
        if (this.isNetworkAvailable()) {
            val bundle = Bundle()
            bundle.putParcelable("HOTEL_DETAILS_SEARCH", it)

            showProgressBar(true)
            navController.navigate(R.id.fragmentDetails, bundle)
        } else {
            Toast.makeText(this, "No internet connection!", Toast.LENGTH_SHORT).show()
        }
    }

    fun showProgressBar(status: Boolean) {
        if (status) binding.progressBar.visibility = View.VISIBLE
        else binding.progressBar.visibility = View.GONE

    }

    private fun isUserLogged(user: FirebaseUser?): Boolean {
        return user != null
    }
}