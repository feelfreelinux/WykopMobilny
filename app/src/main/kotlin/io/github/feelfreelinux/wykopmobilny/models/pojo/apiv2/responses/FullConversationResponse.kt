package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.responses

import com.fasterxml.jackson.annotation.JsonProperty import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.common.ApiResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.common.WykopErrorResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.AuthorResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.PMMessageResponse
@JsonIgnoreProperties(ignoreUnknown = true)
class FullConversationResponse(
        @JsonProperty("data")
        override val data: List<PMMessageResponse>?,

        @JsonProperty("error")
        override val error: WykopErrorResponse?,

        @JsonProperty("receiver")
        val receiver : AuthorResponse
) : ApiResponse<List<PMMessageResponse>>