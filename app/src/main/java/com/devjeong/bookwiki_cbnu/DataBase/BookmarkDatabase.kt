package com.devjeong.bookwiki_cbnu.DataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.devjeong.bookwiki_cbnu.DAO.BookmarkDao
import com.devjeong.bookwiki_cbnu.Model.Bookmark

@Database(entities = [Bookmark::class], version =  1)
abstract class BookmarkDatabase : RoomDatabase(){
    abstract fun bookmarkDao(): BookmarkDao

    companion object{
        @Volatile
        private var INSTANCE: BookmarkDatabase? = null

        fun getDataBase(context: Context): BookmarkDatabase{
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BookmarkDatabase::class.java,
                    "bookmark_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}