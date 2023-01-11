package org.ovi.feature.register.domain

import android.content.ContentValues.TAG
import android.util.Log
import org.ovi.data.pref.OVIPreferences
import org.ovi.data.remote.RegisterService
import org.ovi.feature.register.model.*
import retrofit2.Response

class RegisterRepo(
    private val registerService: RegisterService,
    private val oviPref: OVIPreferences
) : IRegisterRepo {

    override suspend fun registerUser(registerRequest: RegisterRequest): RegisterResponse {
        val response = registerService.registerUser(registerRequest)
        if (response.statusCode == 201) {
            oviPref.email = response.data?.emails?.get(0).toString()
            oviPref.id = response.data?.id.toString()
            oviPref.token = response.data?.token.toString()
//            Log.e(TAG, "registerUser: ${oviPref.token}", )
//            oviPref.token=response.data?..toString()
            oviPref.isLoggedIn = true
        }
        return response
    }

    override suspend fun getOnBoarding(): OnBoardinResponse {
        return registerService.getOnBoarding()
    }

    override suspend fun putOnBoard(putOnBoardRequest: PutOnBoardingResRequest): PutOnBoardResponse {
        return registerService.putOnBoardingRes(putOnBoardRequest)
    }

    override suspend fun getLocation(zipcode: String?): LocationResponse {
        return registerService.getLocationData(MasterDataType.LOCATIONS.value, zipcode)
    }
}