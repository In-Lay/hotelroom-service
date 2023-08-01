package com.inlay.hotelroomservice.presentation.adapters.detailsdialog.detailsimage

import android.util.Log
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.inlay.hotelroomservice.databinding.DetailsPhotoItemBinding

class DetailsImageViewHolder(private val binding: DetailsPhotoItemBinding) :
    ViewHolder(binding.root) {
    fun bind(url: String?) {
        Log.d("detailsDialogTag", "bind: url: $url")
        binding.imgDetailsItemPhoto.load(
            url
        )
    }
}