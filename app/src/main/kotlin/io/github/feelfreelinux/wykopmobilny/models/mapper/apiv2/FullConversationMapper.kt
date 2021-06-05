package io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2

import io.github.feelfreelinux.wykopmobilny.models.dataclass.FullConversation
import io.github.feelfreelinux.wykopmobilny.models.mapper.Mapper
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.responses.FullConversationResponse

class FullConversationMapper {
    companion object : Mapper<FullConversationResponse, FullConversation> {
        override fun map(value: FullConversationResponse) =
            FullConversation(
                value.data!!.map { PMMessageMapper.map(it) },
                AuthorMapper.map(value.receiver)
            )
    }
}
