package io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2

import io.github.feelfreelinux.wykopmobilny.models.dataclass.EntryLink
import io.github.feelfreelinux.wykopmobilny.models.mapper.Mapper
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.EntryLinkResponse

class EntryLinkMapper {
    companion object : Mapper<EntryLinkResponse, EntryLink> {
        override fun map(value: EntryLinkResponse): EntryLink {
            return  EntryLink(
                    if (value.link != null) LinkMapper.map(value.link) else null,
                    if (value.entry != null) EntryMapper.map(value.entry) else null
            )
        }
    }
}