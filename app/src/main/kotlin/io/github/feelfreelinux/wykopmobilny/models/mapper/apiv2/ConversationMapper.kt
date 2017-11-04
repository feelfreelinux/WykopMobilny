package io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2

import io.github.feelfreelinux.wykopmobilny.models.dataclass.Conversation
import io.github.feelfreelinux.wykopmobilny.models.mapper.Mapper
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.ConversationResponse

class ConversationMapper {
    companion object : Mapper<ConversationResponse, Conversation> {
        override fun map(value: ConversationResponse): Conversation {
            return Conversation(AuthorMapper.map(value.receiver), value.lastUpdate)
        }
    }
}