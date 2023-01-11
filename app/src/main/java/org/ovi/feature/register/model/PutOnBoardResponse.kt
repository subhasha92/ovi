package org.ovi.feature.register.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PutOnBoardResponse(

	@Json(name="data")
	val data: List<DataItem?>? = null,

	@Json(name="message")
	val message: String? = null,

	@Json(name="statusCode")
	val statusCode: Int? = null
)

@JsonClass(generateAdapter = true)
data class DataItem(

	@Json(name="text_value")
	val textValue: String? = null,

	@Json(name="question_id")
	val questionId: Int? = null,

	@Json(name="choice_id")
	val choiceId: Int? = null
)
