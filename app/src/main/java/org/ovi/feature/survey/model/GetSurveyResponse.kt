package org.ovi.feature.survey.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetSurveyResponse(

	@Json(name="data")
	val data: Data? = null,

	@Json(name="message")
	val message: String? = null,

	@Json(name="statusCode")
	val statusCode: Int? = null
)

@JsonClass(generateAdapter = true)
data class Attachment(

	@Json(name="createdAt")
	val createdAt: String? = null,

	@Json(name="deletedAt")
	val deletedAt: Any? = null,

	@Json(name="questionId")
	val questionId: String? = null,

	@Json(name="id")
	val id: String? = null,

	@Json(name="href")
	val href: String? = null,

	@Json(name="type")
	val type: String? = null,

	@Json(name="updatedAt")
	val updatedAt: String? = null
)
@JsonClass(generateAdapter = true)
data class QuestionsItem(

	@Json(name="varName")
	val varName: String? = null,

	@Json(name="title_ch")
	val title_ch: Any? = null,

	@Json(name="questionCode")
	val questionCode: Any? = null,

	@Json(name="description")
	val description: Any? = null,

	@Json(name="enforceDecimal")
	val enforceDecimal: Boolean? = null,

	@Json(name="remark")
	val remark: Boolean? = null,

	@Json(name="questionnaireId")
	val questionnaireId: String? = null,

	@Json(name="title")
	val title: String? = null,

	@Json(name="type")
	val type: String? = null,

	@Json(name="required")
	val required: Boolean? = null,

	@Json(name="mainQuestionId")
	val mainQuestionId: Any? = null,

	@Json(name="createdAt")
	val createdAt: String? = null,

	@Json(name="ref")
	val ref: String? = null,

	@Json(name="deletedAt")
	val deletedAt: Any? = null,

	@Json(name="uploadable")
	val uploadable: Boolean? = null,

	@Json(name="attachment")
	val attachment: Attachment? = null,

	@Json(name="id")
	val id: String? = null,

	@Json(name="position")
	val position: Int? = null,

	@Json(name="properties")
	val properties: Properties? = null,

	@Json(name="updatedAt")
	val updatedAt: String? = null
)

@JsonClass(generateAdapter = true)
data class Properties(

	@Json(name="maxRange")
	val maxRange: Int? = null,

	@Json(name="minRange")
	val minRange: Int? = null,

	@Json(name="choices")
	val choices: List<ChoicesItem?>? = null
)
@JsonClass(generateAdapter = true)
data class LinksItem(

	@Json(name="label")
	val label: String? = null,

	@Json(name="url")
	val url: String? = null
)
@JsonClass(generateAdapter = true)
data class Data(

	@Json(name="questionCount")
	val questionCount: Int? = null,

	@Json(name="maxTime")
	val maxTime: Int? = null,

	@Json(name="notes")
	val notes: String? = null,

	@Json(name="questions")
	val questions: List<QuestionsItem?>? = null,

	@Json(name="description")
	val description: String? = null,

	@Json(name="logicJumps")
	val logicJumps: List<Any?>? = null,

	@Json(name="createdAt")
	val createdAt: String? = null,

	@Json(name="deletedAt")
	val deletedAt: Any? = null,

	@Json(name="measure")
	val measure: Any? = null,

	@Json(name="scoringCriteria")
	val scoringCriteria: String? = null,

	@Json(name="minTime")
	val minTime: Int? = null,

	@Json(name="imageUrl")
	val imageUrl: String? = null,

	@Json(name="name")
	val name: String? = null,

	@Json(name="links")
	val links: List<LinksItem?>? = null,

	@Json(name="id")
	val id: String? = null,

	@Json(name="category")
	val category: Int? = null,

	@Json(name="status")
	val status: String? = null,

	@Json(name="updatedAt")
	val updatedAt: String? = null
)
@JsonClass(generateAdapter = true)
data class ChoicesItem(

	@Json(name="createdAt")
	val createdAt: String? = null,

	@Json(name="ref")
	val ref: String? = null,

	@Json(name="deletedAt")
	val deletedAt: Any? = null,

	@Json(name="questionId")
	val questionId: String? = null,

	@Json(name="attachment")
	val attachment: Any? = null,

	@Json(name="label_ch")
	val labelCh: Any? = null,

	@Json(name="id")
	val id: String? = null,

	@Json(name="label")
	val label: String? = null,

	@Json(name="position")
	val position: Int? = null,

	@Json(name="isOther")
	val isOther: Boolean? = null,

	@Json(name="updatedAt")
	val updatedAt: String? = null
)
