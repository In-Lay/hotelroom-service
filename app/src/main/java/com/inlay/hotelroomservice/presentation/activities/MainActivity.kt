package com.inlay.hotelroomservice.presentation.activities

import android.app.UiModeManager
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
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
import com.inlay.hotelroomservice.domain.usecase.sharedpreferences.GetLanguagePreferences
import com.inlay.hotelroomservice.extensions.isNetworkAvailable
import com.inlay.hotelroomservice.presentation.DrawerProvider
import com.inlay.hotelroomservice.presentation.models.details.HotelDetailsSearchModel
import com.inlay.hotelroomservice.presentation.viewmodels.hotels.HotelsViewModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale


class MainActivity : AppCompatActivity(), DrawerProvider {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val hotelsViewModel: HotelsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            hotelsViewModel.darkModeState.collect {
                val currentNightMode =
                    resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
                if (it != currentNightMode) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        val uiModeManager =
                            getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
                        if (it == AppCompatDelegate.MODE_NIGHT_YES) uiModeManager.setApplicationNightMode(
                            UiModeManager.MODE_NIGHT_YES
                        )
                        else uiModeManager.setApplicationNightMode(UiModeManager.MODE_NIGHT_NO)
                    } else {
                        if (it == AppCompatDelegate.MODE_NIGHT_YES)
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }
                } else {
                    val newNightMode = when (currentNightMode) {
                        Configuration.UI_MODE_NIGHT_NO -> AppCompatDelegate.MODE_NIGHT_NO
                        Configuration.UI_MODE_NIGHT_YES -> AppCompatDelegate.MODE_NIGHT_YES
                        else -> MODE_NIGHT_FOLLOW_SYSTEM
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        val uiModeManager =
                            getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
                        uiModeManager.setApplicationNightMode(
                            newNightMode
                        )
                    } else AppCompatDelegate.setDefaultNightMode(newNightMode)
                }
            }
        }

        setupNavigationDrawer()
        setupNavigation()

        Firebase.auth.addAuthStateListener {
            if (it.currentUser != null) {
                setupHeader(isUserLogged(it.currentUser), it.currentUser)
            } else setupHeader(isUserLogged(null), null)
        }

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

        navController.setGraph(R.navigation.nav_graph)

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
            // After photo change it doesn't appear
            headerUserName.text = user?.displayName
            headerMail.text = user?.email
//            Log.d("profileTag", "setupHeader: user?.photoUrl: ${user?.photoUrl}")
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


    override fun attachBaseContext(newBase: Context?) {
        val getLanguagePreferences: GetLanguagePreferences by inject()

        val languageCode = getLanguagePreferences.getLanguage()

        super.attachBaseContext(languageCode?.let { getContextForLocale(newBase, it) })
    }

    private fun getContextForLocale(context: Context?, languageCode: String): Context? {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val configuration = context?.resources?.configuration
        configuration?.setLocale(locale)

        return configuration?.let { context.createConfigurationContext(it) }
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