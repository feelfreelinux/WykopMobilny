package io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2

import io.github.feelfreelinux.wykopmobilny.models.dataclass.Upvoter
import io.github.feelfreelinux.wykopmobilny.models.mapper.Mapper
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.UpvoterResponse

class UpvoterMapper {
    companion object : Mapper<UpvoterResponse, Upvoter> {
        override fun map(value: UpvoterResponse): Upvoter {
            return Upvoter(AuthorMapper.map(value.author), value.date)
        }
    }
}