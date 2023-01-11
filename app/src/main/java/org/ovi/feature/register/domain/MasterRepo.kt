package org.ovi.feature.register.domain

import org.ovi.data.pref.OVIPreferences
import org.ovi.data.remote.ProfileServices
import org.ovi.feature.profile.model.CountiesResponse
import org.ovi.feature.profile.model.GroupsResponse
import org.ovi.feature.register.model.LocationResponse
import org.ovi.feature.register.model.MasterDataResponse
import org.ovi.feature.register.model.MasterDataType
import org.ovi.feature.register.model.ZipcodeResponse

class MasterRepo(
    private val profileServices: ProfileServices,
    private val oviPref: OVIPreferences
) : IMasterRepo {

    override suspend fun getRace(): MasterDataResponse {
        return profileServices.getMasterData(MasterDataType.RACE.value)
    }

    override suspend fun getEthnicity(): MasterDataResponse {
        return profileServices.getMasterData(MasterDataType.ETHNICITY.value)
    }

    override suspend fun getMasterOfType(type: String): MasterDataResponse {
        return profileServices.getMasterData(type)
    }

    override suspend fun getLocation(zipcode:String?): LocationResponse {
        return profileServices.getLocationData(MasterDataType.LOCATIONS.value,zipcode)
    }

    override suspend fun getZipCodes(zipcode: String?): ZipcodeResponse {
        return profileServices.getZipCodes(zipcode)
    }

    override suspend fun getCounties(page: Int?, pageSize: Int?, search: String?): ZipcodeResponse {
        return profileServices.getCounties(page,pageSize,search)
    }

    override suspend fun getGroups(): GroupsResponse {
        return profileServices.getGroups()
    }
}