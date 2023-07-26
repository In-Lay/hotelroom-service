package com.inlay.hotelroomservice.presentation.fragments.details

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.inlay.hotelroomservice.R
import com.inlay.hotelroomservice.databinding.DialogPlacesNearbyBinding
import com.inlay.hotelroomservice.presentation.adapters.detailsdialog.placesnearby.PlacesNearbyDialogAdapter
import com.inlay.hotelroomservice.presentation.models.details.NearbyPlace


class PlacesNearbyDialog : DialogFragment() {
    private lateinit var binding: DialogPlacesNearbyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.dialog_places_nearby, container, false)

        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelableArrayList("DIALOG_DATA", Parcelable::class.java)
                .orEmpty() as List<NearbyPlace>
        } else arguments?.getParcelableArrayList("DIALOG_DATA")

        bindAdapter(data.orEmpty())

        binding.imgCloseIcon.setOnClickListener {
            dialog?.dismiss()
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog

        if (dialog != null) {
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(true)
        }
    }

    private fun bindAdapter(data: List<NearbyPlace>) {

        binding.recyclerView.setHasFixedSize(false)

        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        binding.recyclerView.adapter = PlacesNearbyDialogAdapter(data)
    }

    companion object {
        fun instance(data: List<NearbyPlace>) = PlacesNearbyDialog().apply {
            Log.d(
                "DetailsLog", "PlacesNearbyDialog: instance: data: $data"
            )
            arguments = Bundle().apply {
                val dataAsParcelable = data as ArrayList<out Parcelable>
                Log.d(
                    "DetailsLog",
                    "PlacesNearbyDialog: instance: dataAsParcelable: $dataAsParcelable"
                )
                putParcelableArrayList("DIALOG_DATA", dataAsParcelable)
            }
        }
    }

}