package io.github.feelfreelinux.wykopmobilny.di.modules

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesRepository
import io.github.feelfreelinux.wykopmobilny.api.mywykop.MyWykopApi
import io.github.feelfreelinux.wykopmobilny.api.mywykop.MyWykopRepository
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
    fun provideEntriesApi(retrofit: Retrofit, apiPreferences: CredentialsPreferencesApi) : EntriesApi = EntriesRepository(retrofit, apiPreferences)

    @Provides
    @Singleton
    fun provideMyWykopApi(retrofit: Retrofit, apiPreferences: CredentialsPreferencesApi) : MyWykopApi = MyWykopRepository(retrofit, apiPreferences)

    @Provides
    @Singleton
    fun provideStreamApi(retrofit: Retrofit, apiPreferences: CredentialsPreferencesApi) : StreamApi = StreamRepository(retrofit, apiPreferences)

    @Provides
    @Singleton
    fun proviteTagApi(retrofit: Retrofit, apiPreferences: CredentialsPreferencesApi) : TagApi = TagRepository(retrofit, apiPreferences)

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit, apiPreferences: CredentialsPreferencesApi) : UserApi = UserRepository(retrofit, apiPreferences)
}