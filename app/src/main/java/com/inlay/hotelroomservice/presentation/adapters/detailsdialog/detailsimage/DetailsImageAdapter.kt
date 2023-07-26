package com.inlay.hotelroomservice.presentation.adapters.detailsdialog.detailsimage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.inlay.hotelroomservice.databinding.DetailsPhotoItemBinding

class DetailsImageAdapter(private val imageUrlList: List<String>) :
    Adapter<DetailsImageViewHolder>() {
    private lateinit var binding: DetailsPhotoItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailsImageViewHolder {
        binding =
            DetailsPhotoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        binding.lifecycleOwner = parent.findViewTreeLifecycleOwner()

        return DetailsImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DetailsImageViewHolder, position: Int) {
        val imageUrl = imageUrlList[position]

        holder.bind(imageUrl)
    }

    override fun getItemCount(): Int {
        return imageUrlList.size
    }
}