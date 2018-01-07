package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.responses

import com.squareup.moshi.Json
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.common.ApiResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.common.WykopErrorResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.AuthorResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.PMMessageResponse

class FullConversationResponse(
        @Json(name = "data")
        override val data: List<PMMessageResponse>?,

        @Json(name = "error")
        override val error: WykopErrorResponse?,

        @Json(name="receiver")
        val receiver : AuthorResponse
) : ApiResponse<List<PMMessageResponse>>