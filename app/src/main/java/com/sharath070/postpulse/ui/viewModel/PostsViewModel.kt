package com.sharath070.postpulse.ui.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sharath070.postpulse.model.galleryTags.Data
import com.sharath070.postpulse.model.galleryTags.GalleryTagsResponse
import com.sharath070.postpulse.repository.PostsRepository
import com.sharath070.postpulse.repository.Resource
import com.sharath070.postpulse.ui.networkManager.NetworkManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class PostsViewModel(private val context: Context, private val postsRepository: PostsRepository) :
    ViewModel() {

    val networkManager = NetworkManager(context)

    init {
        networkManager.observeForever {
            if (it == true){
                getHotPosts("viral")
            }
        }
    }


    private val _hotPosts: MutableLiveData<Resource<GalleryTagsResponse>> = MutableLiveData()
    val hotPosts: LiveData<Resource<GalleryTagsResponse>> get() = _hotPosts

    private val _topPosts: MutableLiveData<Resource<GalleryTagsResponse>> = MutableLiveData()
    val topPosts: LiveData<Resource<GalleryTagsResponse>> get() = _topPosts


    fun getHotPosts(filter: String) = viewModelScope.launch(Dispatchers.IO) {
        _hotPosts.postValue(Resource.Loading())
        val response = postsRepository.getHotPosts(filter, 1)
        _hotPosts.postValue(handleHotPosts(response))
    }


    fun getTopPosts() = viewModelScope.launch(Dispatchers.IO) {
        _topPosts.postValue(Resource.Loading())
        val response = postsRepository.getTopPosts("viral", 1)
        _topPosts.postValue(handleTopPosts(response))
    }


    fun savePost(post: Data) = viewModelScope.launch(Dispatchers.IO) {
        postsRepository.upsert(post)
    }

    fun getSavedPosts() = postsRepository.getSavedNews()

    fun deletePost(post: Data) = viewModelScope.launch(Dispatchers.IO) {
        postsRepository.deleteArticle(post)
    }





    private fun handleHotPosts(response: Response<GalleryTagsResponse>) : Resource<GalleryTagsResponse>{
        if (response.isSuccessful && response.body() != null){
            return Resource.Success(response.body())
        }
        return Resource.Error(response.message())
    }

    private fun handleTopPosts(response: Response<GalleryTagsResponse>) : Resource<GalleryTagsResponse>{
        if (response.isSuccessful && response.body() != null){
            return Resource.Success(response.body())
        }
        return Resource.Error(response.message())
    }



}