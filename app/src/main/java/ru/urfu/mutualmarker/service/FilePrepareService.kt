package ru.urfu.mutualmarker.service

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.MimeTypeMap
import android.widget.Toast
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.*


class FilePrepareService {

    fun openFile(url: Uri, context: Context) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        //if you want you can also define the intent type for any other file
        //additionally use else clause below, to manage other unknown extensions
        //in this case, Android will show all applications installed on the device
        //so you can choose which application to use
        intent.setDataAndType(url, "*/*")

        try {
            val file = fileFromContentUri(context, url)
            if (file.exists()) context.startActivity(
                Intent.createChooser(
                    intent,
                    "Open"
                )

            ) else Toast.makeText(context, "File is corrupted", Toast.LENGTH_LONG).show()
        } catch (_: Exception) {
            Toast.makeText(context, "Error", Toast.LENGTH_LONG).show()
        }
    }

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

    fun fileFromContentUri(context: Context, contentUri: Uri): File {
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