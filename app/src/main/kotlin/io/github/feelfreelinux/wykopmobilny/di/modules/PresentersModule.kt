package io.github.feelfreelinux.wykopmobilny.di.modules

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.api.mywykop.MyWykopApi
import io.github.feelfreelinux.wykopmobilny.api.stream.StreamApi
import io.github.feelfreelinux.wykopmobilny.api.tag.TagApi
import io.github.feelfreelinux.wykopmobilny.api.user.UserApi
import io.github.feelfreelinux.wykopmobilny.ui.add_user_input.AddUserInputPresenter
import io.github.feelfreelinux.wykopmobilny.ui.loginscreen.LoginScreenPresenter
import io.github.feelfreelinux.wykopmobilny.ui.mainnavigation.MainNavigationPresenter
import io.github.feelfreelinux.wykopmobilny.ui.mikroblog.entry.EntryPresenter
import io.github.feelfreelinux.wykopmobilny.ui.mikroblog.feed.hot.HotPresenter
import io.github.feelfreelinux.wykopmobilny.ui.mikroblog.feed.tag.TagPresenter
import io.github.feelfreelinux.wykopmobilny.ui.splashscreen.SplashScreenPresenter
import io.github.feelfreelinux.wykopmobilny.utils.api.CredentialsPreferencesApi

@Module
class PresentersModule {
    @Provides
    fun provideLoginPresenter(preferences: CredentialsPreferencesApi) = LoginScreenPresenter(preferences)

    @Provides
    fun provideSplashScreenPresenter(preferences: CredentialsPreferencesApi, userApi: UserApi) = SplashScreenPresenter(preferences, userApi)

    @Provides
    fun provideMainNavigationPresenter(preferences: CredentialsPreferencesApi, myWykopApi: MyWykopApi) = MainNavigationPresenter(preferences, myWykopApi)

    @Provides
    fun provideHotPresenter(streamApi: StreamApi) = HotPresenter(streamApi)

    @Provides
    fun provideTagPresenter(tagApi: TagApi) = TagPresenter(tagApi)

    @Provides
    fun provideEntryPresenter(entriesApi: EntriesApi) = EntryPresenter(entriesApi)

    @Provides
    fun provideAddUserInputPresenter(entriesApi: EntriesApi) = AddUserInputPresenter(entriesApi)
}