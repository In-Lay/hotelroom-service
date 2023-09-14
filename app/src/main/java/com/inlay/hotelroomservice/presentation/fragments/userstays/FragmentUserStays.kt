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
import androidx.transition.TransitionInflater
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
import java.util.Calendar

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
            user,
            getDayTime(),
            requireContext().getString(R.string.stays_login_message)
        )
        hotelsViewModel.getStaysRepo(requireContext().isNetworkAvailable(), isLogged)


        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val toolbar = binding.toolbar.findViewById<MaterialToolbar>(R.id.toolbar_general)

        (activity as AppCompatActivity).apply {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title =
                findNavController().currentDestination?.label
        }

        toolbar.apply {
            setNavigationIcon(R.drawable.baseline_dehaze_24)
            setTitleTextColor(requireContext().getColor(R.color.md_theme_dark_surface))
            setNavigationOnClickListener {
                (activity as DrawerProvider).getDrawerLayout().openDrawer(GravityCompat.START)
            }
        }

        lifecycleScope.launch {
            hotelsViewModel.hotelsDatesAndCurrencyModel.collect {
                if (it != null) {
                    hotelsDatesAndCurrencyModel = it
                }
            }
        }

        val transitionInflater = TransitionInflater.from(requireContext())
        enterTransition = transitionInflater.inflateTransition(R.transition.fade)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!isLogged) {
            val snackBar = Snackbar.make(
                view,
                requireContext().getString(R.string.stays_snackbar_text),
                Snackbar.LENGTH_SHORT
            )
            snackBar.setAction(requireContext().getString(R.string.dismiss)) {
                snackBar.dismiss()
            }
            snackBar.show()
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                hotelsViewModel.selectedHotelsDataList.collectLatest {
                    if (it.isNotEmpty()) {
                        binding.tvEmptyList.visibility = View.GONE
                        binding.recyclerView.apply {
                            visibility = View.VISIBLE
                            setHasFixedSize(false)
                            layoutManager = LinearLayoutManager(context)
                            adapter =
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
        Toast.makeText(context, context?.getString(R.string.removed), Toast.LENGTH_SHORT).show()
    }

    private fun isUserLogged(user: FirebaseUser?): Boolean {
        return user != null
    }

    private fun getDayTime(): String {
        val calendar = Calendar.getInstance()
        val context = requireContext()
        return when (calendar.get(Calendar.HOUR_OF_DAY)) {
            in 1..4 -> context.getString(R.string.night)
            in 4..9 -> context.getString(R.string.morning)
            in 9..17 -> context.getString(R.string.day)
            in 17..22 -> context.getString(R.string.evening)
            in 22..24 -> context.getString(R.string.night)
            else -> context.getString(R.string.day)
        }
    }
}