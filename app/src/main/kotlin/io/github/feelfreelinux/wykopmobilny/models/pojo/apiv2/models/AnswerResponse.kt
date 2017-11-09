package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models

import com.squareup.moshi.Json

data class AnswerResponse(
        @Json(name="id")
        val id : Int,

        @Json(name="answer")
        val answer : String,

        @Json(name="count")
        val count : Int,

        @Json(name="percentage")
        val percentage : Double
)