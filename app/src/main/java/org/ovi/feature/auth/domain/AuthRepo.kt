package org.ovi.feature.auth.domain

import org.ovi.data.pref.OVIPreferences
import org.ovi.data.remote.AuthService
import org.ovi.feature.auth.model.*
import org.ovi.feature.forgetpassword.model.ForgetRequest
import org.ovi.feature.forgetpassword.model.ResetRequest
import retrofit2.Response

class AuthRepo(private val authService: AuthService,private val oviPref:OVIPreferences):IAuthRepo {

    override suspend fun login(request: LoginRequest): LoginResponse {
        val response = authService.login(request)
        if(response.statusCode == 201){
            if (response.data?.role!="admin") {
                oviPref.email = response.data?.emails?.get(0).toString()
                oviPref.token = response.data?.token
                oviPref.isLoggedIn = true
                oviPref.id = response.data?.id.toString()
                oviPref.showOnBoard = response.data?.role?.equals("youth_or_young_adult") == true
                oviPref.memberUserID = response.data?.memberUser?.id.toString()
                oviPref.imageUrl=response.data?.imageUrl
            }
        }
        return response
    }

    override suspend fun forget(forgetRequest: ForgetRequest): LoginResponse {
        return authService.forgetPassword(forgetRequest)
    }

    override suspend fun reset(resetRequest: ResetRequest): Response<Any?> {
        return authService.resetPassword(resetRequest)
    }

    override suspend fun changePass(request: ChangePasswordRequest): ChangePasswordResponse {
        return authService.changePass(request)
    }

    override suspend fun getOtp(request: GetMobileOtpRequest): LoginResponse {
        return authService.getOtp(request)
    }

    override suspend fun verifyOtp(request: VerifyOtpRequest): LoginResponse {
       val response= authService.verifyOtp(request)
        if(response.statusCode == 201){
            oviPref.email = response.data?.emails?.get(0).toString()
            oviPref.token = response.data?.token
            oviPref.isLoggedIn = true
            oviPref.id=response.data?.id.toString()
            oviPref.showOnBoard= response.data?.role?.equals("youth_or_young_adult") == true
            oviPref.memberUserID=response.data?.memberUser?.id.toString()
        }
        return response
    }

    override suspend fun delete(): LoginResponse {
        val response=authService.deleteAccount(oviPref.id?.toInt()!!)
        if (response.statusCode==201){
            oviPref.logOut()
        }
        return response
    }
}