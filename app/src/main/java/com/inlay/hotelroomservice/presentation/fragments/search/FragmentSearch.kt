package com.inlay.hotelroomservice.presentation.fragments.search

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.Pair
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.datepicker.MaterialDatePicker
import com.inlay.hotelroomservice.R
import com.inlay.hotelroomservice.databinding.FragmentSearchBinding
import com.inlay.hotelroomservice.extensions.isNetworkAvailable
import com.inlay.hotelroomservice.presentation.activities.MainActivity
import com.inlay.hotelroomservice.presentation.adapters.search.SearchLocationsAdapter
import com.inlay.hotelroomservice.presentation.models.SearchDataUiModel
import com.inlay.hotelroomservice.presentation.models.hotelsitem.DatesModel
import com.inlay.hotelroomservice.presentation.models.locations.SearchLocationsUiModel
import com.inlay.hotelroomservice.presentation.viewmodels.hotels.HotelsViewModel
import com.inlay.hotelroomservice.presentation.viewmodels.search.SearchViewModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Calendar

class FragmentSearch : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private val searchViewModel: SearchViewModel by viewModel()
    private val hotelsViewModel: HotelsViewModel by activityViewModel()
    private val simpleDateFormat: SimpleDateFormat by inject()
    private var isOnline = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        isOnline = requireContext().isNetworkAvailable()
        searchViewModel.init(isOnline, openDatePicker, searchHotels)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.supportToolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.supportToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

//        binding.appbarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
//            val progress: Float = -verticalOffset / appBarLayout.totalScrollRange.toFloat()
//
//            // Adjust the width of the SearchView
//            val layoutParams = binding.searchBar.layoutParams
//            layoutParams.width = (((binding.supportToolbar.width - binding.supportToolbar.contentInsetStart * 2) * progress).toInt() +
//                    (appBarLayout.width - binding.supportToolbar.contentInsetStart * 2) * (1 - progress)).toInt()
//            binding.searchBar.layoutParams = layoutParams
//
//            // Adjust the margins of the SearchView
//            val params = binding.searchBar.layoutParams as CollapsingToolbarLayout.LayoutParams
//            params.setMargins(
//                (binding.supportToolbar.contentInsetStart * progress).toInt(),
//                0,
//                (binding.supportToolbar.contentInsetStart * progress).toInt(),
//                (appBarLayout.height - binding.searchBar.height) / 2 * (1 - progress).toInt()
//            )
//            binding.searchBar.requestLayout()
//        })

        binding.viewModel = searchViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchViewModel.supportText.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }

        bindAdapter()

        searchViewModel.dates.observe(viewLifecycleOwner) {
            binding.tvDates.editText?.text =
                Editable.Factory.getInstance().newEditable(it.toString())
        }

        binding.searchView.editText.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchViewModel.getSearchLocations(textView.text.toString())
            }
            true
        }

        binding.tvDates.setEndIconOnClickListener {
            openDatePicker()
        }
    }

    private fun bindAdapter() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchViewModel.searchLocationsData.collect {
                    binding.rwSearchResults.adapter = SearchLocationsAdapter(it, selectItem)
                    binding.rwSearchResults.layoutManager = GridLayoutManager(context, 2)
                    binding.rwSearchResults.setHasFixedSize(false)
                }
            }
        }
    }

    private val selectItem: (SearchLocationsUiModel) -> Unit = {
        Log.d("SearchTag", "Item selected")
        searchViewModel.setCurrentItemModel(it)
        binding.searchBar.text = it.title
        binding.tvCurrentLocation.text = it.title
        binding.searchView.hide()
    }

    private val searchHotels: (SearchDataUiModel) -> Unit = {
        Toast.makeText(context, "Search", Toast.LENGTH_SHORT).show()
        if (isOnline) {
            //TODO To uncomment
//            lifecycleScope.launch {
//                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                    hotelsViewModel.getHotelsRepo(
//                        isOnline,
//                        it.geoId ?: "60763",
//                        it.checkInDate,
//                        it.checkOutDate,
//                        it.currencyCode
//                    )
//                }
//            }
            (activity as MainActivity).goToHotels()
        } else Toast.makeText(context, "Check Internet connection", Toast.LENGTH_SHORT).show()
    }

    private val openDatePicker: () -> Unit = {
        Toast.makeText(context, "Date picker", Toast.LENGTH_SHORT).show()
        Log.d("SearchTag", "Date picker opened")
        var checkIn = ""
        var checkOut = ""
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val checkInDate = calendar.timeInMillis

        calendar.add(Calendar.DAY_OF_YEAR, 7)
        val checkOutDate = calendar.timeInMillis

        val materialDatePicker =
            MaterialDatePicker.Builder.dateRangePicker().setTitleText("Select Dates").setSelection(
                Pair.create(
                    checkInDate, checkOutDate
                )
            ).build()

        materialDatePicker.addOnPositiveButtonClickListener {
            checkIn = simpleDateFormat.format(it.first)
            checkOut = simpleDateFormat.format(it.second)
            Log.d(
                "SearchTag",
                "materialDatePicker: positive button: checkIn: $checkIn, checkOut: $checkOut"
            )
            searchViewModel.setDates(DatesModel(checkIn, checkOut))
            materialDatePicker.dismiss()
        }
        materialDatePicker.show(activity?.supportFragmentManager!!, "Date picker")
        Log.d("SearchTag", "materialDatePicker: isVisible: ${materialDatePicker.isVisible}")

        Log.d("SearchTag", "checkIn: $checkIn, checkOut: $checkOut")
    }
}