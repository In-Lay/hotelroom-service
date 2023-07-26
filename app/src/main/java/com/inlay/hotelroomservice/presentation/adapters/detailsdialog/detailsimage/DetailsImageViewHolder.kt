package com.inlay.hotelroomservice.presentation.adapters.detailsdialog.detailsimage

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.inlay.hotelroomservice.databinding.DetailsPhotoItemBinding

class DetailsImageViewHolder(private val binding: DetailsPhotoItemBinding) :
    ViewHolder(binding.root) {
    fun bind(url: String) {
        binding.imgDetailsItemPhoto.load(
            url.replace(
                "{width}", "1200"
            ).replace("{height}", "1000")
        )
    }
}