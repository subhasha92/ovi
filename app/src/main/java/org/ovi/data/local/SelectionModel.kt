package org.ovi.data.local

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SelectionModel(
    @Json(name="content")
    val content : String,
    @Json(name="isSelected")
    var isSelected : Boolean = false)
