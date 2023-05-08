package com.thinlineit.ctrlf.util

import android.content.Context
import android.content.SharedPreferences

class PreferenceUtil(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("prefs_name", 0)

    fun getString(key: String, defValue: String): String {
        return sharedPreferences.getString(key, defValue).toString()
    }

    fun setString(key: String, str: String) {
        sharedPreferences.edit().putString(key, str).apply()
    }
}
