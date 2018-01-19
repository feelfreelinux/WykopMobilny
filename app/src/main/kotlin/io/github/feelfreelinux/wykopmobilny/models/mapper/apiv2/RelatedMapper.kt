package io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2

import io.github.feelfreelinux.wykopmobilny.models.dataclass.Related
import io.github.feelfreelinux.wykopmobilny.models.mapper.Mapper
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.RelatedResponse

class RelatedMapper {
    companion object : Mapper<RelatedResponse, Related> {
        override fun map(value: RelatedResponse): Related {
            return Related(value.id, value.url, value.voteCount, AuthorMapper.map(value.author))
        }
    }
}