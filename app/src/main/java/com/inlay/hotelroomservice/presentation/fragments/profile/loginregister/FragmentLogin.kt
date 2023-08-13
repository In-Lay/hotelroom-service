package com.inlay.hotelroomservice.presentation.fragments.profile.loginregister

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.inlay.hotelroomservice.R
import com.inlay.hotelroomservice.databinding.FragmentLoginBinding
import com.inlay.hotelroomservice.presentation.viewmodels.loginregister.LoginRegisterViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentLogin : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginRegisterViewModel by viewModel()
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        auth = Firebase.auth

        viewModel.initialize(
            close,
            navigateToRegister,
            navigateToProfile,
            auth
        )

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.toastErrorMessage.collect {
                    if (it.isNotEmpty())
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private val close: () -> Unit = {
        findNavController().popBackStack()
    }

    private val navigateToRegister: () -> Unit = {
        findNavController().navigate(R.id.fragmentRegister)
    }

    private val navigateToProfile: () -> Unit = {
        findNavController().navigate(R.id.fragmentProfile)
    }
}