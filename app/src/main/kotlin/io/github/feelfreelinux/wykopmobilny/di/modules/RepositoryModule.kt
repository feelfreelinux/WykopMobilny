package io.github.feelfreelinux.wykopmobilny.di.modules

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.UserTokenRefresher
import io.github.feelfreelinux.wykopmobilny.api.embed.ExternalApi
import io.github.feelfreelinux.wykopmobilny.api.embed.ExternalRepository
import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesRepository
import io.github.feelfreelinux.wykopmobilny.api.hits.HitsApi
import io.github.feelfreelinux.wykopmobilny.api.hits.HitsRepository
import io.github.feelfreelinux.wykopmobilny.api.links.LinksApi
import io.github.feelfreelinux.wykopmobilny.api.links.LinksRepository
import io.github.feelfreelinux.wykopmobilny.api.mywykop.MyWykopApi
import io.github.feelfreelinux.wykopmobilny.api.mywykop.MyWykopRepository
import io.github.feelfreelinux.wykopmobilny.api.notifications.NotificationsApi
import io.github.feelfreelinux.wykopmobilny.api.notifications.NotificationsRepository
import io.github.feelfreelinux.wykopmobilny.api.pm.PMApi
import io.github.feelfreelinux.wykopmobilny.api.pm.PMRepository
import io.github.feelfreelinux.wykopmobilny.api.profile.ProfileApi
import io.github.feelfreelinux.wykopmobilny.api.profile.ProfileRepository
import io.github.feelfreelinux.wykopmobilny.api.profile.ProfileRetrofitApi
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
import io.github.feelfreelinux.wykopmobilny.utils.preferences.LinksPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.api.CredentialsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.preferences.BlacklistPreferences
import io.github.feelfreelinux.wykopmobilny.utils.preferences.BlacklistPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi
import pl.droidsonroids.retrofit2.JspoonConverterFactory
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Provides
    @Singleton
    fun provideEntriesApi(retrofit: Retrofit, userTokenRefresher: UserTokenRefresher, blacklistPreferencesApi: BlacklistPreferencesApi, settingsPreferencesApi: SettingsPreferencesApi) : EntriesApi = EntriesRepository(retrofit, userTokenRefresher, blacklistPreferencesApi, settingsPreferencesApi)

    @Provides
    fun provideNotificationsApi(retrofit: Retrofit, userTokenRefresher: UserTokenRefresher) : NotificationsApi = NotificationsRepository(retrofit, userTokenRefresher)

    @Provides
    fun provideMyWykopApi(retrofit: Retrofit, userTokenRefresher: UserTokenRefresher, linksPreferencesApi: LinksPreferencesApi, blacklistPreferencesApi: BlacklistPreferencesApi, settingsPreferencesApi: SettingsPreferencesApi) : MyWykopApi = MyWykopRepository(retrofit, userTokenRefresher, linksPreferencesApi, blacklistPreferencesApi, settingsPreferencesApi)

    @Provides
    @Singleton
    fun provideLinksApi(retrofit: Retrofit, userTokenRefresher: UserTokenRefresher, linksPreferencesApi: LinksPreferencesApi, blacklistPreferencesApi: BlacklistPreferencesApi, settingsPreferencesApi: SettingsPreferencesApi) : LinksApi = LinksRepository(retrofit, userTokenRefresher, linksPreferencesApi, blacklistPreferencesApi, settingsPreferencesApi)

    @Provides
    fun provideTagApi(retrofit: Retrofit, userTokenRefresher: UserTokenRefresher, linksPreferencesApi: LinksPreferencesApi, blacklistPreferencesApi: BlacklistPreferencesApi, settingsPreferencesApi: SettingsPreferencesApi) : TagApi = TagRepository(retrofit, userTokenRefresher, linksPreferencesApi, blacklistPreferencesApi, settingsPreferencesApi)

    @Provides
    fun provideUserApi(retrofit: Retrofit, credentialsPreferencesApi : CredentialsPreferencesApi) : LoginApi
            = LoginRepository(retrofit, credentialsPreferencesApi)

    @Provides
    fun providePMApi(retrofit: Retrofit, userTokenRefresher: UserTokenRefresher) : PMApi = PMRepository(retrofit, userTokenRefresher)

    @Provides
    fun provideSuggestApi(retrofit: Retrofit, userTokenRefresher: UserTokenRefresher) : SuggestApi = SuggestRepository(retrofit, userTokenRefresher)

    @Provides
    fun provideSearchApi(retrofit: Retrofit, userTokenRefresher: UserTokenRefresher, linksPreferencesApi: LinksPreferencesApi, blacklistPreferencesApi: BlacklistPreferencesApi, settingsPreferencesApi: SettingsPreferencesApi) : SearchApi = SearchRepository(retrofit, userTokenRefresher, linksPreferencesApi, blacklistPreferencesApi, settingsPreferencesApi)

    @Provides
    fun provideHitsApi(retrofit: Retrofit, userTokenRefresher: UserTokenRefresher, linksPreferencesApi: LinksPreferencesApi, blacklistPreferencesApi: BlacklistPreferencesApi, settingsPreferencesApi: SettingsPreferencesApi) : HitsApi = HitsRepository(retrofit, userTokenRefresher, linksPreferencesApi, blacklistPreferencesApi, settingsPreferencesApi)

    @Provides
    fun provideProfilesApi(retrofit: Retrofit, userTokenRefresher: UserTokenRefresher, linksPreferencesApi: LinksPreferencesApi, blacklistPreferences: BlacklistPreferencesApi, settingsPreferencesApi: SettingsPreferencesApi) : ProfileApi = ProfileRepository(retrofit, userTokenRefresher, linksPreferencesApi, blacklistPreferences, settingsPreferencesApi)

    @Provides
    fun provideEmbedApi(retrofit: Retrofit) : ExternalApi = ExternalRepository(retrofit)

    @Provides
    fun provideScraperApi() : ScraperApi = ScraperRepository(createScraperRetrofit())

    fun createScraperRetrofit() : Retrofit {
        return Retrofit.Builder()
                .baseUrl("https://wykop.pl")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(JspoonConverterFactory.create())
                .build()
    }
}