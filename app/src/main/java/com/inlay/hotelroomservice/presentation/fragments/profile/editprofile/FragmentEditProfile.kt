package com.inlay.hotelroomservice.presentation.fragments.profile.editprofile

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
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.inlay.hotelroomservice.R
import com.inlay.hotelroomservice.databinding.FragmentEditProfileBinding
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
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.title =
            findNavController().currentDestination?.label

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
                        findNavController().popBackStack()
                    }
                }
            }
        }

        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
            if (it != null) {
                viewModel.changePhoto(it)
            } else {
                Toast.makeText(context, "No photo selected", Toast.LENGTH_SHORT).show()
            }
        }

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
                emailInputLayout.helperText = "Invalid Email"
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