package com.sharath070.imguram.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.sharath070.imguram.api.ApiInterface
import com.sharath070.imguram.api.RetrofitInstance
import com.sharath070.imguram.model.galleryTags.GalleryTagsResponse

class PostsRepository() {

    suspend fun getHotPosts() = RetrofitInstance.api.getHotPost()
    suspend fun getTopPosts() = RetrofitInstance.api.getTopPost()
}