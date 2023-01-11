package org.ovi.feature.register.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
@JsonClass(generateAdapter = true)
data class RegisterRequest(

	@Json(name="birthday")
    var birthday: String? = null,

	@Json(name="address")
	var address: Address? = null,

	@Json(name="code")
	val code: String? = null,

	@Json(name="role")
	val role: String? = null,

	@Json(name="gender")
	var gender: String? = null,

	@Json(name="race")
	var race: String? = null,

	@Json(name="ethnicity")
	var ethnicity: String? = null,

	@Json(name="shirt_size")
	val shirt_size: String? = null,

	@Json(name="image_url")
	val image_url: String? = null,

	@Json(name="county")
	val county: String? = null,

	@Json(name="youth_council_name")
	val youth_council_name: String? = null,

	@Json(name="joined_on")
	val joined_on: String? = null,

	@Json(name="emails")
	val emails: List<String?>? = null,

	@Json(name="password")
	val password: String? = null,

	@Json(name="phone")
    var phone: String? = null,

	@Json(name="agency_id")
	val agency_id: Int? = null,

	@Json(name="name")
	val name: String? = null
) : Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class Address(

	@Json(name="zipcode")
	val zipcode:@RawValue Any? = null,

	@Json(name="city")
	val city:@RawValue Any? = null,

	@Json(name="street")
	var street: String? = null,

	@Json(name="county")
	val county: String? = null,

	@Json(name="state")
	val state: String? = null
) : Parcelable
