package com.keresman.flatearth.handler

import android.content.Context
import android.util.Log
import com.keresman.flatearth.factory.createHttpUtlConnection
import com.keresman.flatearth.utils.createFile
import java.io.File
import java.net.HttpURLConnection
import java.nio.file.Files

fun downloadImage(context: Context, url: String) : String? {
    val filename = url.substring(url.lastIndexOf("/") + 1)

    var file: File = createFile(context, filename)

    try {
        val connection: HttpURLConnection = createHttpUtlConnection(url)
        Files.copy(connection.inputStream, file.toPath())
        return file.absolutePath
    } catch (e: Exception) {
        Log.e("ERROR", e.toString(), e)
    }

    return null
}
