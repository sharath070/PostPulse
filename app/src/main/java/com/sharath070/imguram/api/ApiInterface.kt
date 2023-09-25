package com.sharath070.imguram.api

import com.sharath070.imguram.model.galleryTags.GalleryTagsResponse
import com.sharath070.imguram.utils.Constants.Companion.CLIENT_ID
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
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