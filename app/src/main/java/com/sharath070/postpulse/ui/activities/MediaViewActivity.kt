package com.sharath070.postpulse.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.sharath070.postpulse.databinding.ActivityMediaViewBinding
import com.sharath070.postpulse.model.galleryTags.PostAndPosition
import com.sharath070.postpulse.ui.adapters.MediaAdapter
import com.sharath070.postpulse.utils.getSerializable

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