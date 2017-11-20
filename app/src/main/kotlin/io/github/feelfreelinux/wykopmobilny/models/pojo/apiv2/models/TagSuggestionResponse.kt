package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models

import com.squareup.moshi.Json

data class TagSuggestionResponse(
        @Json(name="tag")
        val tag : String,

        @Json(name="followers")
        val followers : Int
)