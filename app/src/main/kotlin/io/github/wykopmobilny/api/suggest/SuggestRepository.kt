package io.github.wykopmobilny.api.suggest

import io.github.wykopmobilny.api.UserTokenRefresher
import io.github.wykopmobilny.api.endpoints.SuggestRetrofitApi
import io.github.wykopmobilny.api.errorhandler.ErrorHandlerTransformer
import io.github.wykopmobilny.models.mapper.apiv2.AuthorMapper
import io.github.wykopmobilny.models.mapper.apiv2.TagSuggestionsMapper
import kotlinx.coroutines.rx2.rxSingle
import javax.inject.Inject

class SuggestRepository @Inject constructor(
    private val suggestApi: SuggestRetrofitApi,
    private val userTokenRefresher: UserTokenRefresher
) : SuggestApi {

    override fun getTagSuggestions(suggestionString: String) =
        rxSingle { suggestApi.getTagSuggestions(suggestionString) }
            .retryWhen(userTokenRefresher)
            .compose(ErrorHandlerTransformer())
            .map { it.map { response -> TagSuggestionsMapper.map(response) } }

    override fun getUserSuggestions(suggestionString: String) =
        rxSingle { suggestApi.getUsersSuggestions(suggestionString) }
            .retryWhen(userTokenRefresher)
            .compose(ErrorHandlerTransformer())
            .map { it.map { response -> AuthorMapper.map(response) } }
}
