package io.github.wykopmobilny.models.mapper.apiv2

import io.github.wykopmobilny.api.responses.DownvoterResponse
import io.github.wykopmobilny.models.dataclass.Downvoter
import io.github.wykopmobilny.models.mapper.Mapper

class DownvoterMapper {
    companion object : Mapper<DownvoterResponse, Downvoter> {
        override fun map(value: DownvoterResponse) =
            Downvoter(AuthorMapper.map(value.author), value.date, value.reason)
    }
}
