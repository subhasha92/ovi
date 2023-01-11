package org.ovi.feature.profile.domain

import org.ovi.data.pref.OVIPreferences
import org.ovi.data.remote.ProfileServices
import org.ovi.feature.profile.model.AgencyResponse
import org.ovi.feature.profile.model.EditProfileRequest
import org.ovi.feature.profile.model.GetProfileResponse
import org.ovi.feature.register.model.RegisterResponse

class ProfileRepo(
    private val profileServices: ProfileServices,
    private val oviPref: OVIPreferences
) : IProfileRepo {


    override suspend fun getProfile(): GetProfileResponse {
        return oviPref.id?.toInt()?.let { profileServices.getProfile(it) }!!
    }

    override suspend fun putProfile(request: EditProfileRequest): RegisterResponse {
        val response = oviPref.id?.toInt()?.let { profileServices.putProfile(it, request) }!!
        if (response.statusCode == 201) {
            oviPref.imageUrl = response.data?.imageUrl
        }

        return response
    }

    override suspend fun getAgency(): AgencyResponse {
        return profileServices.getAgency()
    }


}