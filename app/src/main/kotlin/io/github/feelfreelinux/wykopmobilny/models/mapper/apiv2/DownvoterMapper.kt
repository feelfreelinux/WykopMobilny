package io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2

import io.github.feelfreelinux.wykopmobilny.models.dataclass.Downvoter
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Upvoter
import io.github.feelfreelinux.wykopmobilny.models.mapper.Mapper
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.DownvoterResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.UpvoterResponse

class DownvoterMapper {
    companion object : Mapper<DownvoterResponse, Downvoter> {
        override fun map(value: DownvoterResponse): Downvoter {
            return Downvoter(AuthorMapper.map(value.author), value.date, value.reason)
        }
    }
}