package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models

import com.squareup.moshi.Json

class TagStateResponse (
        @Json(name="is_observed")
        val isObserved : Boolean,
        @Json(name="is_blocked")
        val isBlocked : Boolean
)