package com.inlay.hotelroomservice.presentation.adapters.search

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.inlay.hotelroomservice.databinding.SearchLocationItemBinding
import com.inlay.hotelroomservice.presentation.viewmodels.search.item.SearchLocationsItemViewModel

class SearchLocationsItemViewHolder(private val binding: SearchLocationItemBinding) :
    ViewHolder(binding.root) {
    fun bind(viewModel: SearchLocationsItemViewModel) {
        binding.viewModel = viewModel
    }
}