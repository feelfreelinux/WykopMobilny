package io.github.wykopmobilny.models.mapper.apiv2

import io.github.wykopmobilny.api.responses.ConversationResponse
import io.github.wykopmobilny.models.dataclass.Conversation
import io.github.wykopmobilny.models.mapper.Mapper
import io.github.wykopmobilny.utils.toPrettyDate

object ConversationMapper : Mapper<ConversationResponse, Conversation> {
    override fun map(value: ConversationResponse) =
        Conversation(AuthorMapper.map(value.receiver), value.lastUpdate.toPrettyDate())
}
