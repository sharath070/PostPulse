package com.sharath070.imguram.ui.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sharath070.imguram.model.galleryTags.GalleryTagsResponse
import com.sharath070.imguram.repository.PostsRepository
import com.sharath070.imguram.repository.Resource
import com.sharath070.imguram.ui.networkManager.NetworkManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class PostsViewModel(private val context: Context, private val postsRepository: PostsRepository) :
    ViewModel() {

    val networkManager = NetworkManager(context)

    init {
        networkManager.observeForever {
            if (it == true){
                getHotPosts()
            }
        }
    }


    private val _hotPosts: MutableLiveData<Resource<GalleryTagsResponse>> = MutableLiveData()
    val hotPosts: LiveData<Resource<GalleryTagsResponse>> get() = _hotPosts

    private fun getHotPosts() = viewModelScope.launch(Dispatchers.IO) {
        _hotPosts.postValue(Resource.Loading())
        val response = postsRepository.getHotPosts()
        _hotPosts.postValue(handleHotPosts(response))
    }

    private val _topPosts: MutableLiveData<Resource<GalleryTagsResponse>> = MutableLiveData()
    val topPosts: LiveData<Resource<GalleryTagsResponse>> get() = _topPosts

    fun getTopPosts() = viewModelScope.launch(Dispatchers.IO) {
        _topPosts.postValue(Resource.Loading())
        val response = postsRepository.getTopPosts()
        _topPosts.postValue(handleTopPosts(response))
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