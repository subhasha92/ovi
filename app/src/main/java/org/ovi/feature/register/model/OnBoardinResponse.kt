package org.ovi.feature.register.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
@JsonClass(generateAdapter = true)
data class OnBoardinResponse(

	@Json(name="data")
	val data: Data1? = null,

	@Json(name="message")
	val message: String? = null,

	@Json(name="statusCode")
	val statusCode: Int? = null
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class Data1(

	@Json(name="count")
	val count: Int? = null,

	@Json(name="rows")
	val rows: List<RowsItem?>? = null
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class ChoicesItem(

	@Json(name="createdAt")
	val createdAt: String? = null,

	@Json(name="id")
	val id: Int? = null,

	@Json(name="position")
	val position: Int? = null,

	@Json(name="choice")
	val choice: String? = null,

	@Json(name="question_id")
	val questionId: Int? = null,

	@Json(name="updatedAt")
	val updatedAt: String? = null
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class RowsItem(

	@Json(name="createdAt")
	val createdAt: String? = null,

	@Json(name="question")
	val question: String? = null,

	@Json(name="id")
	val id: Int? = null,

	@Json(name="position")
	val position: Int? = null,

	@Json(name="type")
	val type: String? = null,

	@Json(name="choices")
	val choices:@RawValue List<ChoicesItem?>? = null,

	@Json(name="updatedAt")
	val updatedAt: String? = null
) : Parcelable
