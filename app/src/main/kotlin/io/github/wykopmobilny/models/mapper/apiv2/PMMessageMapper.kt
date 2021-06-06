package io.github.wykopmobilny.models.mapper.apiv2

import io.github.wykopmobilny.api.responses.PMMessageResponse
import io.github.wykopmobilny.models.dataclass.PMMessage
import io.github.wykopmobilny.models.mapper.Mapper
import io.github.wykopmobilny.utils.toPrettyDate

class PMMessageMapper {
    companion object : Mapper<PMMessageResponse, PMMessage> {
        override fun map(value: PMMessageResponse) =
            PMMessage(
                value.date.toPrettyDate(), value.body ?: "",
                if (value.embed != null) EmbedMapper.map(value.embed!!) else null,
                value.direction != "received",
                value.app
            )
    }
}
