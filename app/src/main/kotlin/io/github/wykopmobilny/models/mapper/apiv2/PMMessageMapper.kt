package io.github.wykopmobilny.models.mapper.apiv2

import io.github.wykopmobilny.api.responses.PMMessageResponse
import io.github.wykopmobilny.models.dataclass.PMMessage
import io.github.wykopmobilny.models.mapper.Mapper
import io.github.wykopmobilny.utils.toPrettyDate

object PMMessageMapper : Mapper<PMMessageResponse, PMMessage> {

    override fun map(value: PMMessageResponse) =
        PMMessage(
            date = value.date.toPrettyDate(), body = value.body ?: "",
            embed = value.embed?.let(EmbedMapper::map),
            isSentFromUser = value.direction != "received",
            app = value.app
        )
}
