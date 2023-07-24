package com.inlay.hotelroomservice.presentation.fragments.details

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.inlay.hotelroomservice.R
import com.inlay.hotelroomservice.databinding.DialogPlacesNearbyBinding
import com.inlay.hotelroomservice.presentation.adapters.detailsdialog.PlacesNearbyDialogAdapter
import com.inlay.hotelroomservice.presentation.models.details.NearbyPlace

class PlacesNearbyDialog : DialogFragment() {
    private lateinit var binding: DialogPlacesNearbyBinding
    private lateinit var dialog: Dialog

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogPlacesNearbyBinding.inflate(layoutInflater)
        dialog = Dialog(requireContext(), R.style.DialogTheme)

        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)

        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelableArrayList("DIALOG_DATA", Parcelable::class.java)
                .orEmpty() as List<NearbyPlace>
        } else arguments?.getParcelableArrayList("DIALOG_DATA")
        Log.d(
            "DetailsLog", "PlacesNearbyDialog: onCreateDialog: data: $data"
        )
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(binding.root)
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        bindAdapter(data.orEmpty())

        binding.imgCloseIcon.setOnClickListener {
            dialog.dismiss()
        }

        return dialog
    }

    private fun bindAdapter(data: List<NearbyPlace>) {

        binding.recyclerView.setHasFixedSize(false)
//            "image" -> {
//                //            layoutManager.setCarouselStrategy()
//                binding.recyclerView.layoutManager = CarouselLayoutManager()
//            }


        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        binding.recyclerView.adapter = PlacesNearbyDialogAdapter(data)
    }

    companion object {
        fun instance(data: List<NearbyPlace>) = PlacesNearbyDialog().apply {
            Log.d(
                "DetailsLog", "PlacesNearbyDialog: instance: data: $data"
            )
            arguments = Bundle().apply {
//                putString("DIALOG_DATA", data)
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