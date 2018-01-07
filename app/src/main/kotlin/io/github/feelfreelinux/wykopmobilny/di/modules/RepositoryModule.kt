package io.github.feelfreelinux.wykopmobilny.di.modules

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesRepository
import io.github.feelfreelinux.wykopmobilny.api.links.LinksApi
import io.github.feelfreelinux.wykopmobilny.api.links.LinksRepository
import io.github.feelfreelinux.wykopmobilny.api.mywykop.MyWykopApi
import io.github.feelfreelinux.wykopmobilny.api.mywykop.MyWykopRepository
import io.github.feelfreelinux.wykopmobilny.api.notifications.NotificationsApi
import io.github.feelfreelinux.wykopmobilny.api.notifications.NotificationsRepository
import io.github.feelfreelinux.wykopmobilny.api.pm.PMApi
import io.github.feelfreelinux.wykopmobilny.api.pm.PMRepository
import io.github.feelfreelinux.wykopmobilny.api.search.SearchApi
import io.github.feelfreelinux.wykopmobilny.api.search.SearchRepository
import io.github.feelfreelinux.wykopmobilny.api.suggest.SuggestApi
import io.github.feelfreelinux.wykopmobilny.api.suggest.SuggestRepository
import io.github.feelfreelinux.wykopmobilny.api.tag.TagApi
import io.github.feelfreelinux.wykopmobilny.api.tag.TagRepository
import io.github.feelfreelinux.wykopmobilny.api.user.LoginApi
import io.github.feelfreelinux.wykopmobilny.api.user.LoginRepository
import io.github.feelfreelinux.wykopmobilny.utils.api.CredentialsPreferencesApi
import retrofit2.Retrofit

@Module
class RepositoryModule {
    @Provides
    fun provideEntriesApi(retrofit: Retrofit) : EntriesApi = EntriesRepository(retrofit)

    @Provides
    fun provideNotificationsApi(retrofit: Retrofit) : NotificationsApi = NotificationsRepository(retrofit)

    @Provides
    fun provideMyWykopApi(retrofit: Retrofit) : MyWykopApi = MyWykopRepository(retrofit)

    @Provides
    fun provideLinksApi(retrofit: Retrofit) : LinksApi = LinksRepository(retrofit)

    @Provides
    fun provideTagApi(retrofit: Retrofit) : TagApi = TagRepository(retrofit)

    @Provides
    fun provideUserApi(retrofit: Retrofit, credentialsPreferencesApi : CredentialsPreferencesApi) : LoginApi
            = LoginRepository(retrofit, credentialsPreferencesApi)

    @Provides
    fun providePMApi(retrofit: Retrofit) : PMApi = PMRepository(retrofit)

    @Provides
    fun provideSuggestApi(retrofit: Retrofit) : SuggestApi = SuggestRepository(retrofit)

    @Provides
    fun provideSearchApi(retrofit: Retrofit) : SearchApi = SearchRepository(retrofit)
}