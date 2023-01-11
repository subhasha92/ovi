package org.ovi.data.remote

import android.net.wifi.hotspot2.pps.Credential
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.ovi.feature.profile.model.FileUploadResponse
import org.ovi.feature.profile.model.UploadImageReguest
import org.ovi.feature.profile.model.UploadImageResponse
import retrofit2.http.*

interface NewFileUploadService {

    @POST("master-data/assets/upload-url")
    suspend fun getPreSignedUrl(
        @Body fileUploadRequest: UploadImageReguest
    ): FileUploadResponse

    @Multipart
    @POST
    suspend fun uploadFileToAws(
        @Url uploadUrl: String,
        @Part("key") key:RequestBody,
        @Part("acl") acl:RequestBody,
        @Part("Content-Type") contentType:RequestBody,
        @Part("signatureVersion") signatureVersion:RequestBody,
        @Part("bucket") bucket:RequestBody,
        @Part("X-Amz-Algorithm") algorithm:RequestBody,
        @Part("X-Amz-Credential") credential: RequestBody,
        @Part("X-Amz-Date") date: RequestBody,
        @Part("X-Amz-Security-Token") token: RequestBody,
        @Part("Policy") policy: RequestBody,
        @Part("X-Amz-Signature") signature: RequestBody,
        @Part("file") file: RequestBody
    ): retrofit2.Response<Unit>

}