package com.thinlineit.ctrlf.util.uri

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns

const val DEFAULT = "default"

fun getName(context: Context, uri: Uri): String {
    context.contentResolver.query(uri, null, null, null, null)?.let { cursor ->
        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        cursor.moveToFirst()
        val fileName = cursor.getString(nameIndex).split(".")[0]
        cursor.close()
        return fileName
    }
    return DEFAULT
}
