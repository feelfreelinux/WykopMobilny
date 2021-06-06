package io.github.wykopmobilny.di.modules

import dagger.Module
import dagger.Provides
import io.github.wykopmobilny.api.ScraperInterceptor
import io.github.wykopmobilny.api.addlink.AddLinkApi
import io.github.wykopmobilny.api.addlink.AddLinkRepository
import io.github.wykopmobilny.api.embed.ExternalApi
import io.github.wykopmobilny.api.embed.ExternalRepository
import io.github.wykopmobilny.api.entries.EntriesApi
import io.github.wykopmobilny.api.entries.EntriesRepository
import io.github.wykopmobilny.api.hits.HitsApi
import io.github.wykopmobilny.api.hits.HitsRepository
import io.github.wykopmobilny.api.links.LinksApi
import io.github.wykopmobilny.api.links.LinksRepository
import io.github.wykopmobilny.api.mywykop.MyWykopApi
import io.github.wykopmobilny.api.mywykop.MyWykopRepository
import io.github.wykopmobilny.api.notifications.NotificationsApi
import io.github.wykopmobilny.api.notifications.NotificationsRepository
import io.github.wykopmobilny.api.patrons.PatronsApi
import io.github.wykopmobilny.api.patrons.PatronsRepository
import io.github.wykopmobilny.api.pm.PMApi
import io.github.wykopmobilny.api.pm.PMRepository
import io.github.wykopmobilny.api.profile.ProfileApi
import io.github.wykopmobilny.api.profile.ProfileRepository
import io.github.wykopmobilny.api.scraper.ScraperApi
import io.github.wykopmobilny.api.scraper.ScraperRepository
import io.github.wykopmobilny.api.search.SearchApi
import io.github.wykopmobilny.api.search.SearchRepository
import io.github.wykopmobilny.api.suggest.SuggestApi
import io.github.wykopmobilny.api.suggest.SuggestRepository
import io.github.wykopmobilny.api.tag.TagApi
import io.github.wykopmobilny.api.tag.TagRepository
import io.github.wykopmobilny.api.user.LoginApi
import io.github.wykopmobilny.api.user.LoginRepository
import okhttp3.OkHttpClient
import pl.droidsonroids.retrofit2.JspoonConverterFactory
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    fun EntriesRepository.provideEntriesApi(): EntriesApi = this

    @Provides
    fun NotificationsRepository.notifications(): NotificationsApi = this

    @Provides
    fun MyWykopRepository.myWykop(): MyWykopApi = this

    @Provides
    fun LinksRepository.links(): LinksApi = this

    @Provides
    fun TagRepository.tag(): TagApi = this

    @Provides
    fun LoginRepository.login(): LoginApi = this

    @Provides
    fun PMRepository.pm(): PMApi = this

    @Provides
    fun SuggestRepository.suggest(): SuggestApi = this

    @Provides
    fun SearchRepository.search(): SearchApi = this

    @Provides
    fun HitsRepository.hits(): HitsApi = this

    @Provides
    fun ProfileRepository.profile(): ProfileApi = this

    @Provides
    fun ExternalRepository.external(): ExternalApi = this

    @Provides
    fun AddLinkRepository.addLink(): AddLinkApi = this

    @Provides
    fun PatronsRepository.patrons(patronsApi: PatronsRepository): PatronsApi = this

    @Provides
    @Singleton
    fun provideScraperApi(okHttpClient: OkHttpClient): ScraperApi = ScraperRepository(createScraperRetrofit(okHttpClient))

    private fun createScraperRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val client = okHttpClient.newBuilder().addInterceptor(ScraperInterceptor()).build()
        return Retrofit.Builder()
            .baseUrl("https://wykop.pl")
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(JspoonConverterFactory.create())
            .build()
    }
}
