package com.inlay.hotelroomservice.presentation.fragments.details

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.FullScreenCarouselStrategy
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.imageview.ShapeableImageView
import com.inlay.hotelroomservice.R
import com.inlay.hotelroomservice.databinding.FragmentDetailsBinding
import com.inlay.hotelroomservice.presentation.activities.MainActivity
import com.inlay.hotelroomservice.presentation.adapters.detailsdialog.detailsimage.DetailsImageAdapter
import com.inlay.hotelroomservice.presentation.adapters.detailsdialog.placesnearby.PlacesNearbyDialogAdapter
import com.inlay.hotelroomservice.presentation.models.details.HotelDetailsSearchModel
import com.inlay.hotelroomservice.presentation.models.details.NearbyPlace
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
        (activity as AppCompatActivity).supportActionBar?.title =
            findNavController().currentDestination?.label

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        hotelDetailsSearchModel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("HOTEL_DETAILS_SEARCH", HotelDetailsSearchModel::class.java)!!
        } else arguments?.getParcelable("HOTEL_DETAILS_SEARCH")!!

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
    }

    private val openImageDialog: () -> Unit = {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.hotelImagesList.collect {
                    val dialog = DetailsImageDialog.instance(it)

                    dialog.show(parentFragmentManager, "DETAILS_IMAGE_DIALOG")

//                    showImageDialog(it)
                }
            }
        }
    }

    private val viewAllRestaurants: () -> Unit = {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.restaurantsNearby.collect {
//                    val dialog =
//                        PlacesNearbyDialog.instance(it)
//
//                    dialog.show(parentFragmentManager, "PLACES_NEARBY_DIALOG")
                    showPlacesNearbyDialog(it)
                }
            }
        }
    }

    private val viewAllAttractions: () -> Unit = {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.attractionsNearby.collect {
//                    val dialog =
//                        PlacesNearbyDialog.instance(it)
//
//                    dialog.show(parentFragmentManager, "PLACES_NEARBY_DIALOG")
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

        recyclerView.setHasFixedSize(false)

        recyclerView.layoutManager = LinearLayoutManager(context)

        val adapter = PlacesNearbyDialogAdapter(data)

        recyclerView.adapter = adapter

        val dialog = dialogBuilder?.create()

        buttonClose.setOnClickListener {
            dialog?.dismiss()
        }
        dialog?.setCancelable(true)
        dialog?.setCanceledOnTouchOutside(true)

        dialog?.show()
    }

    private fun showImageDialog(data: List<String>) {
        Log.d("detailsDialogTag", "showImageDialog: data: $data")
        val dialogBuilder = context?.let { MaterialAlertDialogBuilder(it) }
        val inflater = layoutInflater
//TODO Coil don't load images
        val dialogView = inflater.inflate(R.layout.dialog_details_image, null)

        dialogBuilder?.setView(dialogView)

        val recyclerView = dialogView.findViewById<RecyclerView>(R.id.recycler_view)
        val buttonClose = dialogView.findViewById<ShapeableImageView>(R.id.img_close_icon)

        val carouselLayoutManager = CarouselLayoutManager()
        carouselLayoutManager.setCarouselStrategy(FullScreenCarouselStrategy())

        recyclerView.layoutManager = carouselLayoutManager

//        recyclerView.layoutManager =
//            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        recyclerView.setHasFixedSize(false)

        val adapter = DetailsImageAdapter(data)
        recyclerView.adapter = adapter

        val dialog = dialogBuilder?.create()

        buttonClose.setOnClickListener {
            dialog?.dismiss()
        }
        dialog?.setCancelable(true)
        dialog?.setCanceledOnTouchOutside(true)
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog?.show()
    }

    private val openWebView: (String) -> Unit = {
        val bundle = Bundle()
        bundle.putString("WEB_VIEW_URL", it)

        findNavController().navigate(R.id.fragmentWebView, bundle)
    }
}