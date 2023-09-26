package com.sharath070.postpulse.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sharath070.postpulse.model.galleryTags.Data
import com.sharath070.postpulse.model.galleryTags.Image

@Dao
interface PostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(post: Data)

    @Query("SELECT * FROM post_data")
    fun getAllPosts(): LiveData<List<Data>>

    @Delete
    fun deleteArticle(post: Data)

}