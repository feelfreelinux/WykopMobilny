package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.responses

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.common.ApiResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.common.WykopErrorResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.LinkResponse

@JsonIgnoreProperties(ignoreUnknown = true)
class NewLinkResponse(
    @JsonProperty("data") override val data: NewLinkInformationResponse?,
    @JsonProperty("error") override val error: WykopErrorResponse?,
    @JsonProperty("duplicates") val duplicates: List<LinkResponse>? = emptyList()
) : ApiResponse<NewLinkInformationResponse>
