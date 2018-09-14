package io.github.feelfreelinux.wykopmobilny.api.suggest

import io.github.feelfreelinux.wykopmobilny.APP_KEY
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.common.WykopApiResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.AuthorResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.TagSuggestionResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface SuggestRetrofitApi {
    @GET("/suggest/tags/{suggestString}/appkey/$APP_KEY")
    fun getTagSuggestions(@Path("suggestString") suggestString: String): Single<WykopApiResponse<List<TagSuggestionResponse>>>

    @GET("/suggest/users/{suggestString}/appkey/$APP_KEY")
    fun getUsersSuggestions(@Path("suggestString") suggestString: String): Single<WykopApiResponse<List<AuthorResponse>>>
}