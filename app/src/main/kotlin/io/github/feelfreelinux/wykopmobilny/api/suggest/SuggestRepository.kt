package io.github.feelfreelinux.wykopmobilny.api.suggest

import io.github.feelfreelinux.wykopmobilny.api.errorhandler.ErrorHandlerTransformer
import io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2.AuthorMapper
import io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2.TagSuggestionsMapper
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.AuthorResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.TagSuggestionResponse
import retrofit2.Retrofit

class SuggestRepository(retrofit: Retrofit) : SuggestApi {
    private val suggestApi by lazy { retrofit.create(SuggestRetrofitApi::class.java) }

    override fun getTagSuggestions(suggestionString : String) =
            suggestApi.getTagSuggestions(suggestionString)
                    .compose<List<TagSuggestionResponse>>(ErrorHandlerTransformer())
                    .map { it.map { TagSuggestionsMapper.map(it) } }

    override fun getUserSuggestions(suggestionString: String) =
            suggestApi.getUsersSuggestions(suggestionString)
                    .compose<List<AuthorResponse>>(ErrorHandlerTransformer())
                    .map { it.map { AuthorMapper.map(it) } }
}