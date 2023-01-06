package ru.urfu.mutualmarker.dto

import android.net.Uri

data class Attachment(
    val uri: Uri?,
    var filename: String?
)
