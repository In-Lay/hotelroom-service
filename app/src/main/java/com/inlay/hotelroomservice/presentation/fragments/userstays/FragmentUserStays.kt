package com.inlay.hotelroomservice.presentation.fragments.userstays

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.inlay.hotelroomservice.R
import com.inlay.hotelroomservice.databinding.FragmentUserStaysBinding
import com.inlay.hotelroomservice.extensions.isNetworkAvailable
import com.inlay.hotelroomservice.presentation.DrawerProvider
import com.inlay.hotelroomservice.presentation.activities.MainActivity
import com.inlay.hotelroomservice.presentation.adapters.hotels.HotelsListAdapter
import com.inlay.hotelroomservice.presentation.models.hotelsitem.HotelsDatesAndCurrencyModel
import com.inlay.hotelroomservice.presentation.models.hotelsitem.HotelsItemUiModel
import com.inlay.hotelroomservice.presentation.viewmodels.hotels.HotelsViewModel
import com.inlay.hotelroomservice.presentation.viewmodels.userstays.UserStaysViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentUserStays : Fragment() {
    private lateinit var binding: FragmentUserStaysBinding
    private val viewModel: UserStaysViewModel by viewModel()
    private val hotelsViewModel: HotelsViewModel by activityViewModel()
    private var isLogged = false
    private var isOnline = false
    private lateinit var hotelsDatesAndCurrencyModel: HotelsDatesAndCurrencyModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_stays, container, false)
        val user = Firebase.auth.currentUser
        isLogged = isUserLogged(user)
        isOnline = requireContext().isNetworkAvailable()

        viewModel.initializeData(
            (activity as MainActivity).goToHotels,
            goToProfile,
            isLogged,
            user
        )
        hotelsViewModel.getStaysRepo(requireContext().isNetworkAvailable(), isLogged)


        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val toolbar = binding.toolbar.findViewById<MaterialToolbar>(R.id.toolbar_general)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationIcon(R.drawable.baseline_dehaze_24)
        toolbar.setNavigationOnClickListener {
            (activity as DrawerProvider).getDrawerLayout().openDrawer(GravityCompat.START)
        }
        (activity as AppCompatActivity).supportActionBar?.title =
            findNavController().currentDestination?.label

        lifecycleScope.launch {
            hotelsViewModel.hotelsDatesAndCurrencyModel.collect {
                if (it != null) {
                    hotelsDatesAndCurrencyModel = it
                }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!isLogged) {
            val snackBar = Snackbar.make(
                view,
                "Note, that data is stored only locally, until you're log in.",
                Snackbar.LENGTH_SHORT
            )
//            snackBar.setAction("Log in") {
//                findNavController().navigate(R.id.fragmentLogin)
//            }
            snackBar.setAction("Dismiss") {
                snackBar.dismiss()
            }
            snackBar.show()
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                hotelsViewModel.selectedHotelsDataList.collectLatest {
                    if (it.isNotEmpty()) {
                        binding.tvEmptyList.visibility = View.GONE
                        binding.recyclerView.visibility = View.VISIBLE

                        binding.recyclerView.setHasFixedSize(false)
                        binding.recyclerView.layoutManager = LinearLayoutManager(context)
                        binding.recyclerView.adapter =
                            HotelsListAdapter(
                                it,
                                hotelsDatesAndCurrencyModel,
                                (activity as MainActivity).goToDetails,
                                removeStay,
                                "stays"
                            )
                    }
                }
            }
        }
    }

    private val goToProfile: () -> Unit = {
        if (!isLogged) findNavController().navigate(R.id.fragmentLogin)
        else findNavController().navigate(R.id.fragmentProfile)
    }

    private val removeStay: (HotelsItemUiModel) -> Unit = {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                hotelsViewModel.removeStay(it, isOnline, isLogged)
            }
        }
        Toast.makeText(context, "Removed", Toast.LENGTH_SHORT).show()
    }

    private fun isUserLogged(user: FirebaseUser?): Boolean {
        return user != null
    }
}