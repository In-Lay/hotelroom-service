package com.inlay.hotelroomservice.presentation.fragments.details

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionInflater
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.imageview.ShapeableImageView
import com.inlay.hotelroomservice.R
import com.inlay.hotelroomservice.databinding.FragmentDetailsBinding
import com.inlay.hotelroomservice.presentation.activities.MainActivity
import com.inlay.hotelroomservice.presentation.adapters.detailsdialog.placesnearby.PlacesNearbyDialogAdapter
import com.inlay.hotelroomservice.presentation.models.details.HotelDetailsSearchModel
import com.inlay.hotelroomservice.presentation.models.details.NearbyPlace
import com.inlay.hotelroomservice.presentation.viewmodels.details.DetailsViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentDetails : Fragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentDetailsBinding
    private val viewModel: DetailsViewModel by viewModel()
    private lateinit var hotelDetailsSearchModel: HotelDetailsSearchModel

    @Suppress("DEPRECATION")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, null, false)

        if (activity is MainActivity || activity is AppCompatActivity) {
            (activity as MainActivity).showProgressBar(false)

            (activity as AppCompatActivity).apply {
                setSupportActionBar(binding.toolbar)
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                supportActionBar?.setDisplayShowHomeEnabled(true)
                supportActionBar?.title =
                    findNavController().currentDestination?.label
            }
        }

        binding.toolbar.apply {
            setNavigationIconTint(com.google.android.material.R.attr.iconTint)
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            setTitleTextColor(requireContext().getColor(R.color.md_theme_dark_surface))
        }

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        hotelDetailsSearchModel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("HOTEL_DETAILS_SEARCH", HotelDetailsSearchModel::class.java)!!
        } else arguments?.getParcelable("HOTEL_DETAILS_SEARCH")!!


        val transitionInflater = TransitionInflater.from(requireContext())
        enterTransition = transitionInflater.inflateTransition(R.transition.fade_long)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.initializeData(
                openImageDialog,
                viewAllRestaurants,
                viewAllAttractions,
                openWebView,
                hotelDetailsSearchModel
            )
        }
        viewModel.chipParkingText.observe(viewLifecycleOwner) {
            binding.detailsChipGroup.findViewById<Chip>(R.id.chip_parking).text = it
        }
        viewModel.chipInternetText.observe(viewLifecycleOwner) {
            binding.detailsChipGroup.findViewById<Chip>(R.id.chip_internet).text = it
        }
        viewModel.chipGymText.observe(viewLifecycleOwner) {
            binding.detailsChipGroup.findViewById<Chip>(R.id.chip_gym).text = it
        }
        viewModel.chipFoodText.observe(viewLifecycleOwner) {
            binding.detailsChipGroup.findViewById<Chip>(R.id.chip_bars).text = it
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.errorMessage.collect {
                    if (it.isNotEmpty()) Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                }
            }
        }

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.apply {
            viewModel.hotelDetailsData.observe(viewLifecycleOwner) {
                val currentLocation = LatLng(
                    it?.latitude!!,
                    it.longitude
                )
                mapType = GoogleMap.MAP_TYPE_NORMAL
                val marker = MarkerOptions()
                    .position(currentLocation)
                    .title(it.title)
                googleMap.clear()
                addMarker(
                    marker
                )
                moveCamera(CameraUpdateFactory.newLatLng(currentLocation))
            }

            setOnCameraMoveStartedListener {
                if (it == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) binding.scrollView.setShouldInterceptTouchEvent(
                    true
                )
            }

            setOnCameraMoveCanceledListener {
                binding.scrollView.setShouldInterceptTouchEvent(false)
            }
        }

    }

    private val openImageDialog: () -> Unit = {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.hotelImagesList.collect {
                    val dialog = DetailsImageDialog.instance(it)
                    dialog.show(parentFragmentManager, "DETAILS_IMAGE_DIALOG")
                }
            }
        }
    }

    private val viewAllRestaurants: () -> Unit = {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.restaurantsNearby.collect {
                    showPlacesNearbyDialog(it)
                }
            }
        }
    }

    private val viewAllAttractions: () -> Unit = {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.attractionsNearby.collect {

                    showPlacesNearbyDialog(it)
                }
            }
        }

    }

    private fun showPlacesNearbyDialog(data: List<NearbyPlace>) {
        val dialogBuilder = activity?.let { MaterialAlertDialogBuilder(it) }
        val inflater = layoutInflater

        val dialogView = inflater.inflate(R.layout.dialog_places_nearby, null)

        dialogBuilder?.setView(dialogView)

        val recyclerView = dialogView.findViewById<RecyclerView>(R.id.recycler_view)
        val buttonClose = dialogView.findViewById<ShapeableImageView>(R.id.img_close_icon)

        recyclerView.apply {
            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(context)
            adapter = PlacesNearbyDialogAdapter(data)
        }

        val dialog = dialogBuilder?.create()

        buttonClose.setOnClickListener {
            dialog?.dismiss()
        }
        dialog?.setCancelable(true)
        dialog?.setCanceledOnTouchOutside(true)

        dialog?.show()
    }

    private val openWebView: (String) -> Unit = {
        val bundle = Bundle()
        bundle.putString("WEB_VIEW_URL", it)

        findNavController().navigate(R.id.fragmentWebView, bundle)
    }
}