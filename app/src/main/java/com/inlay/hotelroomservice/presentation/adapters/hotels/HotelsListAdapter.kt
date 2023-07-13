package com.inlay.hotelroomservice.presentation.adapters.hotels

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.inlay.hotelroomservice.databinding.HotelsItemBinding
import com.inlay.hotelroomservice.presentation.models.hotelsitem.HotelsItemUiModel
import com.inlay.hotelroomservice.presentation.viewmodels.hotels.item.HotelsItemViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HotelsListAdapter(
    private val hotelsList: List<HotelsItemUiModel>,
    private val goToDetails: (String) -> Unit
) : Adapter<HotelsItemViewHolder>(), KoinComponent {
    private lateinit var binding: HotelsItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelsItemViewHolder {
        binding = HotelsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        binding.lifecycleOwner = parent.findViewTreeLifecycleOwner()
        return HotelsItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HotelsItemViewHolder, position: Int) {
        val viewModel: HotelsItemViewModel by inject()
        val singleHotelData = hotelsList[position]
        viewModel.initializeData(
            singleHotelData, goToDetails
        )
        holder.bind(viewModel)
    }

    override fun getItemCount(): Int {
        return hotelsList.size
    }
}