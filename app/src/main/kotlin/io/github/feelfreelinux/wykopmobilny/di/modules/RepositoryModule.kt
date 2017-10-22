package io.github.feelfreelinux.wykopmobilny.di.modules

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesRepository
import io.github.feelfreelinux.wykopmobilny.api.mywykop.MyWykopApi
import io.github.feelfreelinux.wykopmobilny.api.mywykop.MyWykopRepository
import io.github.feelfreelinux.wykopmobilny.api.pm.PMApi
import io.github.feelfreelinux.wykopmobilny.api.pm.PMRepository
import io.github.feelfreelinux.wykopmobilny.api.pm.PMRetrofitApi
import io.github.feelfreelinux.wykopmobilny.api.stream.StreamApi
import io.github.feelfreelinux.wykopmobilny.api.stream.StreamRepository
import io.github.feelfreelinux.wykopmobilny.api.tag.TagApi
import io.github.feelfreelinux.wykopmobilny.api.tag.TagRepository
import io.github.feelfreelinux.wykopmobilny.api.user.UserApi
import io.github.feelfreelinux.wykopmobilny.api.user.UserRepository
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
    fun provideMyWykopApi(retrofit: Retrofit) : MyWykopApi = MyWykopRepository(retrofit)

    @Provides
    @Singleton
    fun provideStreamApi(retrofit: Retrofit) : StreamApi = StreamRepository(retrofit)

    @Provides
    @Singleton
    fun proviteTagApi(retrofit: Retrofit) : TagApi = TagRepository(retrofit)

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit, credentialsPreferencesApi : CredentialsPreferencesApi) : UserApi = UserRepository(retrofit, credentialsPreferencesApi)

    @Provides
    @Singleton
    fun providePMApi(retrofit: Retrofit) : PMApi = PMRepository(retrofit)
}