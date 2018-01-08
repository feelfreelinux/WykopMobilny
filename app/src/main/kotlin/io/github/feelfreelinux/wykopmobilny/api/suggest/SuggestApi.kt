package io.github.feelfreelinux.wykopmobilny.api.suggest

import io.github.feelfreelinux.wykopmobilny.models.dataclass.Author
import io.github.feelfreelinux.wykopmobilny.models.dataclass.TagSuggestion
import io.reactivex.Single

interface SuggestApi {
    fun getTagSuggestions(suggestionString : String) : Single<List<TagSuggestion>>
    fun getUserSuggestions(suggestionString: String) : Single<List<Author>>
}