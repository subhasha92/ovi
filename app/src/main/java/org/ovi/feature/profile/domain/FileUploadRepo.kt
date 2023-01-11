package org.ovi.feature.profile.domain


import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.ovi.base.BaseResponse
import org.ovi.data.remote.FileUploadService
import org.ovi.feature.profile.model.UploadImageReguest
import org.ovi.util.extensions.getMimeTypeByExt
import java.io.File
import java.net.URLConnection

class FileUploadRepo(private val fileFileUploadService: FileUploadService) : IFileUploadRepo {

    override suspend fun uploadFile(filePath: String?): BaseResponse {
        kotlin.runCatching {
            val fileName =
                (filePath?.lastIndexOf("/")?.plus(1)?.let { filePath.substring(it) }).toString()
            val preSignedResponse = fileFileUploadService.getPreSignedUrl(
                UploadImageReguest(fileName)
            )
            if (preSignedResponse.statusCode == 201) {
                var mimeType = URLConnection.guessContentTypeFromName(fileName)
                val file = File(filePath!!)
                if (mimeType.isNullOrEmpty())
                    mimeType = getMimeTypeByExt(fileName)
                fileFileUploadService.uploadFileToAws(
                    mimeType,
                    preSignedResponse.data?.preSignedUrl.toString(),
                    file.asRequestBody(mimeType.toMediaTypeOrNull())
                )
                preSignedResponse.data?.getUrl
            } else null
        }.onSuccess {
            return BaseResponse(if (it.isNullOrEmpty()) 0 else 1, it.toString())
        }.onFailure {
            return BaseResponse(0, it.message.toString())
        }
        return BaseResponse(0, "Error uploading file")
    }

    private fun prepareFilePart(filePath: String): RequestBody {
        val file = File(filePath)
        val mimeType = URLConnection.guessContentTypeFromName(file.name)
        return file.asRequestBody(mimeType.toMediaTypeOrNull())
    }

}