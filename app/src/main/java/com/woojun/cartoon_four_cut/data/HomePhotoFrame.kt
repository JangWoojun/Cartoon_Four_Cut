package com.woojun.cartoon_four_cut.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HomePhotoFrame (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val imagePath: String,
    val date: String
)