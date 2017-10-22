package io.github.feelfreelinux.wykopmobilny.models.mapper

import io.github.feelfreelinux.wykopmobilny.models.dataclass.Author
import io.github.feelfreelinux.wykopmobilny.models.dataclass.PMMessage
import io.github.feelfreelinux.wykopmobilny.models.pojo.PMMessageResponse

class PMMessageMapper {
    companion object : Mapper<PMMessageResponse, PMMessage>{
        override fun map(value: PMMessageResponse): PMMessage {
            return PMMessage(
                    Author(value.author, value.authorAvatarMed, value.authorGroup, value.authorSex, value.app),
                    value.date,
                    value.body,
                    value.embed,
                    value.status != "read",
                    value.direction != "received"
            )
        }

    }
}