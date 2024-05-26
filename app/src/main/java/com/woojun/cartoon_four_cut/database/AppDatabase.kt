package com.woojun.cartoon_four_cut.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.woojun.cartoon_four_cut.data.HomePhotoFrame

@TypeConverters(
    TypeConverter::class
)

@Database(entities = [HomePhotoFrame::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun homePhotoFrameItemDao(): HomePhotoFrameDAO

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app database")
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}