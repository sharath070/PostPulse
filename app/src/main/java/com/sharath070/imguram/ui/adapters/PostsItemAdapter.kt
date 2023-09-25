package com.sharath070.imguram.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.sharath070.imguram.R
import com.sharath070.imguram.databinding.ItemImageCardBinding
import com.sharath070.imguram.model.galleryTags.Data
import com.sharath070.imguram.model.galleryTags.Image

class PostsItemAdapter(private val context: Context) :
    ListAdapter<Data, PostsItemAdapter.ViewHolder>(GalleryDiffUtil()) {

    private var click = 0


    inner class ViewHolder(val itemBinding: ItemImageCardBinding) :
        RecyclerView.ViewHolder(itemBinding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemImageCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val caption = getItem(position).images?.firstOrNull()?.description ?: ""
        val userName = getItem(position).id ?: "/*ID-Hidden*/"
        val data = getItem(position)
        val imagesList = data.images ?: listOf()



        holder.itemBinding.ibOption.setOnClickListener { view ->
            showPopupMenu(view, data)
        }


        holder.itemBinding.apply {
            tvCaption.text = caption
            holder.itemBinding.ivId.text = userName

            val viewPagerAdapter = ViewPagerAdapter(context, imagesList)
            imgLayout.viewPager.adapter = viewPagerAdapter
            imgLayout.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            if (imagesList.size == 1) {
                imgLayout.circleIndicator.visibility = View.GONE
            }
            imgLayout.circleIndicator.setViewPager(imgLayout.viewPager)

            clickListenerForImgLayout(holder, position)

            holder.itemBinding.imgLayout.ibPlayerAction.setOnClickListener {
                if (click++ % 2 == 0){
                    holder.itemBinding.imgLayout.ibPlayerAction.setImageResource(R.drawable.ic_mute)

                }
                else{
                    holder.itemBinding.imgLayout.ibPlayerAction.setImageResource(R.drawable.ic_volume)
                }
            }
        }
    }

    private fun clickListenerForImgLayout(holder: ViewHolder, position: Int) {



    }

    private fun showImgItemCLick(position: Int, holder: ViewHolder) {

        val post = getItem(position)

        val viewPagerPosition = holder.itemBinding.imgLayout.viewPager.currentItem
        val currentImage = post.images?.get(viewPagerPosition)
        onPostClickListener?.let { image ->
            if (currentImage != null) {
                image(currentImage)
            }
        }
    }

    class GalleryDiffUtil : DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(
            oldItem: Data, newItem: Data
        ): Boolean {
            return oldItem.images == newItem.images
        }

        override fun areContentsTheSame(
            oldItem: Data, newItem: Data
        ): Boolean {
            return oldItem == newItem
        }

    }

    private var onPostClickListener: ((Image) -> Unit)? = null
    fun setOnPostClickListener(listener: (Image) -> Unit) {
        onPostClickListener = listener
    }

    private var onItemClickListener: ((Data) -> Unit)? = null
    fun setOnItemClickListener(listener: (Data) -> Unit) {
        onItemClickListener = listener
    }



    private fun showPopupMenu(view: View, data: Data) {
        val popupMenu = PopupMenu(context, view)
        popupMenu.inflate(R.menu.item_image_menu_options)

        val saveMenuItem = popupMenu.menu.findItem(R.id.action_save)
        saveMenuItem.title = if (data.isSaved) "Unsave" else "Save"

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_save -> {
                    data.isSaved = !data.isSaved

                    Snackbar.make(view, "Post Saved Successfully.", Snackbar.LENGTH_SHORT).show()

                    saveMenuItem.title = if (data.isSaved) "Unsave" else "Save"
                    true
                }

                R.id.action_open -> {
                    onItemClickListener?.let { it(data) }
                    true
                }

                R.id.action_share -> {
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.type = "text/plain"
                    intent.putExtra(
                        Intent.EXTRA_TEXT,
                        "${data.link}\nI found this intesting post in Imgruram."
                    )
                    context.startActivity(intent)
                    true
                }

                else -> false
            }
        }
        popupMenu.show()
    }
}