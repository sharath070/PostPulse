package com.sharath070.postpulse.api

import com.sharath070.postpulse.model.galleryTags.GalleryTagsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @GET("gallery/hot/{sort}/{page}")
    @Headers("Cache-Control: max-age=60")
    suspend fun getHotPost(
        @Path("sort") sort: String,
        @Path("page") page: Int,
        @Query("showViral") showViral: Boolean = false
    ): Response<GalleryTagsResponse>



    @GET("gallery/top/{sort}/{page}")
    @Headers("Cache-Control: max-age=60")
    suspend fun getTopPost(
        @Path("sort") sort: String,
        @Path("page") page: Int,
        @Query("showViral") showViral: Boolean = false
    ): Response<GalleryTagsResponse>

}