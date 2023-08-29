package com.inlay.hotelroomservice.presentation.fragments.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.inlay.hotelroomservice.R
import com.inlay.hotelroomservice.databinding.DialogLanguagesBinding
import com.inlay.hotelroomservice.databinding.FragmentSettingsBinding
import com.inlay.hotelroomservice.presentation.DrawerProvider
import com.inlay.hotelroomservice.presentation.activities.MainActivity
import com.inlay.hotelroomservice.presentation.fragments.search.Languages
import com.inlay.hotelroomservice.presentation.viewmodels.hotels.HotelsViewModel
import com.inlay.hotelroomservice.presentation.viewmodels.settings.SettingsViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale


class FragmentSettings : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: SettingsViewModel by viewModel()
    private val hotelsViewModel: HotelsViewModel by activityViewModel()
    private lateinit var currentLocale: Locale

//    override fun onResume() {
//        super.onResume()
//        if (!areNotificationsEnabled()) {
//            viewModel.changeNotificationsState(false)
//            binding.switchNotifications.isEnabled = false
//        } else {
//            viewModel.changeNotificationsState(true)
//            binding.switchNotifications.isChecked = false
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)

        val toolbar = binding.toolbar.findViewById<MaterialToolbar>(R.id.toolbar_general)

        (activity as AppCompatActivity).apply {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title =
                findNavController().currentDestination?.label
        }

        toolbar.apply {
            setNavigationIcon(R.drawable.baseline_dehaze_24)
            setNavigationOnClickListener {
                (activity as DrawerProvider).getDrawerLayout().openDrawer(GravityCompat.START)
            }
            setTitleTextColor(requireContext().getColor(R.color.md_theme_dark_surface))
        }

        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewModel = viewModel

        currentLocale = context?.resources?.configuration?.locales?.get(0)!!

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.initialize(
            openLangDialog, requireContext().resources.getStringArray(R.array.lang_array).toList()
        )

        binding.tvCurrentLang.text = currentLocale.displayName

        lifecycleScope.launch {
            hotelsViewModel.darkModeState.collect {
                binding.switchDayNight.isChecked = it == AppCompatDelegate.MODE_NIGHT_YES
            }
        }

        lifecycleScope.launch {
            hotelsViewModel.notificationsAvailability.collect {
                if (!it) {
//                    viewModel.changeNotificationsState(false)
                    binding.switchNotifications.isEnabled = false
                    Snackbar.make(view, R.string.enable_notifications, Snackbar.LENGTH_SHORT)
                        .setAction(R.string.enable) {
                            openAppNotificationSettings()
                        }.show()
                } else {
//                    viewModel.changeNotificationsState(true)
                    binding.switchNotifications.isEnabled = true
                }
            }
        }

        lifecycleScope.launch {
            viewModel.inAppNotificationsState.collect {
                binding.switchNotifications.isChecked = it
            }
        }
    }

//    private fun areNotificationsEnabled(): Boolean {
//        val notificationManager =
//            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        return notificationManager.areNotificationsEnabled()
//    }

    private val openLangDialog: () -> Unit = {
        showLangDialog(currentLocale.displayName.split(" ").first())
    }

    private fun openAppNotificationSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.fromParts("package", context?.packageName, null)
        context?.startActivity(intent)
    }

    private fun showLangDialog(selectedLocale: String) {
        val dialogBinding = DialogLanguagesBinding.inflate(layoutInflater)
        val dialogBuilder = MaterialAlertDialogBuilder(requireContext())
        var localeCode = "en"

        dialogBinding.viewModel = viewModel

        binding.lifecycleOwner = viewLifecycleOwner

        when (selectedLocale.lowercase(Locale.ROOT)) {
            Languages.ENGLISH.language -> {
                dialogBinding.btnEng.isChecked = true
                localeCode = "en"
            }

            Languages.UKRAINIAN.language -> {
                dialogBinding.btnUa.isChecked = true
                localeCode = "ua"
            }

            Languages.RUSSIAN.language -> {
                dialogBinding.btnRu.isChecked = true
                localeCode = "ru"
            }

            Languages.SPANISH.language -> {
                dialogBinding.btnEs.isChecked = true
                localeCode = "es"
            }

            Languages.GERMAN.language -> {
                dialogBinding.btnDe.isChecked = true
                localeCode = "de"
            }

            Languages.ITALIAN.language -> {
                dialogBinding.btnIt.isChecked = true
                localeCode = "it"
            }

            Languages.FRENCH.language -> {
                dialogBinding.btnFr.isChecked = true
                localeCode = "fr"
            }

            Languages.CHINESE.language -> {
                dialogBinding.btnZh.isChecked = true
                localeCode = "zh"
            }
        }
        dialogBuilder.setView(dialogBinding.root)

        dialogBinding.radoGroup.setOnCheckedChangeListener { _, id ->
            localeCode = when (id) {
                R.id.btn_eng -> "en"

                R.id.btn_ua -> "ua"

                R.id.btn_ru -> "ru"

                R.id.btn_es -> "es"

                R.id.btn_de -> "de"

                R.id.btn_it -> "it"

                R.id.btn_fr -> "fr"

                R.id.btn_zh -> "zh"

                else -> "en"
            }
        }

        dialogBuilder.setPositiveButton(R.string.ok) { dialog, _ ->
            showChangeLocaleDialog(localeCode)
            dialog.dismiss()
        }

        dialogBuilder.setNegativeButton(R.string.cancel) { dialog, _ ->
            dialog.dismiss()
        }

        dialogBuilder.create().show()
    }

    private fun showChangeLocaleDialog(languageCode: String) {
        val dialogBuilder = MaterialAlertDialogBuilder(requireContext())

        dialogBuilder.apply {
            setTitle(R.string.change_language)
            setMessage(R.string.change_language_info)
            setNegativeButton(R.string.dismiss) { dialog, _ ->
                dialog.dismiss()
            }
            setPositiveButton(R.string.ok) { _, _ ->
                hotelsViewModel.changeLanguage(languageCode)

                (activity as MainActivity).recreate()
            }
        }

        dialogBuilder.create().show()
    }
}