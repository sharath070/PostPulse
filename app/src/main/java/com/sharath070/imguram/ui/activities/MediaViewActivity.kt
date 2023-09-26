package com.sharath070.imguram.ui.activities

import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.sharath070.imguram.R
import com.sharath070.imguram.databinding.ActivityMediaViewBinding
import com.sharath070.imguram.model.galleryTags.PostAndPosition
import com.sharath070.imguram.ui.adapters.MediaAdapter
import com.sharath070.imguram.utils.getSerializable

class MediaViewActivity : AppCompatActivity() {

    private var _binding: ActivityMediaViewBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMediaViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val postAndPosition: PostAndPosition = getSerializable("postData", PostAndPosition::class.java)

        val post = postAndPosition.post
        val position = postAndPosition.position

        if (post.size == 1){
            binding.circleIndicator.visibility = View.GONE
        }
        else{
            binding.circleIndicator.visibility = View.VISIBLE
        }

        val adapter = MediaAdapter(this@MediaViewActivity, post)
        binding.mediaViewPager.adapter = adapter

        binding.mediaViewPager.setCurrentItem(position, false)
        binding.circleIndicator.setViewPager(binding.mediaViewPager)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}