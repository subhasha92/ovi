package org.ovi.data.remote

import org.ovi.feature.auth.model.*
import org.ovi.feature.forgetpassword.model.ForgetRequest
import org.ovi.feature.forgetpassword.model.ResetRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthService {

    @POST("auth/login")
    suspend fun login(@Body reqeust: LoginRequest): LoginResponse

    @POST("auth/forgot-password")
    suspend fun forgetPassword(@Body forgetRequest: ForgetRequest): LoginResponse

    @POST("auth/reset-password")
    suspend fun resetPassword(@Body resetRequest: ResetRequest): Response<Any?>

    @POST("auth/change-password")
    suspend fun changePass(@Body request: ChangePasswordRequest): ChangePasswordResponse

    @POST("auth/get-otp")
    suspend fun getOtp(@Body request: GetMobileOtpRequest): LoginResponse

    @POST("auth/verify-otp")
    suspend fun verifyOtp(@Body request: VerifyOtpRequest): LoginResponse

    @DELETE("auth/account/{user_id}")
    suspend fun deleteAccount(@Path("user_id") user_id: Int): LoginResponse

}