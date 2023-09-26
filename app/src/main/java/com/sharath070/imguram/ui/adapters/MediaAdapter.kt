package com.sharath070.imguram.ui.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sharath070.imguram.R
import com.sharath070.imguram.databinding.MediaItemBinding
import com.sharath070.imguram.model.galleryTags.Image

class MediaAdapter(private val context: Context, private val post: List<Image>) :
    RecyclerView.Adapter<MediaAdapter.ViewHolder>() {

    inner class ViewHolder(val itemBinding: MediaItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            MediaItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return post.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val link = post[position].link ?: ""
        val mp4Size = post[position].mp4_size ?: 0


        if (mp4Size == 0) {

            holder.itemBinding.ivImage.visibility = View.VISIBLE
            holder.itemBinding.vvVideo.visibility = View.GONE

            Glide.with(context)
                .load(link)
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .into(holder.itemBinding.ivImage)

            holder.itemBinding.apply {

            }


        } else {

            holder.itemBinding.ivImage.visibility = View.GONE
            holder.itemBinding.vvVideo.visibility = View.VISIBLE

            holder.itemBinding.vvVideo.setVideoURI(Uri.parse(link))
            val mediaController = MediaController(context)
            holder.itemBinding.vvVideo.setMediaController(mediaController)
            holder.itemBinding.vvVideo.setOnPreparedListener { mediaPlayer ->
                mediaPlayer.start()


            }


        }
    }


}