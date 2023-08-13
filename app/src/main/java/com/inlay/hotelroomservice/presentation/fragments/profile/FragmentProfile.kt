package com.inlay.hotelroomservice.presentation.fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.inlay.hotelroomservice.R
import com.inlay.hotelroomservice.databinding.FragmentProfileBinding
import com.inlay.hotelroomservice.presentation.DrawerProvider
import com.inlay.hotelroomservice.presentation.viewmodels.profile.ProfileViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentProfile : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarGeneral)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarGeneral.setNavigationIcon(R.drawable.baseline_dehaze_24)
        binding.toolbarGeneral.setNavigationOnClickListener {
            (activity as DrawerProvider).getDrawerLayout().openDrawer(GravityCompat.START)
        }
        (activity as AppCompatActivity).supportActionBar?.title =
            findNavController().currentDestination?.label

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = Firebase.auth.currentUser
        viewModel.initializeData(logout, edit, user)
    }

    private val logout: () -> Unit = {
        showLogoutDialog()
    }

    private val edit: () -> Unit = {
        findNavController().navigate(R.id.fragmentEditProfile)
    }

    private fun showLogoutDialog() {
        val dialogBuilder = MaterialAlertDialogBuilder(requireContext())

        dialogBuilder.apply {
            setTitle(R.string.logout)
            setMessage(R.string.logout_info)
            setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            setPositiveButton(R.string.logout) { dialog, _ ->
                Firebase.auth.signOut()
                dialog.dismiss()
                findNavController().navigate(R.id.hotelsFragment)
            }
        }

        dialogBuilder.create().show()
    }
}