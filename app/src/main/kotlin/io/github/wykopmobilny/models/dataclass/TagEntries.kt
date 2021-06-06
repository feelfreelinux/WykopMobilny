package io.github.wykopmobilny.models.dataclass

import io.github.wykopmobilny.api.responses.TagMetaResponse

data class TagEntries(
    val entries: List<Entry>,
    val meta: TagMetaResponse
)
