package com.dmitriy.movie.view.webview

import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.dmitriy.movie.R
import com.dmitriy.movie.adapters.KEY_MOVIE_DETAILS
import com.dmitriy.movie.databinding.FragmentWebviewBinding
import com.dmitriy.movie.util.Constants
import com.dmitriy.movie.view.tabs.dashboard.KEY_MOVIE_DETAILS_ID

class WebViewFragment : Fragment(R.layout.fragment_webview) {

    private lateinit var binding: FragmentWebviewBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWebviewBinding.bind(view)

        updateUI()
    }

    private fun updateUI() {
        val movieId = requireArguments().getInt(KEY_MOVIE_DETAILS_ID)

        Log.d("logs", movieId.toString())
        Log.d("logs", Constants.URL_MOVIE + movieId)
        binding.webView.apply {
            loadUrl(Constants.URL_MOVIE + movieId)
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient()
        }
    }
}