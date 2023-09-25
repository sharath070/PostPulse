package com.sharath070.imguram.model.galleryTags

import java.io.Serializable

data class GalleryTagsResponse(
    val `data`: List<Data>?,
    val status: Int?,
    val success: Boolean?
): Serializable