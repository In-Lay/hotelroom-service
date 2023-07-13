package com.inlay.hotelroomservice.presentation.fragments.hotels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.inlay.hotelroomservice.R
import com.inlay.hotelroomservice.databinding.FragmentHotelsBinding
import com.inlay.hotelroomservice.presentation.activities.MainActivity
import com.inlay.hotelroomservice.presentation.adapters.hotels.HotelsListAdapter
import com.inlay.hotelroomservice.presentation.viewmodels.hotels.HotelsViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class HotelsFragment : Fragment() {
    private lateinit var binding: FragmentHotelsBinding
    private val hotelsViewModel: HotelsViewModel by activityViewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_hotels, container, false)
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindAdapter()
    }

    private fun bindAdapter() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                hotelsViewModel.hotelsDataList.collectLatest {
                    binding.recyclerView.adapter =
                        HotelsListAdapter(
                            it,
                            (activity as MainActivity).goToDetails
                        )
                    binding.recyclerView.layoutManager = LinearLayoutManager(context)
                    binding.recyclerView.setHasFixedSize(false)
                }
            }
        }
    }
}