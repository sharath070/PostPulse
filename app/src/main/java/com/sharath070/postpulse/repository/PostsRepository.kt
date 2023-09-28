package com.sharath070.postpulse.repository

import com.sharath070.postpulse.api.RetrofitInstance
import com.sharath070.postpulse.db.PostDatabase
import com.sharath070.postpulse.model.galleryTags.Data

class PostsRepository(private val db: PostDatabase) {

    suspend fun getHotPosts(page: Int, showViral: Boolean) =
        RetrofitInstance.api.getHotPost(page, showViral)

    suspend fun getTopPosts(page: Int, showViral: Boolean) =
        RetrofitInstance.api.getTopPost(page, showViral)


    fun upsert(postData: Data) = db.getArticleDao().upsert(postData)

    fun getSavedNews() = db.getArticleDao().getAllPosts()

    fun deleteArticle(postData: Data) = db.getArticleDao().deleteArticle(postData)


}