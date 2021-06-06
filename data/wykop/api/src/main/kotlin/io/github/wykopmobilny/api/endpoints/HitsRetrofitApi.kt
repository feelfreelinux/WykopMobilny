package io.github.wykopmobilny.api.endpoints

import io.github.wykopmobilny.APP_KEY
import io.github.wykopmobilny.api.responses.WykopApiResponse
import io.github.wykopmobilny.api.responses.LinkResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface HitsRetrofitApi {

    @GET("/hits/week/appkey/$APP_KEY")
    fun currentWeek(): Single<WykopApiResponse<List<LinkResponse>>>

    @GET("/hits/day/appkey/$APP_KEY")
    fun currentDay(): Single<WykopApiResponse<List<LinkResponse>>>

    @GET("/hits/month/{year}/{month}/appkey/$APP_KEY")
    fun byMonth(
        @Path("year") year: Int,
        @Path("month") month: Int
    ): Single<WykopApiResponse<List<LinkResponse>>>

    @GET("/hits/year/{year}/appkey/$APP_KEY")
    fun byYear(@Path("year") year: Int): Single<WykopApiResponse<List<LinkResponse>>>

    @GET("/hits/popular/appkey/$APP_KEY")
    fun popular(): Single<WykopApiResponse<List<LinkResponse>>>
}
