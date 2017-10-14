package io.github.feelfreelinux.wykopmobilny.di.modules

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.api.mywykop.MyWykopApi
import io.github.feelfreelinux.wykopmobilny.api.stream.StreamApi
import io.github.feelfreelinux.wykopmobilny.api.tag.TagApi
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.entry.add.AddEntryPresenter
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.entry.edit.EditEntryPresenter
import io.github.feelfreelinux.wykopmobilny.ui.modules.loginscreen.LoginScreenPresenter
import io.github.feelfreelinux.wykopmobilny.ui.modules.mainnavigation.MainNavigationPresenter
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.entry.EntryDetailPresenter
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.hot.HotPresenter
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.tag.TagPresenter
import io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist.hashtags.HashTagsNotificationsListPresenter
import io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist.notification.NotificationsListPresenter
import io.github.feelfreelinux.wykopmobilny.utils.rx.SubscriptionHelperApi
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi

@Module
class PresentersModule {
    @Provides
    fun provideLoginPresenter(userManagerApi : UserManagerApi) = LoginScreenPresenter(userManagerApi)

    @Provides
    fun provideMainNavigationPresenter(subscriptionHelperApi: SubscriptionHelperApi, userManagerApi: UserManagerApi, myWykopApi: MyWykopApi)
            = MainNavigationPresenter(subscriptionHelperApi, userManagerApi, myWykopApi)

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
    fun provideEditEntryPresenter(subscriptionHelperApi: SubscriptionHelperApi, entriesApi: EntriesApi)
            = EditEntryPresenter(subscriptionHelperApi, entriesApi)

    @Provides
    fun provideNotificationsListPresenter(subscriptionHelperApi: SubscriptionHelperApi, myWykopApi: MyWykopApi)
        = NotificationsListPresenter(subscriptionHelperApi, myWykopApi)

    @Provides
    fun provideHashTagsNotificationListPresenter(subscriptionHelperApi: SubscriptionHelperApi, myWykopApi: MyWykopApi)
            = HashTagsNotificationsListPresenter(subscriptionHelperApi, myWykopApi)
}