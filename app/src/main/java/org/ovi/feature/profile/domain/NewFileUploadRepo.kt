package org.ovi.feature.profile.domain

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.ovi.base.BaseResponse
import org.ovi.data.remote.NewFileUploadService
import org.ovi.feature.profile.model.UploadImageReguest
import org.ovi.util.extensions.getMimeTypeByExt
import java.io.File
import java.net.URLConnection

class NewFileUploadRepo(private val fileFileUploadService: NewFileUploadService) :
    INewFileUploadRepo {


    override suspend fun uploadImage(filePath: String?): BaseResponse {
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
                    uploadUrl = preSignedResponse.data?.url.toString(),
                    key = prepareRequestbody(preSignedResponse.data?.fields?.key.toString()),
                    acl= prepareRequestbody(preSignedResponse.data?.fields?.acl.toString()),
                    contentType=prepareRequestbody(preSignedResponse.data?.fields?.contentType.toString()),
                    signatureVersion=prepareRequestbody(preSignedResponse.data?.fields?.signatureVersion.toString()),
                    bucket=prepareRequestbody(preSignedResponse.data?.fields?.bucket.toString()),
                    algorithm=prepareRequestbody(preSignedResponse.data?.fields?.xAmzAlgorithm.toString()),
                    credential=prepareRequestbody(preSignedResponse.data?.fields?.xAmzCredential.toString()),
                    date=prepareRequestbody(preSignedResponse.data?.fields?.xAmzDate.toString()),
                    token=prepareRequestbody(preSignedResponse.data?.fields?.xAmzSecurityToken.toString()),
                    policy=prepareRequestbody(preSignedResponse.data?.fields?.policy.toString()),
                    signature=prepareRequestbody(preSignedResponse.data?.fields?.xAmzSignature.toString()),
                    file.asRequestBody(mimeType.toMediaTypeOrNull())
//                    prepareRequestbody(file.path)
//                    createFilePart(file)
//                    "https://slf-ovi-backend-dev-use1.s3.amazonaws.com/images/image_1656048702038.jpg?Content-Type=image%2Fjpeg&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIA52DCX3G25NKFU6P7%2F20220624%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20220624T053142Z&X-Amz-Expires=1800&X-Amz-Signature=44e0be840063d752c87614456977bbd216f2b6bf3c7672c7cd6280ba138cf2ff&X-Amz-SignedHeaders=host%3Bx-amz-acl&x-amz-acl=public-read",
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

    private fun createFilePart(file: File):MultipartBody.Part{
        val mimeType = URLConnection.guessContentTypeFromName(file.name)
        return MultipartBody.Part.createFormData("file", file.name,
            file.asRequestBody(mimeType.toMediaTypeOrNull())
        )
    }

     private fun prepareRequestbody(text: String): RequestBody {
        return text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
    }


}