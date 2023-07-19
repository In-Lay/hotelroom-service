package com.inlay.hotelroomservice.presentation.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.inlay.hotelroomservice.R
import com.inlay.hotelroomservice.databinding.FragmentSettingsBinding
import com.inlay.hotelroomservice.presentation.DrawerProvider

class FragmentSettings : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
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

        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}