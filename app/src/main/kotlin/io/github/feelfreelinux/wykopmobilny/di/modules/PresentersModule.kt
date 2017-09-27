package io.github.feelfreelinux.wykopmobilny.di.modules

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.api.mywykop.MyWykopApi
import io.github.feelfreelinux.wykopmobilny.api.stream.StreamApi
import io.github.feelfreelinux.wykopmobilny.api.tag.TagApi
import io.github.feelfreelinux.wykopmobilny.api.user.UserApi
import io.github.feelfreelinux.wykopmobilny.ui.input.entry.add.AddEntryPresenter
import io.github.feelfreelinux.wykopmobilny.ui.input.entry.comment.add.AddEntryCommentPresenter
import io.github.feelfreelinux.wykopmobilny.ui.loginscreen.LoginScreenPresenter
import io.github.feelfreelinux.wykopmobilny.ui.mainnavigation.MainNavigationPresenter
import io.github.feelfreelinux.wykopmobilny.ui.mikroblog.entry.EntryDetailPresenter
import io.github.feelfreelinux.wykopmobilny.ui.mikroblog.feed.hot.HotPresenter
import io.github.feelfreelinux.wykopmobilny.ui.mikroblog.feed.tag.TagPresenter
import io.github.feelfreelinux.wykopmobilny.ui.splashscreen.SplashScreenPresenter
import io.github.feelfreelinux.wykopmobilny.utils.api.CredentialsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.rx.SubscriptionHelperApi

@Module
class PresentersModule {
    @Provides
    fun provideLoginPresenter(preferences: CredentialsPreferencesApi) = LoginScreenPresenter(preferences)

    @Provides
    fun provideSplashScreenPresenter(subscriptionHelperApi: SubscriptionHelperApi, preferences: CredentialsPreferencesApi, userApi: UserApi)
            = SplashScreenPresenter(subscriptionHelperApi, preferences, userApi)

    @Provides
    fun provideMainNavigationPresenter(subscriptionHelperApi: SubscriptionHelperApi, preferences: CredentialsPreferencesApi, myWykopApi: MyWykopApi)
            = MainNavigationPresenter(subscriptionHelperApi, preferences, myWykopApi)

    @Provides
    fun provideHotPresenter(subscriptionHelperApi: SubscriptionHelperApi, streamApi: StreamApi)
            = HotPresenter(subscriptionHelperApi, streamApi)

    @Provides
    fun provideTagPresenter(subscriptionHelperApi: SubscriptionHelperApi, tagApi: TagApi)
            = TagPresenter(subscriptionHelperApi, tagApi)

    @Provides
    fun provideEntryDetailPresenter(subscriptionHelperApi: SubscriptionHelperApi, entriesApi: EntriesApi)
            = EntryDetailPresenter(subscriptionHelperApi, entriesApi)

    @Provides
    fun provideAddEntryPresenter(subscriptionHelperApi: SubscriptionHelperApi, entriesApi: EntriesApi)
            = AddEntryPresenter(subscriptionHelperApi, entriesApi)

    @Provides
    fun provideAddEntryCommentPresenter(subscriptionHelperApi: SubscriptionHelperApi, entriesApi: EntriesApi)
            = AddEntryCommentPresenter(subscriptionHelperApi, entriesApi)
}