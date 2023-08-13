package com.inlay.hotelroomservice.presentation.fragments.settings

import android.os.Bundle
import android.util.Log
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
import com.inlay.hotelroomservice.R
import com.inlay.hotelroomservice.databinding.DialogLanguagesBinding
import com.inlay.hotelroomservice.databinding.FragmentSettingsBinding
import com.inlay.hotelroomservice.presentation.DrawerProvider
import com.inlay.hotelroomservice.presentation.activities.MainActivity
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


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)

        val toolbar = binding.toolbar.findViewById<MaterialToolbar>(R.id.toolbar_general)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationIcon(R.drawable.baseline_dehaze_24)
        toolbar.setNavigationOnClickListener {
            (activity as DrawerProvider).getDrawerLayout().openDrawer(GravityCompat.START)
        }
        (activity as AppCompatActivity).supportActionBar?.title =
            findNavController().currentDestination?.label
        Log.d(
            "SettingsLog",
            "findNavController().currentDestination?.label: ${findNavController().currentDestination?.label}"
        )

        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewModel = viewModel

        currentLocale = context?.resources?.configuration?.locales?.get(0)!!

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.initialize(
            openLangDialog,
            requireContext().resources.getStringArray(R.array.lang_array).toList()
        )

        binding.tvCurrentLang.text = currentLocale.displayName

        lifecycleScope.launch {
            hotelsViewModel.darkModeState.collect {
                binding.switchDayNight.isChecked = it == AppCompatDelegate.MODE_NIGHT_YES
            }
        }
    }

    private val openLangDialog: () -> Unit = {
        showLangDialog(currentLocale.displayName.split(" ").first())
    }

    private fun showLangDialog(selectedLocale: String) {
        Log.d("SettingsLog", "showLangDialog: selectedLocale: $selectedLocale")
        Log.d("SettingsLog", "showLangDialog: langList: ${viewModel.langsList.value}")
        val dialogBinding = DialogLanguagesBinding.inflate(layoutInflater)
        val dialogBuilder = MaterialAlertDialogBuilder(requireContext())
        var localeCode = "en"

        dialogBinding.viewModel = viewModel

        binding.lifecycleOwner = viewLifecycleOwner

        when (selectedLocale.lowercase(Locale.ROOT)) {
            "english" -> {
                dialogBinding.btnEng.isChecked = true
                localeCode = "en"
            }

            "українська" -> {
                dialogBinding.btnUa.isChecked = true
                localeCode = "ua"
            }

            "русский" -> {
                dialogBinding.btnRu.isChecked = true
                localeCode = "ru"
            }

            "español" -> {
                dialogBinding.btnEs.isChecked = true
                localeCode = "es"
            }

            "deutsch" -> {
                dialogBinding.btnDe.isChecked = true
                localeCode = "de"
            }

            "italiano" -> {
                dialogBinding.btnIt.isChecked = true
                localeCode = "it"
            }

            "français" -> {
                dialogBinding.btnFr.isChecked = true
                localeCode = "fr"
            }

            "中文" -> {
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