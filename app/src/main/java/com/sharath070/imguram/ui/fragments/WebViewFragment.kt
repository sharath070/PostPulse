package com.sharath070.imguram.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.sharath070.imguram.R
import com.sharath070.imguram.databinding.FragmentWebViewBinding
import com.sharath070.imguram.ui.activities.MainActivity
import com.sharath070.imguram.ui.viewModel.PostsViewModel
import android.webkit.CookieManager

class WebViewFragment : Fragment() {

    private lateinit var binding: FragmentWebViewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_web_view, container, false)
        return binding.root
    }

    private val args: WebViewFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val posts = args.post

        Log.i("@@@@", posts.link.toString())

        binding.webView.apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.loadsImagesAutomatically = true
            webViewClient = WebViewClient()
            posts.link?.let { loadUrl(it) }
        }

    }


}