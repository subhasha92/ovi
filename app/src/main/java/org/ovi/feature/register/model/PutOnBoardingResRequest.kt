package org.ovi.feature.register.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.ovi.feature.profile.model.AnswersItem
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
@JsonClass(generateAdapter = true)
data class PutOnBoardingResRequest(
	@Json(name="answers")
	var answers: @RawValue MutableList<AnswersItem?>? = null
) : Parcelable


