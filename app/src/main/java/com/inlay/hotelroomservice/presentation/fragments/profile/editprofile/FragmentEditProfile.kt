package com.inlay.hotelroomservice.presentation.fragments.profile.editprofile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.inlay.hotelroomservice.R
import com.inlay.hotelroomservice.databinding.FragmentEditProfileBinding
import com.inlay.hotelroomservice.presentation.activities.MainActivity
import com.inlay.hotelroomservice.presentation.viewmodels.editprofile.EditProfileViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentEditProfile : Fragment() {
    private lateinit var binding: FragmentEditProfileBinding
    private val viewModel: EditProfileViewModel by viewModel()
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_edit_profile, container, false)
        val toolbar = binding.toolbar.findViewById<MaterialToolbar>(R.id.toolbar_general)

        (activity as AppCompatActivity).apply {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title =
                findNavController().currentDestination?.label
        }

        toolbar.apply {
            setTitleTextColor(requireContext().getColor(R.color.md_theme_dark_surface))
            setNavigationIconTint(com.google.android.material.R.attr.iconTint)
        }

        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewModel = viewModel

        val dialogBuilder = activity?.let {
            MaterialAlertDialogBuilder(it).setTitle(R.string.unsaved_changes)
        }?.apply {
            setPositiveButton(R.string.stay) { dialog, _ ->
                dialog.dismiss()
            }
            setNegativeButton(R.string.discard) { dialog, _ ->
                dialog.dismiss()
                (activity as MainActivity).setupHeader(true, Firebase.auth.currentUser)
                findNavController().popBackStack()
            }
            setMessage(R.string.edit_profile_dialog)
        }

        toolbar.setNavigationOnClickListener {
            lifecycleScope.launch {
                viewModel.changesApplied.collectLatest { changesApplied ->
                    if (!changesApplied) {
                        dialogBuilder?.create()?.show()
                    } else {
                        (activity as MainActivity).setupHeader(true, Firebase.auth.currentUser)
                        findNavController().popBackStack()
                    }
                }
            }
        }

        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
            if (it != null) {
                val fireStoreReference = Firebase.storage.reference
                viewModel.changePhoto(it, fireStoreReference)
            } else {
                Toast.makeText(context, R.string.no_photo_selected, Toast.LENGTH_SHORT).show()
            }
        }

        val transitionInflater = TransitionInflater.from(requireContext())
        enterTransition = transitionInflater.inflateTransition(R.transition.fade)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.initialize(
            Firebase.auth.currentUser,
            showAuthDialog,
            openPhotoPicker
        )

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.toastText.collect {
                    if (it.isNotEmpty()) Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @SuppressLint("InflateParams")
    private val showAuthDialog: () -> Unit = {
        val alertBuilder = MaterialAlertDialogBuilder(requireContext())
        val inflater = layoutInflater

        val dialogView = inflater.inflate(R.layout.auth_dialog_layout, null)

        alertBuilder.setView(dialogView)

        val emailInputLayout = dialogView.findViewById<TextInputLayout>(R.id.et_layout_email)
        val emailInput = dialogView.findViewById<TextInputEditText>(R.id.et_email)
        val passwordInput = dialogView.findViewById<TextInputEditText>(R.id.et_password)

        var emailText = ""
        emailInput.doOnTextChanged { text, _, _, _ ->
            emailText = if (viewModel.isEmailValid(text.toString())) {
                emailInputLayout.helperText = ""
                text.toString()
            } else {
                emailInputLayout.helperText = resources.getString(R.string.invalid_email)
                ""
            }
        }

        alertBuilder.setPositiveButton(R.string.log_in) { _, _ ->
            val passwordText = passwordInput.text

            viewModel.changeEmail(emailText, passwordText.toString())
        }

        alertBuilder.setNegativeButton(R.string.discard) { dialog, _ ->
            dialog.dismiss()
        }

        alertBuilder.create().show()
    }

    private val openPhotoPicker: () -> Unit = {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
}