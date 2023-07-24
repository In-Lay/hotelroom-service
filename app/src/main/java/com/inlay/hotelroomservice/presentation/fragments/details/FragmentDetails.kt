package com.inlay.hotelroomservice.presentation.fragments.details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.inlay.hotelroomservice.R
import com.inlay.hotelroomservice.databinding.FragmentDetailsBinding
import com.inlay.hotelroomservice.presentation.viewmodels.details.DetailsViewModel
import com.inlay.hotelroomservice.presentation.viewmodels.hotels.HotelsViewModel
import com.squareup.moshi.Moshi
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentDetails : Fragment() {
    private lateinit var binding: FragmentDetailsBinding
    private val viewModel: DetailsViewModel by viewModel()
    private val hotelsViewModel: HotelsViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

//        val detailsSearchModel = arguments?.getString("HOTEL_DETAILS_SEARCH")
//            ?.let { moshi.adapter(HotelDetailsSearchModel::class.java).fromJson(it) }
//            ?: HotelDetailsSearchModel("", DatesModel("", ""), "")
//
//        Log.d(
//            "DetailsLog",
//            "FragmentDetails: detailsSearchModel: $detailsSearchModel \nid: ${detailsSearchModel.id}, \ndates: ${detailsSearchModel.dates}, \ncurrency: ${detailsSearchModel.currency}"
//        )
        lifecycleScope.launch {
            hotelsViewModel.hotelDetailsSearchModel.collect {
                viewModel.initializeData(
                    openImageDialog,
                    viewAllRestaurants,
                    viewAllAttractions,
                    openLinkInBrowser,
                    closeWebView,
                    it.id,
                    it.dates,
                    it.currency
                )
            }
        }

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private val openImageDialog: () -> Unit = {
        Toast.makeText(context, "Open Image Dialog", Toast.LENGTH_SHORT).show()
    }

    private val viewAllRestaurants: () -> Unit = {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.restaurantsNearby.collect {
//                    val stringData = moshi.adapter(DialogDataModel::class.java).toJson(
//                        DialogDataModel(it)
//                    )
                    val dialog =
                        PlacesNearbyDialog.instance(it)
                    dialog.show(parentFragmentManager, "PLACES_NEARBY_DIALOG")
                }
            }
        }
        Toast.makeText(context, "View All Restaurants", Toast.LENGTH_SHORT).show()
    }

    private val viewAllAttractions: () -> Unit = {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.attractionsNearby.collect {
//                    val stringData = moshi.adapter(DialogDataModel::class.java).toJson(
//                        DialogDataModel(it)
//                    )
                    Log.d(
                        "DetailsLog", "FragmentDetails: viewAllAttractions: attractionsNearby: $it"
                    )
                    val dialog =
                        PlacesNearbyDialog.instance(it)

                    dialog.show(parentFragmentManager, "PLACES_NEARBY_DIALOG")
                }
            }
        }
        Toast.makeText(context, "View All Attractions", Toast.LENGTH_SHORT).show()
    }

    private val openLinkInBrowser: (String) -> Unit = {
        binding.fabCloseWebView.visibility = View.VISIBLE
        binding.webView.apply {
            visibility = View.VISIBLE
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            loadUrl(it)
        }
    }

    private val closeWebView: () -> Unit = {
        binding.webView.visibility = View.GONE
        binding.fabCloseWebView.visibility = View.GONE
    }
}