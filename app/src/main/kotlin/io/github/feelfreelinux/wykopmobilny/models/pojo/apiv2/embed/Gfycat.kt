package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.embed

import com.squareup.moshi.Json

data class Gfycat(
        @Json(name = "gfyItem")
        val gfyItem : GfycatItem
)