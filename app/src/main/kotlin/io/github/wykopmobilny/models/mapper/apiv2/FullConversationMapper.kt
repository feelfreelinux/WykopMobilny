package io.github.wykopmobilny.models.mapper.apiv2

import io.github.wykopmobilny.api.responses.FullConversationResponse
import io.github.wykopmobilny.models.dataclass.FullConversation
import io.github.wykopmobilny.models.mapper.Mapper

class FullConversationMapper {
    companion object : Mapper<FullConversationResponse, FullConversation> {
        override fun map(value: FullConversationResponse) =
            FullConversation(
                value.data!!.map { PMMessageMapper.map(it) },
                AuthorMapper.map(value.receiver)
            )
    }
}
