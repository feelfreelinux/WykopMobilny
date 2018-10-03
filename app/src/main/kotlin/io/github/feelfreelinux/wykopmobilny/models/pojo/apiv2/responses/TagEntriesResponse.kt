package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.responses

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.common.ApiResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.common.WykopErrorResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.EntryResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.TagMetaResponse

@JsonIgnoreProperties(ignoreUnknown = true)
class TagEntriesResponse(
    @JsonProperty("data") override val data: List<EntryResponse>?,
    @JsonProperty("error") override val error: WykopErrorResponse?,
    @JsonProperty("meta") val meta: TagMetaResponse
) : ApiResponse<List<EntryResponse>>