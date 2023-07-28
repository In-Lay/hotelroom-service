package com.inlay.hotelroomservice.presentation.fragments.profile.editprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.inlay.hotelroomservice.R
import com.inlay.hotelroomservice.databinding.FragmentEditProfileBinding
import com.inlay.hotelroomservice.presentation.DrawerProvider
import com.inlay.hotelroomservice.presentation.viewmodels.editprofile.EditProfileViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentEditProfile : Fragment() {
    private lateinit var binding: FragmentEditProfileBinding
    private val viewModel: EditProfileViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_edit_profile, container, false)
        val toolbar = binding.toolbar.findViewById<MaterialToolbar>(R.id.toolbar_general)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val dialogBuilder = activity?.let {
            AlertDialog.Builder(it).setTitle(R.string.unsaved_changes)
        }
        dialogBuilder?.setPositiveButton(R.string.apply) { dialog, _ ->
            viewModel.save()
            dialog.dismiss()
        }
        dialogBuilder?.setNegativeButton(R.string.discard) { dialog, _ ->
            dialog.dismiss()
        }
        dialogBuilder?.setMessage(R.string.edit_profile_dialog)

        toolbar.setNavigationOnClickListener {
            lifecycleScope.launch {
                viewModel.changesApplied.collectLatest {
                    if (!it) {
                        dialogBuilder?.create()?.show()
                    } else {
                        findNavController().popBackStack()
                    }

                }
            }
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