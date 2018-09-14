package io.github.feelfreelinux.wykopmobilny.api.suggest

import io.github.feelfreelinux.wykopmobilny.api.UserTokenRefresher
import io.github.feelfreelinux.wykopmobilny.api.errorhandler.ErrorHandlerTransformer
import io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2.AuthorMapper
import io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2.TagSuggestionsMapper
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.AuthorResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.TagSuggestionResponse
import retrofit2.Retrofit

class SuggestRepository(
    retrofit: Retrofit,
    val userTokenRefresher: UserTokenRefresher
) : SuggestApi {

    private val suggestApi by lazy { retrofit.create(SuggestRetrofitApi::class.java) }

    override fun getTagSuggestions(suggestionString: String) =
        suggestApi.getTagSuggestions(suggestionString)
            .retryWhen(userTokenRefresher)
            .compose<List<TagSuggestionResponse>>(ErrorHandlerTransformer())
            .map { it.map { response -> TagSuggestionsMapper.map(response) } }

    override fun getUserSuggestions(suggestionString: String) =
        suggestApi.getUsersSuggestions(suggestionString)
            .retryWhen(userTokenRefresher)
            .compose<List<AuthorResponse>>(ErrorHandlerTransformer())
            .map { it.map { response -> AuthorMapper.map(response) } }
}