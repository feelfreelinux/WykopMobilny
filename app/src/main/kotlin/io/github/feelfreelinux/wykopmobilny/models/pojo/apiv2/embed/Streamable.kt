package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.embed

import com.squareup.moshi.Json




data class Streamable(
        @Json(name="files")
        val files: StreamableFiles
)