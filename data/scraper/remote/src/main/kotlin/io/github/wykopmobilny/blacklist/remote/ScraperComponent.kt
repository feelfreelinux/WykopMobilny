package io.github.wykopmobilny.blacklist.remote

import dagger.BindsInstance
import dagger.Component
import io.github.wykopmobilny.blacklist.api.ScraperRetrofitApi
import okhttp3.OkHttpClient

@Component(modules = [ScraperModule::class])
interface ScraperComponent {

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance okHttpClient: OkHttpClient,
            @BindsInstance baseUrl: String,
            @BindsInstance cookieProvider: (webPage: String) -> String
        ): ScraperComponent
    }

    fun scraper(): ScraperRetrofitApi
}
