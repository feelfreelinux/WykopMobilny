package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.responses

import com.squareup.moshi.Json
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.common.ApiResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.common.WykopErrorResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.EntryResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.LinkResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.TagMetaResponse

class TagLinksResponse(
        @Json(name = "data")
        override val data: List<LinkResponse>?,

        @Json(name = "error")
        override val error: WykopErrorResponse?,

        @Json(name="meta")
        val meta : TagMetaResponse
) : ApiResponse<List<LinkResponse>>