package com.inlay.hotelroomservice.presentation.adapters.detailsdialog.placesnearby

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.inlay.hotelroomservice.databinding.PlacesNearbyItemBinding
import com.inlay.hotelroomservice.presentation.models.details.NearbyPlace
import com.inlay.hotelroomservice.presentation.viewmodels.details.dialog.PlaceNearbyViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PlacesNearbyDialogAdapter(private val dataList: List<NearbyPlace>) :
    Adapter<PlacesNearbyDialogViewHolder>(), KoinComponent {
    private lateinit var binding: PlacesNearbyItemBinding

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): PlacesNearbyDialogViewHolder {
        binding =
            PlacesNearbyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        binding.lifecycleOwner = parent.findViewTreeLifecycleOwner()

        return PlacesNearbyDialogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlacesNearbyDialogViewHolder, position: Int) {
        val viewModel: PlaceNearbyViewModel by inject()
        val singleDataObject = dataList[position]

        viewModel.initializeData(singleDataObject)

        holder.bind(viewModel)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}