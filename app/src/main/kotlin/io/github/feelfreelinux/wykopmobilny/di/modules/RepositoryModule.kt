package io.github.feelfreelinux.wykopmobilny.di.modules

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.ScraperInterceptor
import io.github.feelfreelinux.wykopmobilny.api.UserTokenRefresher
import io.github.feelfreelinux.wykopmobilny.api.addlink.AddLinkApi
import io.github.feelfreelinux.wykopmobilny.api.addlink.AddLinkRepository
import io.github.feelfreelinux.wykopmobilny.api.embed.ExternalApi
import io.github.feelfreelinux.wykopmobilny.api.embed.ExternalRepository
import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesRepository
import io.github.feelfreelinux.wykopmobilny.api.filters.OWMContentFilter
import io.github.feelfreelinux.wykopmobilny.api.hits.HitsApi
import io.github.feelfreelinux.wykopmobilny.api.hits.HitsRepository
import io.github.feelfreelinux.wykopmobilny.api.links.LinksApi
import io.github.feelfreelinux.wykopmobilny.api.links.LinksRepository
import io.github.feelfreelinux.wykopmobilny.api.mywykop.MyWykopApi
import io.github.feelfreelinux.wykopmobilny.api.mywykop.MyWykopRepository
import io.github.feelfreelinux.wykopmobilny.api.notifications.NotificationsApi
import io.github.feelfreelinux.wykopmobilny.api.notifications.NotificationsRepository
import io.github.feelfreelinux.wykopmobilny.api.patrons.PatronsApi
import io.github.feelfreelinux.wykopmobilny.api.patrons.PatronsRepository
import io.github.feelfreelinux.wykopmobilny.api.pm.PMApi
import io.github.feelfreelinux.wykopmobilny.api.pm.PMRepository
import io.github.feelfreelinux.wykopmobilny.api.profile.ProfileApi
import io.github.feelfreelinux.wykopmobilny.api.profile.ProfileRepository
import io.github.feelfreelinux.wykopmobilny.api.scraper.ScraperApi
import io.github.feelfreelinux.wykopmobilny.api.scraper.ScraperRepository
import io.github.feelfreelinux.wykopmobilny.api.search.SearchApi
import io.github.feelfreelinux.wykopmobilny.api.search.SearchRepository
import io.github.feelfreelinux.wykopmobilny.api.suggest.SuggestApi
import io.github.feelfreelinux.wykopmobilny.api.suggest.SuggestRepository
import io.github.feelfreelinux.wykopmobilny.api.tag.TagApi
import io.github.feelfreelinux.wykopmobilny.api.tag.TagRepository
import io.github.feelfreelinux.wykopmobilny.api.user.LoginApi
import io.github.feelfreelinux.wykopmobilny.api.user.LoginRepository
import io.github.feelfreelinux.wykopmobilny.utils.api.CredentialsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.preferences.BlacklistPreferencesApi
import okhttp3.OkHttpClient
import pl.droidsonroids.retrofit2.JspoonConverterFactory
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideEntriesApi(
        retrofit: Retrofit,
        userTokenRefresher: UserTokenRefresher,
        owmContentFilter: OWMContentFilter,
        patronsApi: PatronsApi
    ): EntriesApi = EntriesRepository(retrofit, userTokenRefresher, owmContentFilter, patronsApi)

    @Provides
    fun provideNotificationsApi(
        retrofit: Retrofit,
        userTokenRefresher: UserTokenRefresher
    ): NotificationsApi = NotificationsRepository(retrofit, userTokenRefresher)

    @Provides
    fun provideMyWykopApi(
        retrofit: Retrofit,
        userTokenRefresher: UserTokenRefresher,
        owmContentFilter: OWMContentFilter,
        patronsApi: PatronsApi
    ): MyWykopApi = MyWykopRepository(retrofit, userTokenRefresher, owmContentFilter, patronsApi)

    @Provides
    @Singleton
    fun provideLinksApi(
        retrofit: Retrofit,
        userTokenRefresher: UserTokenRefresher,
        owmContentFilter: OWMContentFilter,
        patronsApi: PatronsApi
    ): LinksApi = LinksRepository(retrofit, userTokenRefresher, owmContentFilter, patronsApi)

    @Provides
    fun provideTagApi(
        retrofit: Retrofit,
        userTokenRefresher: UserTokenRefresher,
        owmContentFilter: OWMContentFilter,
        blacklistPreferences: BlacklistPreferencesApi
    ): TagApi = TagRepository(
        retrofit,
        userTokenRefresher,
        owmContentFilter,
        blacklistPreferences
    )

    @Provides
    @Singleton
    fun providePatronsApi(retrofit: Retrofit): PatronsApi = PatronsRepository(retrofit)

    @Provides
    fun provideUserApi(
        retrofit: Retrofit,
        credentialsPreferencesApi: CredentialsPreferencesApi
    ): LoginApi = LoginRepository(retrofit, credentialsPreferencesApi)

    @Provides
    fun providePMApi(
        retrofit: Retrofit,
        userTokenRefresher: UserTokenRefresher,
        patronsApi: PatronsApi
    ): PMApi = PMRepository(retrofit, userTokenRefresher, patronsApi)

    @Provides
    fun provideSuggestApi(
        retrofit: Retrofit,
        userTokenRefresher: UserTokenRefresher
    ): SuggestApi = SuggestRepository(retrofit, userTokenRefresher)

    @Provides
    fun provideSearchApi(
        retrofit: Retrofit,
        userTokenRefresher: UserTokenRefresher,
        owmContentFilter: OWMContentFilter,
        patronsApi: PatronsApi
    ): SearchApi = SearchRepository(retrofit, userTokenRefresher, owmContentFilter, patronsApi)

    @Provides
    fun provideHitsApi(
        retrofit: Retrofit,
        userTokenRefresher: UserTokenRefresher,
        owmContentFilter: OWMContentFilter,
        patronsApi: PatronsApi
    ): HitsApi = HitsRepository(retrofit, userTokenRefresher, owmContentFilter, patronsApi)

    @Provides
    fun provideProfilesApi(
        retrofit: Retrofit,
        userTokenRefresher: UserTokenRefresher,
        blacklistPreferences: BlacklistPreferencesApi,
        owmContentFilter: OWMContentFilter
    ): ProfileApi = ProfileRepository(retrofit, userTokenRefresher, owmContentFilter, blacklistPreferences)

    @Provides
    fun provideEmbedApi(retrofit: Retrofit): ExternalApi = ExternalRepository(retrofit)

    @Provides
    fun provideAddlinkApi(retrofit: Retrofit, userTokenRefresher: UserTokenRefresher, owmContentFilter: OWMContentFilter): AddLinkApi =
        AddLinkRepository(retrofit, userTokenRefresher, owmContentFilter)

    @Provides
    fun provideScraperApi(): ScraperApi = ScraperRepository(createScraperRetrofit())

    private fun createScraperRetrofit(): Retrofit {
        val client = OkHttpClient.Builder().addInterceptor(ScraperInterceptor()).build()
        return Retrofit.Builder()
            .baseUrl("https://wykop.pl")
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(JspoonConverterFactory.create())
            .build()
    }
}
