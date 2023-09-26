package com.sharath070.postpulse.api

import com.sharath070.postpulse.model.galleryTags.GalleryTagsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiInterface {

    @GET("gallery/hot")
    @Headers("Cache-Control: max-age=60")
    suspend fun getHotPost(
        @Query("showViral") showViral: Boolean = true,
        @Query("album_previews") albumPreviews: Boolean = true
    ): Response<GalleryTagsResponse>

    @GET("gallery/top")
    @Headers("Cache-Control: max-age=60")
    suspend fun getTopPost(
        @Query("showViral") showViral: Boolean = true,
        @Query("album_previews") albumPreviews: Boolean = true
    ): Response<GalleryTagsResponse>

}