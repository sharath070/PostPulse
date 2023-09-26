package com.sharath070.postpulse.db

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sharath070.postpulse.model.galleryTags.Image


class ImageListConverter {

    @TypeConverter
    fun fromJson(json: String?): List<Image>? {
        if (json == null) {
            return null
        }

        val listType = object : TypeToken<List<Image>>() {}.type
        return Gson().fromJson(json, listType)
    }

    @TypeConverter
    fun toJson(images: List<Image>?): String? {
        if (images == null) {
            return null
        }

        return Gson().toJson(images)
    }

}

class AnyTypeConverter {
    @TypeConverter
    fun fromJson(json: String?): Any? {
        if (json == null) {
            return null
        }

        return Gson().fromJson(json, Any::class.java)
    }

    @TypeConverter
    fun toJson(data: Any?): String? {
        if (data == null) {
            return null
        }

        return Gson().toJson(data)
    }
}

