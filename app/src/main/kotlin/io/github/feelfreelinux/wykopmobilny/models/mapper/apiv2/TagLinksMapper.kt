package io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2

import io.github.feelfreelinux.wykopmobilny.models.dataclass.TagLinks
import io.github.feelfreelinux.wykopmobilny.models.mapper.Mapper
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.responses.TagLinksResponse

class TagLinksMapper {
    companion object : Mapper<TagLinksResponse, TagLinks> {
        override fun map(value: TagLinksResponse): TagLinks {
            return TagLinks(value.data!!.map { LinkMapper.map(it) }, value.meta)
        }
    }
}