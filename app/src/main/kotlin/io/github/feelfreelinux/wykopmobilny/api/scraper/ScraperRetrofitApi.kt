package io.github.feelfreelinux.wykopmobilny.api.scraper

import io.github.feelfreelinux.wykopmobilny.APP_KEY
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.common.WykopApiResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.*
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.responses.BadgeResponse
import io.github.feelfreelinux.wykopmobilny.models.scraper.Blacklist
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ScraperRetrofitApi {
    @GET("https://www.wykop.pl/ustawienia/czarne-listy/")
    fun getBlacklist(@Header("Cookie") session : String) : Single<Blacklist>
}