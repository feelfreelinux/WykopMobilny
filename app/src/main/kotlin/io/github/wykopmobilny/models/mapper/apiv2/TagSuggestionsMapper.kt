package io.github.wykopmobilny.models.mapper.apiv2

import io.github.wykopmobilny.api.responses.TagSuggestionResponse
import io.github.wykopmobilny.models.dataclass.TagSuggestion
import io.github.wykopmobilny.models.mapper.Mapper

class TagSuggestionsMapper {
    companion object : Mapper<TagSuggestionResponse, TagSuggestion> {
        override fun map(value: TagSuggestionResponse) =
            TagSuggestion(
                value.tag,
                value.followers
            )
    }
}
