package org.ovi.data.remote

import org.ovi.feature.register.model.RegisterRequest
import org.ovi.feature.register.model.ValidationResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ValidationService {

    @POST("auth/validate-email")
    suspend fun validEmail(@Body request: RegisterRequest): ValidationResponse

    @POST("auth/validate-phone")
    suspend fun validPhone(@Body request: RegisterRequest): ValidationResponse

}