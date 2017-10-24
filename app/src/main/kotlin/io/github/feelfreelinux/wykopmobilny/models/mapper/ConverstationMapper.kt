package io.github.feelfreelinux.wykopmobilny.models.mapper

import io.github.feelfreelinux.wykopmobilny.models.dataclass.Author
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Conversation
import io.github.feelfreelinux.wykopmobilny.models.pojo.ConversationsListResponse

class ConverstationMapper {
    companion object : Mapper<ConversationsListResponse, Conversation> {
        override fun map(value: ConversationsListResponse): Conversation {
            return Conversation(
                    Author(value.author, value.authorAvatarMed, value.authorGroup, value.authorSex, null),
                    value.lastUpdate,
                    value.status != "read"
            )
        }

    }
}