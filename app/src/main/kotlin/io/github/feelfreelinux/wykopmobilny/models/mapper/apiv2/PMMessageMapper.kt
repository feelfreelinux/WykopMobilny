package io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2

import io.github.feelfreelinux.wykopmobilny.models.dataclass.PMMessage
import io.github.feelfreelinux.wykopmobilny.models.mapper.Mapper
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.PMMessageResponse

class PMMessageMapper {
    companion object : Mapper<PMMessageResponse, PMMessage> {
        override fun map(value: PMMessageResponse): PMMessage {
            return PMMessage(value.date, value.body ?: "",
                    if (value.embed != null) EmbedMapper.map(value.embed) else null,
                    value.direction != "received",
                    value.app)
        }
    }
}