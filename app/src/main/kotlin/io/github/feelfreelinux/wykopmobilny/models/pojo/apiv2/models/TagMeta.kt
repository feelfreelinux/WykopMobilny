package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models

import com.squareup.moshi.Json

data class TagMetaResponse(
        @Json(name="tag")
        val tag: String,

        @Json(name="is_observed")
        var isObserved: Boolean,

        @Json(name="is_blocked")
        var isBlocked: Boolean,

        @Json(name="is_own")
        val isOwn : Boolean,

        @Json(name="description")
        val description : String?,

        @Json(name="background")
        val background : String?)