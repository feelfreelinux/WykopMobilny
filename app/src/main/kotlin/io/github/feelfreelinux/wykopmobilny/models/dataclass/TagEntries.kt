package io.github.feelfreelinux.wykopmobilny.models.dataclass

import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.TagMetaResponse

data class TagEntries(
        val entries : List<Entry>,
        val meta : TagMetaResponse
)