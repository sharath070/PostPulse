package com.sharath070.postpulse.ui.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sharath070.postpulse.repository.PostsRepository

class PostsViewModelFactory(private val context: Context,private val postsRepository: PostsRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PostsViewModel(context,postsRepository) as T
    }
}