package io.github.wykopmobilny.models.mapper.apiv2

import io.github.wykopmobilny.api.responses.FullConversationResponse
import io.github.wykopmobilny.models.dataclass.FullConversation
import io.github.wykopmobilny.models.mapper.Mapper

object FullConversationMapper : Mapper<FullConversationResponse, FullConversation> {
    override fun map(value: FullConversationResponse) =
        FullConversation(
            value.data!!.map { PMMessageMapper.map(it) },
            AuthorMapper.map(value.receiver)
        )
}
