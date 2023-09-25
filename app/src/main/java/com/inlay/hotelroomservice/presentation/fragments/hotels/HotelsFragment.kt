package com.inlay.hotelroomservice.presentation.fragments.hotels

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
import androidx.transition.TransitionInflater
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.inlay.hotelroomservice.R
import com.inlay.hotelroomservice.databinding.FragmentHotelsBinding
import com.inlay.hotelroomservice.extensions.isNetworkAvailable
import com.inlay.hotelroomservice.presentation.DrawerProvider
import com.inlay.hotelroomservice.presentation.activities.MainActivity
import com.inlay.hotelroomservice.presentation.adapters.hotels.HotelsListAdapter
import com.inlay.hotelroomservice.presentation.models.hotelsitem.HotelsDatesAndCurrencyModel
import com.inlay.hotelroomservice.presentation.models.hotelsitem.HotelsItemUiModel
import com.inlay.hotelroomservice.presentation.viewmodels.hotels.HotelsViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class HotelsFragment : Fragment() {
    private lateinit var binding: FragmentHotelsBinding
    private val hotelsViewModel: HotelsViewModel by activityViewModel()
    private lateinit var hotelsDatesAndCurrencyModel: HotelsDatesAndCurrencyModel
    private var isOnline = false
    private var isLogged = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_hotels, container, false)
        binding.lifecycleOwner = this

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
            setNavigationIconTint(com.google.android.material.R.attr.iconTint)
            setTitleTextColor(requireContext().getColor(R.color.md_theme_dark_surface))
        }

        lifecycleScope.launch {
            hotelsViewModel.hotelsDatesAndCurrencyModel.collectLatest {
                if (it != null) {
                    hotelsDatesAndCurrencyModel = it
                }
            }
        }
        val user = Firebase.auth.currentUser

        isOnline = requireContext().isNetworkAvailable()
        isLogged = isUserLogged(user)

        val transitionInflater = TransitionInflater.from(requireContext())
        enterTransition = transitionInflater.inflateTransition(R.transition.fade)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                hotelsViewModel.hotelsDataList.collect {
                    if (requireContext().isNetworkAvailable() && it.isNotEmpty()) binding.progressBar.visibility =
                        View.GONE
                    else binding.progressBar.visibility = View.VISIBLE
                }
            }
        }

        bindAdapter()
    }

    private fun bindAdapter() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                hotelsViewModel.hotelsDataList.collectLatest {
                    binding.hotelsRecyclerView.adapter =
                        HotelsListAdapter(
                            it,
                            hotelsDatesAndCurrencyModel,
                            (activity as MainActivity).goToDetails,
                            addStay,
                            "main"
                        )
                    binding.hotelsRecyclerView.layoutManager = LinearLayoutManager(context)
                    binding.hotelsRecyclerView.setHasFixedSize(false)
                }
            }
        }
    }

    private val addStay: (HotelsItemUiModel) -> Unit = {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                hotelsViewModel.addStay(it, isOnline, isLogged)
            }
        }
        Toast.makeText(context, context?.getString(R.string.added), Toast.LENGTH_SHORT).show()
    }

    private fun isUserLogged(user: FirebaseUser?): Boolean {
        return user != null
    }
}