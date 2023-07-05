package com.inlay.hotelroomservice.presentation.adapters

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.inlay.hotelroomservice.databinding.HotelsItemBinding
import com.inlay.hotelroomservice.presentation.viewmodels.hotels.item.HotelsItemViewModel

class HotelsItemViewHolder(private val binding: HotelsItemBinding) : ViewHolder(binding.root) {
    fun bind(viewModel: HotelsItemViewModel) {
        binding.viewModel = viewModel
    }
}