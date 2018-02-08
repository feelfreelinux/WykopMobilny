package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.responses

import com.squareup.moshi.Json

data class BadgeResponse(
        @Json(name = "name")
        val name : String,

        @Json(name = "date")
        val date : String,

        @Json(name = "description")
        val description : String,

        @Json(name = "icon")
        val icon : String
)