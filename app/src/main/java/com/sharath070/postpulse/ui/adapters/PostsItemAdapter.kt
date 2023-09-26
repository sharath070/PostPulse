package com.sharath070.postpulse.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.sharath070.postpulse.R
import com.sharath070.postpulse.databinding.ItemImageCardBinding
import com.sharath070.postpulse.model.galleryTags.Data
import com.sharath070.postpulse.model.galleryTags.Image
import com.sharath070.postpulse.ui.viewModel.PostsViewModel

class PostsItemAdapter(private val context: Context, private val viewModel: PostsViewModel) :
    ListAdapter<Data, PostsItemAdapter.ViewHolder>(GalleryDiffUtil()) {

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
        val post = getItem(position)
        val imagesList = post.images ?: listOf()



        holder.itemBinding.ibOption.setOnClickListener { view ->
            showPopupMenu(view, post)
        }


        holder.itemBinding.apply {
            tvCaption.text = caption
            holder.itemBinding.ivId.text = userName

            val viewPagerAdapter = ViewPagerAdapter(context, imagesList) { position, post ->
                onPostClickListener?.invoke(position, post)
            }

            imgLayout.viewPager.adapter = viewPagerAdapter
            imgLayout.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            if (imagesList.size == 1) {
                imgLayout.circleIndicator.visibility = View.GONE
            }
            imgLayout.circleIndicator.setViewPager(imgLayout.viewPager)


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


    private var onItemClickListener: ((Data) -> Unit)? = null
    fun setOnItemClickListener(listener: (Data) -> Unit) {
        onItemClickListener = listener
    }

    private var onPostClickListener: ((position: Int, data: List<Image>) -> Unit)? = null
    fun setOnPostClickListener(listener: (position: Int, data: List<Image>) -> Unit) {
        onPostClickListener = listener
    }

    private fun showPopupMenu(view: View, post: Data) {
        val popupMenu = PopupMenu(context, view)
        popupMenu.inflate(R.menu.item_image_menu_options)

        val saveMenuItem = popupMenu.menu.findItem(R.id.action_save)
        saveMenuItem.title = if (post.isSaved) "Unsave" else "Save"

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_save -> {

                    if (!post.isSaved){
                        post.isSaved = true
                        viewModel.savePost(post)
                        Snackbar.make(view, "Post Saved Successfully.", Snackbar.LENGTH_SHORT).show()
                    }
                    else{
                        post.isSaved = false
                        viewModel.deletePost(post)
                        Snackbar.make(view, "Successfully deleted article", Snackbar.LENGTH_LONG).apply {
                            setAction("Undo") {
                                viewModel.savePost(post)
                            }
                            show()
                        }
                    }

                    saveMenuItem.title = if (post.isSaved) "Unsave" else "Save"
                    true
                }

                R.id.action_open -> {
                    onItemClickListener?.let { it(post) }
                    true
                }

                R.id.action_share -> {
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.type = "text/plain"
                    intent.putExtra(
                        Intent.EXTRA_TEXT,
                        "${post.link}\nI found this intesting post in Imgruram."
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