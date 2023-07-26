package com.inlay.hotelroomservice.presentation.fragments.details

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.inlay.hotelroomservice.R
import com.inlay.hotelroomservice.databinding.FragmentDetailsBinding
import com.inlay.hotelroomservice.presentation.activities.MainActivity
import com.inlay.hotelroomservice.presentation.models.details.HotelDetailsSearchModel
import com.inlay.hotelroomservice.presentation.viewmodels.details.DetailsViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentDetails : Fragment() {
    private lateinit var binding: FragmentDetailsBinding
    private val viewModel: DetailsViewModel by viewModel()
    private lateinit var hotelDetailsSearchModel: HotelDetailsSearchModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, null, false)

        (activity as MainActivity).showProgressBar(false)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        hotelDetailsSearchModel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("HOTEL_DETAILS_SEARCH", HotelDetailsSearchModel::class.java)!!
        } else arguments?.getParcelable("HOTEL_DETAILS_SEARCH")!!

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(
            "DetailsNavLog",
            "FragmentDetails: onCreateView: hotelDetailsSearchModel: $hotelDetailsSearchModel"
        )
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
                    val dialog =
                        PlacesNearbyDialog.instance(it)

                    dialog.show(parentFragmentManager, "PLACES_NEARBY_DIALOG")
                }
            }
        }
    }

    private val viewAllAttractions: () -> Unit = {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.attractionsNearby.collect {
                    val dialog =
                        PlacesNearbyDialog.instance(it)

                    dialog.show(parentFragmentManager, "PLACES_NEARBY_DIALOG")
                }
            }
        }

    }

    private val openWebView: (String) -> Unit = {
        val bundle = Bundle()
        bundle.putString("WEB_VIEW_URL", it)

        findNavController().navigate(R.id.fragmentWebView, bundle)
    }
}