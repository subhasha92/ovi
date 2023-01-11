package org.ovi.feature.profile.model

import android.os.Parcelable
import org.ovi.feature.auth.model.Address
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize


@JsonClass(generateAdapter = true)
@Parcelize
data class EditProfileRequest(

	@Json(name="birthday")
    var birthday: String? = null,

	@Json(name="address")
	var address: AddressEditProfile? = null,

	@Json(name="code")
	val code: String? = null,

	@Json(name="groups")
	var groups: List<Int?>? = null,

	@Json(name="role")
	val role: String? = null,

	@Json(name="gender")
	var gender: String? = null,

	@Json(name="race")
	var race: String? = null,

	@Json(name="ethnicity")
	var ethnicity: String? = null,

	@Json(name="shirt_size")
	var shirt_size: String? = null,

	@Json(name="image_url")
    var image_url: String? = null,

	@Json(name="county")
	var county: String? = null,

	@Json(name="agency_id")
	val agency_id: Int? = null,

	@Json(name="answers")
    var answers: List<AnswersItem?>? = null,

	@Json(name="youth_council_name")
	var youth_council_name: String? = null,

	@Json(name="joined_on")
	val joined_on: String? = null,

	@Json(name="emails")
	val emails: List<String?>? = null,

	@Json(name="phone")
    var phone: String? = null,

	@Json(name="name")
	val name: String? = null,

	@Json(name="status")
	val status: String? = null
):Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class AnswersItem(

    @Json(name="text_value")
    var text_value: String? = null,

    @Json(name="question_id")
    val question_id: Int? = null,

    @Json(name="choice_id")
    var choice_id: Int? = null
):Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class AddressEditProfile(

	@Json(name="zipcode")
	var zipcode: String? = null,

	@Json(name="city")
	var city: String? = null,

	@Json(name="street")
	var street: String? = null,

	@Json(name="county")
	var county: String? = null,

	@Json(name="state")
	var state: String? = null
):Parcelable
