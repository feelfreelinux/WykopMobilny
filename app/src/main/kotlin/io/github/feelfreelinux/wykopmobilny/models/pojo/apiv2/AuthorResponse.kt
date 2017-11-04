package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2

import com.squareup.moshi.Json

data class AuthorResponse(
        @Json(name = "login")
        val login : String,

        @Json(name = "color")
        val color : Int,

        @Json(name = "sex")
        val sex : String?,

        @Json(name = "avatar")
        val avatar : String
)