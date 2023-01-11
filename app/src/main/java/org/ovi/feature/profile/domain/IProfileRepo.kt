package org.ovi.feature.profile.domain

import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.ovi.feature.profile.model.*
import org.ovi.feature.register.model.RegisterResponse
import retrofit2.Response

interface IProfileRepo {

    suspend fun getProfile(): GetProfileResponse

    suspend fun putProfile(request:EditProfileRequest):RegisterResponse

    suspend fun getAgency():AgencyResponse


}