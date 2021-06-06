package io.github.wykopmobilny.models.dataclass

import io.github.wykopmobilny.api.responses.TagMetaResponse

data class TagLinks(
    val entries: List<Link>,
    val meta: TagMetaResponse
)
