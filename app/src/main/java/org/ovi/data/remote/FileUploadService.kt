package org.ovi.data.remote

import okhttp3.RequestBody
import org.ovi.feature.profile.model.UploadImageReguest
import org.ovi.feature.profile.model.UploadImageResponse
import retrofit2.http.*

interface FileUploadService {

    @POST("master-data/assets/upload-url")
    suspend fun getPreSignedUrl(
        @Body fileUploadRequest: UploadImageReguest
    ): UploadImageResponse

    @PUT
    suspend fun uploadFileToAws(
        @Header("Content-Type") contentType: String,
        @Url uploadUrl: String,
        @Body file: RequestBody
    ): retrofit2.Response<Unit>

}