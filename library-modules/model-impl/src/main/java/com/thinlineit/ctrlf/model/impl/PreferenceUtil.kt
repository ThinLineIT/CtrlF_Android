package com.thinlineit.ctrlf.model.impl

import android.content.Context
import android.content.SharedPreferences

class PreferenceUtil(context: Context, name: String) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(name, 0)

    fun getString(key: String, defValue: String? = null): String? {
        return sharedPreferences.getString(key, defValue)
    }

    fun setString(key: String, str: String?) {
        sharedPreferences.edit().putString(key, str).apply()
    }
}
