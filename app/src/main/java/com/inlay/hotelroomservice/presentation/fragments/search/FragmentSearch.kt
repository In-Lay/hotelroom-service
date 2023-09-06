package com.inlay.hotelroomservice.presentation.fragments.search

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
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
import com.inlay.hotelroomservice.presentation.viewmodels.search.AppSearchViewModel
import com.inlay.hotelroomservice.presentation.viewmodels.search.SearchViewModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import kotlin.math.abs

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

        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.supportToolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(false)
        }

        binding.supportToolbar.apply {
            setNavigationIconTint(com.google.android.material.R.attr.iconTint)
            setTitleTextColor(requireContext().getColor(R.color.md_theme_dark_surface))
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }

        binding.appbarLayout.addOnOffsetChangedListener { _, verticalOffset ->
            val totalScrollRange = binding.appbarLayout.totalScrollRange

            val isCollapsed = abs(verticalOffset) == totalScrollRange
            if (isCollapsed) {
                adjustSearchBarPosition()
            } else resetSearchBarPosition()
        }

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
                if (isOnline) searchViewModel.getSearchLocations(textView.text.toString())
                else Toast.makeText(
                    context, context?.getString(R.string.no_internet_connection), Toast.LENGTH_SHORT
                ).show()
            }
            true
        }

        binding.tvDates.setEndIconOnClickListener {
            openDatePicker()
        }
    }

    private fun adjustSearchBarPosition() {
        val searchBarLayoutParams =
            binding.searchBar.layoutParams as CollapsingToolbarLayout.LayoutParams

        val homeButtonWidth = binding.supportToolbar.navigationIcon?.intrinsicWidth ?: 0

        val maxSearchBarWidth = binding.supportToolbar.width - homeButtonWidth + 50

        searchBarLayoutParams.width = maxSearchBarWidth - 150
        searchBarLayoutParams.marginStart = homeButtonWidth + 90

        searchBarLayoutParams.topMargin = 10
        searchBarLayoutParams.bottomMargin = 10
        searchBarLayoutParams.marginEnd = 10

        binding.searchBar.layoutParams = searchBarLayoutParams
    }

    private fun resetSearchBarPosition() {
        val searchBarLayoutParams =
            binding.searchBar.layoutParams as CollapsingToolbarLayout.LayoutParams

        searchBarLayoutParams.collapseMode = CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PIN
        searchBarLayoutParams.topMargin = 0
        searchBarLayoutParams.marginStart = 10
        searchBarLayoutParams.marginEnd = 10
        searchBarLayoutParams.bottomMargin = 16
        searchBarLayoutParams.width = binding.supportToolbar.width - 20

        binding.searchBar.layoutParams = searchBarLayoutParams
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
        searchViewModel.setCurrentItemModel(it)
        binding.searchBar.setText(it.title)
        binding.searchView.hide()
    }

    private val searchHotels: (SearchDataUiModel) -> Unit = {
        if (isOnline) {
            //TODO Uncomment to Network
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
            (activity as MainActivity).goToHotels()
        } else Toast.makeText(
            context, context?.getString(R.string.no_internet_connection), Toast.LENGTH_SHORT
        ).show()
    }

    private val openDatePicker: () -> Unit = {
        var checkIn: String
        var checkOut: String
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val checkInDate = calendar.timeInMillis

        calendar.add(Calendar.DAY_OF_YEAR, 7)
        val checkOutDate = calendar.timeInMillis

        val materialDatePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText(context?.getString(R.string.btn_pick_dates)).setSelection(
                Pair.create(
                    checkInDate, checkOutDate
                )
            ).build()

        materialDatePicker.addOnPositiveButtonClickListener {
            checkIn = simpleDateFormat.format(it.first)
            checkOut = simpleDateFormat.format(it.second)
            searchViewModel.setDates(DatesModel(checkIn, checkOut))
            materialDatePicker.dismiss()
        }
        materialDatePicker.show(activity?.supportFragmentManager!!, "Date picker")
    }
}