package org.ovi.feature.survey.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SubmitSurveyRequest(

    @Json(name = "timeTaken")
    val timeTaken: Int? = null,

    @Json(name = "questionId")
    val questionId: String? = null,

    @Json(name = "response")
    val response: Response? = null,

    @Json(name = "surveyType")
    var surveyType: String? = null,

    @Json(name = "eventId")
    var eventId: String? = null,



    @Json(name = "questionType")
    val questionType: String? = null
)

@JsonClass(generateAdapter = true)
data class Response(

    @Json(name = "dateValue")
    var dateValue: String? = null,

//    @Json(name = "textValue")
//    val textValueList: List<String>? = null,

    @Json(name = "textValue")
    var textValue: String? = null,

    @Json(name = "questionChoiceIds")
    val questionChoiceIds: List<String>? = null,

    @Json(name = "questionChoiceId")
    val questionChoiceId: String? = null,

    @Json(name = "numberValue")
    val numberValue: Int? = null,

    @Json(name = "remarkValue")
    var remarkValue: String? = null
)
