package com.thinlineit.ctrlf.util

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import java.io.File
import java.io.IOException

fun copyUri(context: Context, uri: Uri, fileName: String, mimeType: String): Uri {
    val mimeTypeMap = MimeTypeMap.getSingleton()
    val extension = mimeTypeMap.getExtensionFromMimeType(mimeType)
    val currentTime = System.currentTimeMillis()
    val file = File.createTempFile(fileName + currentTime, ".$extension", context.cacheDir)

    return try {
        file.outputStream().use {
            context.contentResolver.openInputStream(uri)?.copyTo(it)
        }
        Uri.fromFile(file)
    } catch (e: IOException) {
        uri
    }
}
