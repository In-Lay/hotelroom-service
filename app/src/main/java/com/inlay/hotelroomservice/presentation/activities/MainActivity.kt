package com.inlay.hotelroomservice.presentation.activities

import android.app.UiModeManager
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.core.content.edit
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
import com.inlay.hotelroomservice.extensions.isNetworkAvailable
import com.inlay.hotelroomservice.presentation.DrawerProvider
import com.inlay.hotelroomservice.presentation.LocaleContextWrapper
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
    private var signInRememberState = true
    private var language = "en"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("sharprefs_key", MODE_PRIVATE)

        val lnag = sharedPreferences.getString("lang_key", "en")
        Log.d(
            "SettingsLog",
            "lnag: $lnag; sharedPreferences: $sharedPreferences"
        )
        val locale = Locale(lnag)
        val ctxt = LocaleContextWrapper.wrap(this@MainActivity, locale)
        resources.updateConfiguration(
            ctxt.resources.configuration,
            ctxt.resources.displayMetrics
        )

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentUser = Firebase.auth.currentUser
        val languageToLoad: String = Resources.getSystem().configuration.locales[0].language
        language = languageToLoad

        lifecycleScope.launch {
            hotelsViewModel.language.collect {
                Log.d(
                    "SettingsLog",
                    "MainActivity: attachBaseContext: hotelsViewModel.language: $it"
                )
                language = it
//                val locale = Locale(it)
//
//                Locale.setDefault(locale)
//
//                val configuration = Configuration()
//                configuration.setLocale(locale)
//                val newContext = createConfigurationContext(configuration)
//                applicationContext?.resources?.configuration?.updateFrom(configuration)

//                val ctxt = LocaleContextWrapper.wrap(this@MainActivity, locale)
//                resources.updateConfiguration(
//                    ctxt.resources.configuration,
//                    ctxt.resources.displayMetrics
//                )

            }
        }

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

//    override fun onStart() {
//        super.onStart()
//        lifecycleScope.launch {
//            hotelsViewModel.language.collectLatest {
//                Log.d(
//                    "SettingsLog",
//                    "MainActivity: attachBaseContext: hotelsViewModel.language: $it"
//                )
//                language = it
////                attachBaseContext(this@MainActivity)
//            }
//        }
//
//    }

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
            Log.d("profileTag", "setupHeader: user?.photoUrl: ${user?.photoUrl}")
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


//    override fun attachBaseContext(newBase: Context?) {
//        val hotelsViewModel: HotelsViewModel by inject()
//        val languageToLoad: String = Resources.getSystem().configuration.locales[0].language
//        var language = languageToLoad

//        lifecycleScope.launch {
//            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
//                hotelsViewModel.language.collectLatest {
//                    Log.d(
//                        "SettingsLog",
//                        "MainActivity: attachBaseContext: hotelsViewModel.language: $it"
//                    )
//                    language = it
//
//                }
//            }
//        }
//        Log.d("SettingsLog", "MainActivity: attachBaseContext: language: $language")
//        //TODO get language code from DataStore
//
//        val locale = Locale("de")
//        Locale.setDefault(locale)
//
//        val context = LocaleContextWrapper.wrap(newBase, locale)
//        super.attachBaseContext(context)
//    }

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