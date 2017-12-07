package io.github.feelfreelinux.wykopmobilny.di.modules

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesRepository
import io.github.feelfreelinux.wykopmobilny.api.links.LinksApi
import io.github.feelfreelinux.wykopmobilny.api.links.LinksRepository
import io.github.feelfreelinux.wykopmobilny.api.links.LinksRetrofitApi
import io.github.feelfreelinux.wykopmobilny.api.mywykop.MyWykopApi
import io.github.feelfreelinux.wykopmobilny.api.mywykop.MyWykopRepository
import io.github.feelfreelinux.wykopmobilny.api.notifications.NotificationsApi
import io.github.feelfreelinux.wykopmobilny.api.notifications.NotificationsRepository
import io.github.feelfreelinux.wykopmobilny.api.pm.PMApi
import io.github.feelfreelinux.wykopmobilny.api.pm.PMRepository
import io.github.feelfreelinux.wykopmobilny.api.suggest.SuggestApi
import io.github.feelfreelinux.wykopmobilny.api.suggest.SuggestRepository
import io.github.feelfreelinux.wykopmobilny.api.tag.TagApi
import io.github.feelfreelinux.wykopmobilny.api.tag.TagRepository
import io.github.feelfreelinux.wykopmobilny.api.user.LoginApi
import io.github.feelfreelinux.wykopmobilny.api.user.LoginRepository
import io.github.feelfreelinux.wykopmobilny.utils.api.CredentialsPreferencesApi
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Provides
    @Singleton
    fun provideEntriesApi(retrofit: Retrofit) : EntriesApi = EntriesRepository(retrofit)

    @Provides
    @Singleton
    fun provideNotificationsApi(retrofit: Retrofit) : NotificationsApi = NotificationsRepository(retrofit)

    @Provides
    @Singleton
    fun provideMyWykopApi(retrofit: Retrofit) : MyWykopApi = MyWykopRepository(retrofit)

    @Provides
    @Singleton
    fun provideLinksApi(retrofit: Retrofit) : LinksApi = LinksRepository(retrofit)

    @Provides
    @Singleton
    fun provideTagApi(retrofit: Retrofit) : TagApi = TagRepository(retrofit)

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit, credentialsPreferencesApi : CredentialsPreferencesApi) : LoginApi
            = LoginRepository(retrofit, credentialsPreferencesApi)

    @Provides
    @Singleton
    fun providePMApi(retrofit: Retrofit) : PMApi = PMRepository(retrofit)

    @Provides
    @Singleton
    fun provideSuggestApi(retrofit: Retrofit) : SuggestApi = SuggestRepository(retrofit)
}