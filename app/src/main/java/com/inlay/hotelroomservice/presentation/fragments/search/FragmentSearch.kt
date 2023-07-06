package com.inlay.hotelroomservice.presentation.fragments.search

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.util.Pair
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.inlay.hotelroomservice.R
import com.inlay.hotelroomservice.databinding.FragmentSearchBinding
import com.inlay.hotelroomservice.extensions.isNetworkAvailable
import com.inlay.hotelroomservice.presentation.activities.MainActivity
import com.inlay.hotelroomservice.presentation.adapters.search.SearchLocationsAdapter
import com.inlay.hotelroomservice.presentation.models.SearchDataUiModel
import com.inlay.hotelroomservice.presentation.models.hotelsitem.DatesModel
import com.inlay.hotelroomservice.presentation.viewmodels.hotels.HotelsViewModel
import com.inlay.hotelroomservice.presentation.viewmodels.search.SearchViewModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat

class FragmentSearch : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private val searchViewModel: SearchViewModel by viewModel()
    private val hotelsViewModel: HotelsViewModel by activityViewModel()
    private val simpleDateFormat: SimpleDateFormat by inject()
    private var isOnline = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)

        (activity as MainActivity).setSupportActionBar(binding.searchBar)

        isOnline = requireContext().isNetworkAvailable()
        searchViewModel.init(isOnline, openDatePicker, searchHotels)
        binding.lifecycleOwner = viewLifecycleOwner
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
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchViewModel.getSearchLocations(textView.text.toString())
            }
            true
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

    private val selectItem: (String) -> Unit = {
        searchViewModel.setCurrentItemGeoId(it)
    }

    private val searchHotels: (SearchDataUiModel) -> Unit = {
        if (isOnline) {
            lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    hotelsViewModel.getHotelsRepo(
                        isOnline,
                        it.geoId ?: "60763",
                        it.checkInDate,
                        it.checkOutDate,
                        it.currencyCode
                    )
                }
            }
        } else Toast.makeText(context, "Check Internet connection", Toast.LENGTH_SHORT).show()
    }

    private val openDatePicker: () -> DatesModel = {
        var checkIn = ""
        var checkOut = ""
        val materialDatePicker =
            MaterialDatePicker.Builder.dateRangePicker().setTitleText("Select Dates").setSelection(
                Pair.create(
                    MaterialDatePicker.thisMonthInUtcMilliseconds(),
                    MaterialDatePicker.todayInUtcMilliseconds()
                )
            ).build()

        materialDatePicker.addOnDismissListener {
            materialDatePicker.dismiss()
        }

        materialDatePicker.addOnPositiveButtonClickListener {
            checkIn = simpleDateFormat.format(it.first)
            checkOut = simpleDateFormat.format(it.second)
            materialDatePicker.dismiss()
        }
        DatesModel(checkIn, checkOut)
    }
}