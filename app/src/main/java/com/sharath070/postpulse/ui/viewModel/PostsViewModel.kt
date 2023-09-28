package com.sharath070.postpulse.ui.viewModel

import android.content.Context
import android.util.Log
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
                getHotPosts()
                getTopPosts()
            }
        }
    }


    private val _hotPosts: MutableLiveData<Resource<GalleryTagsResponse>> = MutableLiveData()
    val hotPosts: LiveData<Resource<GalleryTagsResponse>> get() = _hotPosts
    private var paginationHotPosts = 1
    private var hotPostsResponse: GalleryTagsResponse? = null
    var filterHotPosts = "viral"

    private val _topPosts: MutableLiveData<Resource<GalleryTagsResponse>> = MutableLiveData()
    val topPosts: LiveData<Resource<GalleryTagsResponse>> get() = _topPosts
    private var paginationTopPosts = 1
    private var topPostsResponse: GalleryTagsResponse? = null
    var filterTopPosts = "viral"

    fun getHotPosts() = viewModelScope.launch(Dispatchers.IO) {
        val sort = if (filterHotPosts == "viral") "viral" else "top"
        Log.d("@@@@", "getTopPosts: $filterHotPosts $sort")
        _hotPosts.postValue(Resource.Loading())
        val response = postsRepository.getHotPosts(sort, paginationHotPosts)
        _hotPosts.postValue(handleHotPosts(response))
    }


    fun getTopPosts() = viewModelScope.launch(Dispatchers.IO) {
        val sort = if (filterTopPosts == "viral") "viral" else "top"
        Log.d("@@@@", "getTopPosts: $filterTopPosts $sort")
        _topPosts.postValue(Resource.Loading())
        val response = postsRepository.getTopPosts(sort, paginationTopPosts)
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

            response.body()?.let {galleryResponse ->
                paginationHotPosts++
                if (hotPostsResponse == null){
                    hotPostsResponse = galleryResponse
                }
                else{
                    val oldPosts = hotPostsResponse?.data
                    val newPosts = galleryResponse.data
                    if (newPosts != null) {
                        oldPosts?.addAll(newPosts)
                    }
                }
                return Resource.Success(hotPostsResponse ?: galleryResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleTopPosts(response: Response<GalleryTagsResponse>) : Resource<GalleryTagsResponse>{
        if (response.isSuccessful && response.body() != null){

            response.body()?.let {galleryResponse ->
                paginationTopPosts++
                if (topPostsResponse == null){
                    topPostsResponse = galleryResponse
                }
                else{
                    val oldPosts = topPostsResponse?.data
                    val newPosts = galleryResponse.data
                    if (newPosts != null) {
                        oldPosts?.addAll(newPosts)
                    }
                }
                return Resource.Success(topPostsResponse ?: galleryResponse)
            }
        }
        return Resource.Error(response.message())
    }



}