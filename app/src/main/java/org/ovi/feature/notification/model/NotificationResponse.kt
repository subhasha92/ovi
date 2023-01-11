package org.ovi.feature.notification.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NotificationResponse(

    @Json(name = "data")
    val data: Data? = null,
    @Json(name = "message")
    val message: String? = null,
    @Json(name = "statusCode")
    val statusCode: Int? = null
)

@JsonClass(generateAdapter = true)
data class RowsItem(
    @Json(name = "createdAt")
    val createdAt: String? = null,
    @Json(name = "user_id")
    val userId: Int? = null,
    @Json(name = "subject")
    val subject: String? = null,
    @Json(name = "id")
    val id: Int? = null,
    @Json(name = "destination_type")
    val destination_type: String? = null,
    @Json(name = "destination")
    val destination: String? = null,
    @Json(name = "is_read")
    var is_read: Boolean? = null,

    @Json(name = "media")
    val media: List<String?>? = null,
    @Json(name = "body")
    val body: String? = null,
    @Json(name = "updatedAt")
    val updatedAt: String? = null
)

@JsonClass(generateAdapter = true)
data class Data(

    @Json(name = "count")
    val count: Int? = null,

    @Json(name = "rows")
    val rows: List<RowsItem?>? = null
)
