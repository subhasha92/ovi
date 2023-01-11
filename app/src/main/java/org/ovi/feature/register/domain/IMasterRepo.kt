package org.ovi.feature.register.domain

import org.ovi.feature.profile.model.GroupsResponse
import org.ovi.feature.register.model.LocationResponse
import org.ovi.feature.register.model.MasterDataResponse
import org.ovi.feature.register.model.ZipcodeResponse

interface IMasterRepo {
    suspend fun getRace(): MasterDataResponse
    suspend fun getEthnicity(): MasterDataResponse
    suspend fun getMasterOfType(type: String): MasterDataResponse
    suspend fun getLocation(zipcode: String?): LocationResponse
    suspend fun getZipCodes(zipcode: String?): ZipcodeResponse
    suspend fun getCounties(page:Int?,pageSize:Int?,search: String?): ZipcodeResponse
    suspend fun getGroups():GroupsResponse
}