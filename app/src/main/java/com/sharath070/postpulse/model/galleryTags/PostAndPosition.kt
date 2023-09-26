package com.sharath070.postpulse.model.galleryTags

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class PostAndPosition(
    val position: Int,
    val post: List<Image>
): Serializable
