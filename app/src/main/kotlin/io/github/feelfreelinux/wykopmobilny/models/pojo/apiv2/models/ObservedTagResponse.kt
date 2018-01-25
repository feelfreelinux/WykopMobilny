package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models

import com.squareup.moshi.Json

class ObservedTagResponse(
        @Json(name="tag")
        val tag : String
)