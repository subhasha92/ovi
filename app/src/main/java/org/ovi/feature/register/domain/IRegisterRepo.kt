package org.ovi.feature.register.domain

import org.ovi.feature.register.model.*
import retrofit2.Response

interface IRegisterRepo {

    suspend fun registerUser(registerRequest: RegisterRequest): RegisterResponse

    suspend fun getOnBoarding():OnBoardinResponse

    suspend fun putOnBoard(putOnBoardRequest: PutOnBoardingResRequest): PutOnBoardResponse

    suspend fun getLocation(zipcode: String?): LocationResponse

}