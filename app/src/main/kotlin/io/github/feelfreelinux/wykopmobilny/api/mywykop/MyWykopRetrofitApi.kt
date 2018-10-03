package io.github.feelfreelinux.wykopmobilny.api.mywykop

import io.github.feelfreelinux.wykopmobilny.APP_KEY
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.common.WykopApiResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.EntryLinkResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface MyWykopRetrofitApi {
    @GET("/mywykop/index/page/{page}/appkey/$APP_KEY")
    fun getIndex(@Path("page") page: Int): Single<WykopApiResponse<List<EntryLinkResponse>>>

    @GET("/mywykop/users/page/{page}/appkey/$APP_KEY")
    fun byUsers(@Path("page") page: Int): Single<WykopApiResponse<List<EntryLinkResponse>>>

    @GET("/mywykop/tags/page/{page}/appkey/$APP_KEY")
    fun byTags(@Path("page") page: Int): Single<WykopApiResponse<List<EntryLinkResponse>>>
}