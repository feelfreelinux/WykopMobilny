package io.github.wykopmobilny.api.scraper

import io.github.wykopmobilny.models.scraper.Blacklist
import io.reactivex.Single
import retrofit2.http.GET

interface ScraperRetrofitApi {
    @GET("https://www.wykop.pl/ustawienia/czarne-listy/")
    fun getBlacklist(): Single<Blacklist>
}
