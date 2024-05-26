package com.woojun.cartoon_four_cut.database

import androidx.room.*
import com.woojun.cartoon_four_cut.data.HomePhotoFrame

@Dao
interface HomePhotoFrameDAO {
    @Insert
    fun insertHomePhotoFrame(homePhotoFrame: HomePhotoFrame)

    @Query("SELECT * FROM HomePhotoFrame")
    fun getHomePhotoFrameList(): MutableList<HomePhotoFrame>

    @Delete
    fun deleteHomePhotoFrame(homePhotoFrame: HomePhotoFrame)
}