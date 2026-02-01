package com.keresman.flatearth.utils

import android.content.Context
import java.io.File

fun createFile(context: Context, filename: String): File {
    //storage/../com.keresman.flatearth
    val dir = context.getExternalFilesDir(null)

    val file = File(dir, filename)

    if (file.exists())
        file.delete()

    return file
}
