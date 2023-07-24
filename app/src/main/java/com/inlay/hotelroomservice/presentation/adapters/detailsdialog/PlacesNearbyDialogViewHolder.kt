package com.inlay.hotelroomservice.presentation.adapters.detailsdialog

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.inlay.hotelroomservice.databinding.PlacesNearbyItemBinding
import com.inlay.hotelroomservice.presentation.viewmodels.details.dialog.PlaceNearbyViewModel

class PlacesNearbyDialogViewHolder(private val binding: PlacesNearbyItemBinding) :
    ViewHolder(binding.root) {
    fun bind(viewModel: PlaceNearbyViewModel) {
        binding.viewModel = viewModel
    }
}