package io.github.wykopmobilny.api.endpoints

import io.github.wykopmobilny.APP_KEY
import io.github.wykopmobilny.api.responses.LinkResponse
import io.github.wykopmobilny.api.responses.WykopApiResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface HitsRetrofitApi {

    @GET("/hits/week/appkey/$APP_KEY")
    suspend fun currentWeek(): WykopApiResponse<List<LinkResponse>>

    @GET("/hits/day/appkey/$APP_KEY")
    suspend fun currentDay(): WykopApiResponse<List<LinkResponse>>

    @GET("/hits/month/{year}/{month}/appkey/$APP_KEY")
    suspend fun byMonth(
        @Path("year") year: Int,
        @Path("month") month: Int
    ): WykopApiResponse<List<LinkResponse>>

    @GET("/hits/year/{year}/appkey/$APP_KEY")
    suspend fun byYear(@Path("year") year: Int): WykopApiResponse<List<LinkResponse>>

    @GET("/hits/popular/appkey/$APP_KEY")
    suspend fun popular(): WykopApiResponse<List<LinkResponse>>
}
