package com.inlay.hotelroomservice.presentation.fragments.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.inlay.hotelroomservice.R
import com.inlay.hotelroomservice.databinding.FragmentWebViewBinding

class FragmentWebView : Fragment() {
    private lateinit var binding: FragmentWebViewBinding
    private var webViewUrl = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_web_view, container, false)

        webViewUrl = arguments?.getString("WEB_VIEW_URL").orEmpty()

        binding.lifecycleOwner = this

        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabCloseWebView.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.webView.apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            loadUrl(webViewUrl)
        }
    }
}