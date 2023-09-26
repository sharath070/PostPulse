package com.sharath070.postpulse.repository

import com.sharath070.postpulse.api.RetrofitInstance
import com.sharath070.postpulse.db.PostDatabase
import com.sharath070.postpulse.model.galleryTags.Data

class PostsRepository(private val db: PostDatabase) {

    suspend fun getHotPosts() = RetrofitInstance.api.getHotPost()
    suspend fun getTopPosts() = RetrofitInstance.api.getTopPost()


    suspend fun upsert(postData: Data) = db.getArticleDao().upsert(postData)

    fun getSavedNews() = db.getArticleDao().getAllPosts()

    suspend fun deleteArticle(postData: Data) = db.getArticleDao().deleteArticle(postData)


}