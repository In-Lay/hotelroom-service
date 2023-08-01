package com.inlay.hotelroomservice.presentation.fragments.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.FullScreenCarouselStrategy
import com.inlay.hotelroomservice.R
import com.inlay.hotelroomservice.databinding.DialogDetailsImageBinding
import com.inlay.hotelroomservice.presentation.adapters.detailsdialog.detailsimage.DetailsImageAdapter

class DetailsImageDialog : DialogFragment() {
    private lateinit var binding: DialogDetailsImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.dialog_details_image,
            container,
            false
        )
        val imageUrlList = arguments?.getStringArrayList("IMAGE_URL_DATA").orEmpty()
        bindAdapter(imageUrlList)

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
            dialog.window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
        }
    }

    private fun bindAdapter(data: List<String>) {
        val carouselLayoutManager = CarouselLayoutManager()
        carouselLayoutManager.setCarouselStrategy(FullScreenCarouselStrategy())
        binding.recyclerView.layoutManager = carouselLayoutManager
        binding.recyclerView.setHasFixedSize(false)
        binding.recyclerView.adapter = DetailsImageAdapter(data)
    }

    companion object {
        fun instance(imageUrlList: List<String>) = DetailsImageDialog().apply {
            arguments = Bundle().apply {
                putStringArrayList("IMAGE_URL_DATA", imageUrlList as ArrayList<String>)
            }
        }
    }
}