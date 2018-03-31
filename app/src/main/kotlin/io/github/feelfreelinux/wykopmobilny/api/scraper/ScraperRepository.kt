package io.github.feelfreelinux.wykopmobilny.api.scraper

import io.github.feelfreelinux.wykopmobilny.models.scraper.Blacklist
import io.reactivex.Single
import retrofit2.Retrofit

class ScraperRepository(val retrofit: Retrofit) : ScraperApi {
    private val scraperApi by lazy { retrofit.create(ScraperRetrofitApi::class.java) }

    override fun getBlacklist(session: String): Single<Blacklist> =
            scraperApi.getBlacklist(session)
}