package io.github.wykopmobilny.api.scraper

import io.github.wykopmobilny.models.scraper.Blacklist
import io.reactivex.Single
import retrofit2.Retrofit

class ScraperRepository(val retrofit: Retrofit) : ScraperApi {

    private val scraperApi by lazy { retrofit.create(ScraperRetrofitApi::class.java) }

    override fun getBlacklist(): Single<Blacklist> =
        scraperApi.getBlacklist()
}
