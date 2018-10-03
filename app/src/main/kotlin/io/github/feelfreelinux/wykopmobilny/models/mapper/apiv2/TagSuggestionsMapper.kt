package io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2

import io.github.feelfreelinux.wykopmobilny.models.dataclass.TagSuggestion
import io.github.feelfreelinux.wykopmobilny.models.mapper.Mapper
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.TagSuggestionResponse

class TagSuggestionsMapper {
    companion object : Mapper<TagSuggestionResponse, TagSuggestion> {
        override fun map(value: TagSuggestionResponse) =
            TagSuggestion(
                value.tag,
                value.followers
            )
    }
}