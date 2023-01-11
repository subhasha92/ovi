package org.ovi.feature.survey.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SubmitSurveyResponse(

	@Json(name="data")
	val data: DataSubmit? = null,

	@Json(name="message")
	val message: String? = null,

	@Json(name="statusCode")
	val statusCode: Int? = null
)
@JsonClass(generateAdapter = true)
data class DataSubmit(

	@Json(name="dateValue")
	val dateValue: Any? = null,

	@Json(name="questionId")
	val questionId: String? = null,

	@Json(name="textValue")
	val textValue: Any? = null,

	@Json(name="questionnaireId")
	val questionnaireId: String? = null,

	@Json(name="questionChoiceId")
	val questionChoiceId: String? = null,

	@Json(name="participantId")
	val participantId: String? = null,

	@Json(name="timeTaken")
	val timeTaken: Int? = null,

	@Json(name="createdAt")
	val createdAt: String? = null,

	@Json(name="deletedAt")
	val deletedAt: Any? = null,

	@Json(name="id")
	val id: String? = null,

	@Json(name="position")
	val position: Int? = null,

	@Json(name="numberValue")
	val numberValue: Any? = null,

	@Json(name="projectId")
	val projectId: String? = null,

	@Json(name="updatedAt")
	val updatedAt: String? = null
)
