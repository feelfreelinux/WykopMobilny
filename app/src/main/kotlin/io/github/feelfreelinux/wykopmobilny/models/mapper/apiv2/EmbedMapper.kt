package io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2

import io.github.feelfreelinux.wykopmobilny.models.dataclass.Embed
import io.github.feelfreelinux.wykopmobilny.models.mapper.Mapper
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.EmbedResponse

class EmbedMapper {
    companion object : Mapper<EmbedResponse, Embed> {
        override fun map(value: EmbedResponse) =
            Embed(value.type, value.preview, value.url, value.plus18, value.source, value.animated, value.size ?: "")
    }
}
