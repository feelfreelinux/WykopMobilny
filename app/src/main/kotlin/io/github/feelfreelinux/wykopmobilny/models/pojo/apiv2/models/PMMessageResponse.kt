package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models

import com.squareup.moshi.Json

data class PMMessageResponse(
        @Json(name = "id")
        val id : Int,

        @Json(name = "date")
        val date : String,

        @Json(name = "body")
        val body : String?,

        @Json(name = "status")
        val status : String,

        @Json(name = "direction")
        val direction : String,

        @Json(name = "embed")
        val embed : EmbedResponse?,

        @Json(name = "app")
        val app : String?
)