package com.thinlineit.ctrlf.util

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import com.thinlineit.ctrlf.util.uri.getName
import java.io.File
import java.io.IOException

fun copyUri(context: Context, uri: Uri, mimeType: String): Uri {
    val mimeTypeMap = MimeTypeMap.getSingleton()
    val extension = mimeTypeMap.getExtensionFromMimeType(mimeType)
    val fileName = getName(context, uri)
    val file = File.createTempFile(fileName, ".$extension", context.cacheDir)
    return try {
        file.outputStream().use {
            context.contentResolver.openInputStream(uri)?.copyTo(it)
        }
        Uri.fromFile(file)
    } catch (e: IOException) {
        uri
    }
}
