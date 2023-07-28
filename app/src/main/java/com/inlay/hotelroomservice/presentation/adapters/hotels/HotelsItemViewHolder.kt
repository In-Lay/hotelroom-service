package com.inlay.hotelroomservice.presentation.adapters.hotels

import android.content.Context
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.inlay.hotelroomservice.R
import com.inlay.hotelroomservice.databinding.HotelsItemBinding
import com.inlay.hotelroomservice.presentation.viewmodels.hotels.item.HotelsItemViewModel

class HotelsItemViewHolder(private val binding: HotelsItemBinding, private val context: Context) :
    ViewHolder(binding.root) {
    fun bind(viewModel: HotelsItemViewModel, listType: String) {
        binding.viewModel = viewModel
        if (listType == "stays") binding.btnAddRemove.text = context.getString(R.string.btn_remove)
    }
}