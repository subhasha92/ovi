package org.ovi.feature.profile.domain

import org.ovi.base.BaseResponse

interface IFileUploadRepo {

    suspend fun uploadFile(filePath: String?): BaseResponse
}