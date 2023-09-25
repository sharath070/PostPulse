package com.sharath070.imguram.ui.adapters

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sharath070.imguram.R
import com.sharath070.imguram.databinding.ImageViewRecyclerItemBinding
import com.sharath070.imguram.model.galleryTags.Data
import com.sharath070.imguram.model.galleryTags.Image

class ViewPagerAdapter(private val context: Context, private val imgList: List<Image>) :
    RecyclerView.Adapter<ViewPagerAdapter.ViewPagerViewHolder>() {

    var click = 0

    inner class ViewPagerViewHolder(val itemBinding: ImageViewRecyclerItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        val binding =
            ImageViewRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewPagerViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return imgList.size
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {

        val link = imgList[position].link ?: ""
        val mp4Size = imgList[position].mp4_size ?: 0
        if (mp4Size == 0) {

            holder.itemBinding.ibPlayerAction.visibility = View.GONE
            holder.itemBinding.vvPost.visibility = View.GONE
            holder.itemBinding.ivPic.visibility = View.VISIBLE

            Glide.with(context)
                .load(link)
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .into(holder.itemBinding.ivPic)
        } else {
            holder.itemBinding.ivPic.visibility = View.GONE
            holder.itemBinding.vvPost.visibility = View.VISIBLE
            holder.itemBinding.ibPlayerAction.visibility = View.VISIBLE

            holder.itemBinding.vvPost.setVideoURI(Uri.parse(link))
            holder.itemBinding.vvPost.setOnPreparedListener { mediaPlayer ->
                mediaPlayer.start()
                mediaPlayer.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING)
            }



            holder.itemBinding.root.setOnClickListener {
                val image = imgList[position]
                onItemClickListener?.let { it(image) }
            }

        }

    }

    private var onItemClickListener: ((Image) -> Unit)? = null
    fun setOnItemClickListener(listener: (Image) -> Unit) {
        onItemClickListener = listener
    }


}
