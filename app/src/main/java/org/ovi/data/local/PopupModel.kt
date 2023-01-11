package org.ovi.data.local

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PopupModel(
    @Json(name="id")
    var id: Int = 0,
    @Json(name="title")
    var title: String,
    @Json(name="isSelected")
    var isSelected: Boolean = false,
    @Json(name="highlight")
    var highlight: Boolean = false,
    @Json(name="pic")
    var pic: Int = -1
)