package org.ovi.feature.auth.domain

import org.ovi.feature.auth.model.*
import org.ovi.feature.forgetpassword.model.ForgetRequest
import org.ovi.feature.forgetpassword.model.ResetRequest
import retrofit2.Response

interface IAuthRepo {

    suspend fun login(request: LoginRequest): LoginResponse

    suspend fun forget(forgetRequest: ForgetRequest):LoginResponse

    suspend fun reset(resetRequest: ResetRequest):Response<Any?>

    suspend fun changePass(request: ChangePasswordRequest): ChangePasswordResponse
    suspend fun getOtp(request: GetMobileOtpRequest): LoginResponse
    suspend fun verifyOtp(request: VerifyOtpRequest): LoginResponse
    suspend fun delete(): LoginResponse

}