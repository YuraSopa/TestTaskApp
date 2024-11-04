package com.yurasopa.testtaskapp.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

fun Uri.toFile(context: Context): File {
    val inputStream: InputStream? = context.contentResolver.openInputStream(this)
    val tempFile = File.createTempFile("image", ".jpg", context.cacheDir)
    tempFile.deleteOnExit()
    inputStream?.use { input ->
        FileOutputStream(tempFile).use { output ->
            input.copyTo(output)
        }
    }
    return tempFile
}