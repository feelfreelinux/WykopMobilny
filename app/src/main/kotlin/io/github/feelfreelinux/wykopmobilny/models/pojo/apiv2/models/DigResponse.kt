package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models

import com.squareup.moshi.Json

data class DigResponse (
    @Json(name="digs")
    val diggs : Int,

    @Json(name="buries")
    val buries : Int
)