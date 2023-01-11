package org.ovi.data.remote

import okhttp3.RequestBody
import org.ovi.feature.profile.model.*
import org.ovi.feature.register.model.LocationResponse
import org.ovi.feature.register.model.MasterDataResponse
import org.ovi.feature.register.model.RegisterResponse
import org.ovi.feature.register.model.ZipcodeResponse
import retrofit2.Response
import retrofit2.http.*


interface ProfileServices {

    @GET("member-user/{id}")
    suspend fun getProfile(@Path("id") id: Int): GetProfileResponse

    @PUT("member-user/{id}")
    suspend fun putProfile(@Path("id") id: Int, @Body request: EditProfileRequest): RegisterResponse

    @GET("agency")
    suspend fun getAgency(): AgencyResponse

    @GET("master-data/location-counties")
    suspend fun getCounties(
        @Query("page") page: Int? = null,
        @Query("pageSize") pageSize: Int? = null,
        @Query("search") search: String? = null
    ): ZipcodeResponse

    @POST("master-data/assets/upload-url")
    suspend fun uploadProfile(@Body reqeust: UploadImageReguest): UploadImageResponse

    @PUT
    suspend fun uploading(@Url url: String, @Body image: RequestBody): Response<Any>?

    @GET("master-data/{type}")
    suspend fun getMasterData(@Path("type") type: String): MasterDataResponse

    @GET("master-data/{type}")
    suspend fun getLocationData(
        @Path("type") type: String,
        @Query("zipcode") zipcode: String?
    ): LocationResponse

    @GET("master-data/zipcodes")
    suspend fun getZipCodes(
        @Query("zipcode") zipcode: String?
    ): ZipcodeResponse

    @GET("group")
    suspend fun getGroups(): GroupsResponse



}