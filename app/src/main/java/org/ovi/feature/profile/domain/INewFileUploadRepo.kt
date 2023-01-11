package org.ovi.feature.profile.domain

import org.ovi.base.BaseResponse

interface INewFileUploadRepo {

    suspend fun uploadImage(filePath: String?):BaseResponse

}