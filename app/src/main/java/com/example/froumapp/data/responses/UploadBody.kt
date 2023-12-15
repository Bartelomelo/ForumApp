package com.example.froumapp.data.responses

import android.graphics.Bitmap
import java.io.File


data class UploadBody(
    val username: String,
    val filename: String,
    val file: File
)
