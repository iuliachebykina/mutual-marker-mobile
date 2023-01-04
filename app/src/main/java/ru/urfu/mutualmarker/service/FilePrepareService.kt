package ru.urfu.mutualmarker.service

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.*


class FilePrepareService {

    fun prepareFile(
        uris: List<Uri>,
        context: Context?,
    ): MutableList<MultipartBody.Part> {
        val files: MutableList<MultipartBody.Part> = ArrayList()
        for (uri in uris) {
            val file = fileFromContentUri(context!!, uri)

            val requestFile =
                file.let { RequestBody.create(MediaType.parse("multipart/form-data"), it) }
            files.add(
                requestFile.let {
                    MultipartBody.Part.createFormData(
                        "files", file.name,
                        it
                    )
                })

        }
        return files


    }

    private fun getFileExtension(context: Context, uri: Uri): String? {
        val fileType: String? = context.contentResolver.getType(uri)
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(fileType)
    }

    @Throws(IOException::class)
    private fun copy(source: InputStream, target: OutputStream) {
        val buf = ByteArray(8192)
        var length: Int
        while (source.read(buf).also { length = it } > 0) {
            target.write(buf, 0, length)
        }
    }

    private fun fileFromContentUri(context: Context, contentUri: Uri): File {
        // Preparing Temp file name
        val fileExtension = getFileExtension(context, contentUri)
        val fileName = "temp_file" + if (fileExtension != null) ".$fileExtension" else ""

        // Creating Temp file
        val tempFile = File(context.cacheDir, fileName)
        tempFile.createNewFile()

        try {
            val oStream = FileOutputStream(tempFile)
            val inputStream = context.contentResolver.openInputStream(contentUri)

            inputStream?.let {
                copy(inputStream, oStream)
            }

            oStream.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return tempFile
    }


}