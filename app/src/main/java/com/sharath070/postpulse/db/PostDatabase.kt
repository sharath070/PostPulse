package com.sharath070.postpulse.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sharath070.postpulse.model.galleryTags.Data
import com.sharath070.postpulse.model.galleryTags.Image

@Database(entities = [Data::class], version = 1)
@TypeConverters(
    ImageListConverter::class,
    AnyTypeConverter::class,
    AnyTypeConverter::class,
    AnyTypeConverter::class,
    AnyTypeConverter::class,
    AnyTypeConverter::class
    )
abstract class PostDatabase: RoomDatabase() {

    abstract fun getArticleDao(): PostDao

    companion object{
        @Volatile
        private var INSTANCE: PostDatabase? = null
        fun getDatabase(context: Context): PostDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PostDatabase::class.java,
                    "post_database"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

}