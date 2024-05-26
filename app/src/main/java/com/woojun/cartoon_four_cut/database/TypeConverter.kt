package com.woojun.cartoon_four_cut.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.woojun.cartoon_four_cut.data.DownloadItem
import com.woojun.cartoon_four_cut.data.HomePhotoFrame

class TypeConverter {

    @TypeConverter
    fun fromDownloadItem(downloadItem: DownloadItem): String {
        return Gson().toJson(downloadItem)
    }

    @TypeConverter
    fun toHomeDownloadItem(downloadItemString: String):DownloadItem {
        val listType = object : TypeToken<DownloadItem>() {}.type
        return Gson().fromJson(downloadItemString, listType)
    }


}
