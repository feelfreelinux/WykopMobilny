package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models

import com.squareup.moshi.Json

data class NotificationResponse(
        @Json(name = "id")
        val id : Int,
        @Json(name = "author")
        val author : AuthorResponse?,
        @Json(name = "date")
        val date : String,
        @Json(name = "body")
        val body : String,
        @Json(name = "type")
        val type : String,
        @Json(name = "item_id")
        val itemId : String?,
        @Json(name = "url")
        val url : String?,
        @Json(name = "new")
        val new : Boolean
)