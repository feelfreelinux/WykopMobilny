package io.github.feelfreelinux.wykopmobilny.models.dataclass

import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.TagMetaResponse

data class TagLinks(
        val entries : List<Link>,
        val meta : TagMetaResponse
)