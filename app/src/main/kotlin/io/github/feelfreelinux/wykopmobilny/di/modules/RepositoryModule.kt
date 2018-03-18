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
import io.github.feelfreelinux.wykopmobilny.api.search.SearchApi
import io.github.feelfreelinux.wykopmobilny.api.search.SearchRepository
import io.github.feelfreelinux.wykopmobilny.api.suggest.SuggestApi
import io.github.feelfreelinux.wykopmobilny.api.suggest.SuggestRepository
import io.github.feelfreelinux.wykopmobilny.api.tag.TagApi
import io.github.feelfreelinux.wykopmobilny.api.tag.TagRepository
import io.github.feelfreelinux.wykopmobilny.api.user.LoginApi
import io.github.feelfreelinux.wykopmobilny.api.user.LoginRepository
import io.github.feelfreelinux.wykopmobilny.utils.LinksPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.api.CredentialsPreferencesApi
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Provides
    @Singleton
    fun provideEntriesApi(retrofit: Retrofit, userTokenRefresher: UserTokenRefresher) : EntriesApi = EntriesRepository(retrofit, userTokenRefresher)

    @Provides
    fun provideNotificationsApi(retrofit: Retrofit, userTokenRefresher: UserTokenRefresher) : NotificationsApi = NotificationsRepository(retrofit, userTokenRefresher)

    @Provides
    fun provideMyWykopApi(retrofit: Retrofit, userTokenRefresher: UserTokenRefresher, linksPreferencesApi: LinksPreferencesApi) : MyWykopApi = MyWykopRepository(retrofit, userTokenRefresher, linksPreferencesApi)

    @Provides
    @Singleton
    fun provideLinksApi(retrofit: Retrofit, userTokenRefresher: UserTokenRefresher, linksPreferencesApi: LinksPreferencesApi) : LinksApi = LinksRepository(retrofit, userTokenRefresher, linksPreferencesApi)

    @Provides
    fun provideTagApi(retrofit: Retrofit, userTokenRefresher: UserTokenRefresher, linksPreferencesApi: LinksPreferencesApi) : TagApi = TagRepository(retrofit, userTokenRefresher, linksPreferencesApi)

    @Provides
    fun provideUserApi(retrofit: Retrofit, credentialsPreferencesApi : CredentialsPreferencesApi) : LoginApi
            = LoginRepository(retrofit, credentialsPreferencesApi)

    @Provides
    fun providePMApi(retrofit: Retrofit, userTokenRefresher: UserTokenRefresher) : PMApi = PMRepository(retrofit, userTokenRefresher)

    @Provides
    fun provideSuggestApi(retrofit: Retrofit, userTokenRefresher: UserTokenRefresher) : SuggestApi = SuggestRepository(retrofit, userTokenRefresher)

    @Provides
    fun provideSearchApi(retrofit: Retrofit, userTokenRefresher: UserTokenRefresher, linksPreferencesApi: LinksPreferencesApi) : SearchApi = SearchRepository(retrofit, userTokenRefresher, linksPreferencesApi)

    @Provides
    fun provideHitsApi(retrofit: Retrofit, userTokenRefresher: UserTokenRefresher, linksPreferencesApi: LinksPreferencesApi) : HitsApi = HitsRepository(retrofit, userTokenRefresher, linksPreferencesApi)

    @Provides
    fun provideProfilesApi(retrofit: Retrofit, userTokenRefresher: UserTokenRefresher, linksPreferencesApi: LinksPreferencesApi) : ProfileApi = ProfileRepository(retrofit, userTokenRefresher, linksPreferencesApi)

    @Provides
    fun provideEmbedApi(retrofit: Retrofit) : ExternalApi = ExternalRepository(retrofit)
}