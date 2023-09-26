package com.sharath070.postpulse.repository

import com.sharath070.postpulse.api.RetrofitInstance

class PostsRepository() {

    suspend fun getHotPosts() = RetrofitInstance.api.getHotPost()
    suspend fun getTopPosts() = RetrofitInstance.api.getTopPost()
}