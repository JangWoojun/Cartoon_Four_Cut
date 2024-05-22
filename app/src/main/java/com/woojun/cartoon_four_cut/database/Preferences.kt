package com.woojun.cartoon_four_cut.database

import android.content.Context
import android.content.SharedPreferences

object Preferences {
    fun saveId(context: Context, id: String) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("id", id)
        editor.apply()
    }

    fun loadId(context: Context): String? {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("id", null)
    }

}