package io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2

import io.github.feelfreelinux.wykopmobilny.models.dataclass.TagLinks
import io.github.feelfreelinux.wykopmobilny.models.mapper.Mapper
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.responses.TagLinksResponse
import io.github.feelfreelinux.wykopmobilny.utils.LinksPreferencesApi

class TagLinksMapper {
    companion object {
        fun map(value: TagLinksResponse, linksPreferencesApi: LinksPreferencesApi): TagLinks {
            return TagLinks(value.data!!.map { LinkMapper.map(it, linksPreferencesApi) }, value.meta)
        }
    }
}