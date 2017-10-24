package io.github.feelfreelinux.wykopmobilny.models.mapper.tag

import io.github.feelfreelinux.wykopmobilny.models.dataclass.TagMeta
import io.github.feelfreelinux.wykopmobilny.models.mapper.Mapper
import io.github.feelfreelinux.wykopmobilny.models.pojo.TagMetaResponse

class TagMetaMapper {
    companion object : Mapper<TagMetaResponse, TagMeta> {
        override fun map(value: TagMetaResponse): TagMeta {
            value.run {
                return TagMeta(tag, isObserved ?: false, isObserved ?: false)
            }
        }
    }
}