package io.github.feelfreelinux.wykopmobilny.api.search

import io.github.feelfreelinux.wykopmobilny.APP_KEY
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.common.WykopApiResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.AuthorResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.EntryResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.LinkResponse
import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Path

interface SearchRetrofitApi {
    @FormUrlEncoded
    @POST("/search/links/page/{page}/appkey/$APP_KEY")
    fun searchLinks(
        @Path("page") page: Int,
        @Field("q") query: String
    ): Single<WykopApiResponse<List<LinkResponse>>>

    @FormUrlEncoded
    @POST("/search/entries/page/{page}/appkey/$APP_KEY")
    fun searchEntries(
        @Path("page") page: Int,
        @Field("q") query: String
    ): Single<WykopApiResponse<List<EntryResponse>>>

    @FormUrlEncoded
    @POST("/search/profiles/appkey/$APP_KEY")
    fun searchProfiles(@Field("q") query: String): Single<WykopApiResponse<List<AuthorResponse>>>
}
