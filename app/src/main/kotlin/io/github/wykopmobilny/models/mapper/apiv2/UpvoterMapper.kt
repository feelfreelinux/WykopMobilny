package io.github.wykopmobilny.models.mapper.apiv2

import io.github.wykopmobilny.api.responses.UpvoterResponse
import io.github.wykopmobilny.models.dataclass.Upvoter
import io.github.wykopmobilny.models.mapper.Mapper

object UpvoterMapper : Mapper<UpvoterResponse, Upvoter> {

    override fun map(value: UpvoterResponse) =
        Upvoter(AuthorMapper.map(value.author), value.date)
}
