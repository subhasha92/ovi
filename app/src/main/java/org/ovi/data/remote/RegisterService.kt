package org.ovi.data.remote

import org.ovi.feature.register.model.*
import retrofit2.Response
import retrofit2.http.*

interface RegisterService {

    @GET("onboarding-question")
    suspend fun getOnBoarding(): OnBoardinResponse

    @POST("onboarding-question/onboarding_questions")
    suspend fun putOnBoardingRes(@Body putOnBoardRequest: PutOnBoardingResRequest): PutOnBoardResponse

    @POST("member-user")
    suspend fun registerUser(@Body registerRequest: RegisterRequest): RegisterResponse

    @GET("master-data/{type}")
    suspend fun getLocationData(
        @Path("type") type: String,
        @Query("zipcode") zipcode: String?
    ): LocationResponse


}