package ru.urfu.mutualmarker.service

import android.content.Context
import android.os.Environment
import android.util.Log
import android.widget.Toast
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.urfu.mutualmarker.client.AttachmentService
import java.io.FileOutputStream
import java.io.InputStream


class AttachmentDownloadService {

    fun downloadFile(fileName: String, attachmentService: AttachmentService, context: Context?){
        attachmentService.downloadAttachment(fileName).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                println(response)
                if (response.code() == 200 && response.body() != null) {
                    val filePath = saveFile(
                        response.body(),
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + fileName
                    )
                    if(filePath == ""){
                        Toast.makeText(context, "Не удалось скачать работу. Попробуйте позже", Toast.LENGTH_LONG).show()

                    } else {
                        Toast.makeText(context, "Работа сохранилась в загрузках", Toast.LENGTH_SHORT).show()

                    }
                } else {
                    Toast.makeText(context, "Не удалось скачать работу. Попробуйте позже", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    fun saveFile(body: ResponseBody?, pathWhereYouWantToSaveFile: String):String{
        if (body==null)
            return ""
        var input: InputStream? = null
        try {
            input = body.byteStream()
            //val file = File(getCacheDir(), "cacheFileAppeal.srl")
            val fos = FileOutputStream(pathWhereYouWantToSaveFile)
            fos.use { output ->
                val buffer = ByteArray(4 * 1024) // or other buffer size
                var read: Int
                while (input.read(buffer).also { read = it } != -1) {
                    output.write(buffer, 0, read)
                }
                output.flush()
            }

            return pathWhereYouWantToSaveFile
        }catch (e:Exception){
            Log.e("saveFile",e.toString())
        }
        finally {
            input?.close()
        }
        return ""
    }

}