package io.github.feelfreelinux.wykopmobilny.api.hits

import io.github.feelfreelinux.wykopmobilny.APP_KEY
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.common.WykopApiResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.LinkResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface HitsRetrofitApi {
    @GET("/hits/week/appkey/$APP_KEY")
    fun currentWeek(): Single<WykopApiResponse<List<LinkResponse>>>

    @GET("/hits/day/appkey/$APP_KEY")
    fun currentDay(): Single<WykopApiResponse<List<LinkResponse>>>

    @GET("/hits/month/{year}/{month}/appkey/$APP_KEY")
    fun byMonth(@Path("year") year : Int,
            @Path("month") month : Int): Single<WykopApiResponse<List<LinkResponse>>>

    @GET("/hits/year/{year}/appkey/$APP_KEY")
    fun byYear(@Path("year") year : Int): Single<WykopApiResponse<List<LinkResponse>>>

    @GET("/hits/popular/appkey/$APP_KEY")
    fun popular() : Single<WykopApiResponse<List<LinkResponse>>>

}