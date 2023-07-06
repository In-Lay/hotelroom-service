package com.inlay.hotelroomservice.presentation.adapters.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.inlay.hotelroomservice.databinding.SearchLocationItemBinding
import com.inlay.hotelroomservice.presentation.models.locations.SearchLocationsUiModel
import com.inlay.hotelroomservice.presentation.viewmodels.search.item.SearchLocationsItemViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SearchLocationsAdapter(
    private val searchLocationsList: List<SearchLocationsUiModel>,
    private val selectItem: (String) -> Unit
) : Adapter<SearchLocationsItemViewHolder>(), KoinComponent {
    private lateinit var binding: SearchLocationItemBinding

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchLocationsItemViewHolder {
        binding =
            SearchLocationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.lifecycleOwner = parent.findViewTreeLifecycleOwner()
        return SearchLocationsItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchLocationsItemViewHolder, position: Int) {
        val viewModel: SearchLocationsItemViewModel by inject()
        val searchLocationItem = searchLocationsList[position]
        viewModel.initialize(searchLocationItem, selectItem)
        holder.bind(viewModel)
    }

    override fun getItemCount(): Int {
        return searchLocationsList.size
    }
}