package io.github.feelfreelinux.wykopmobilny.di.modules

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.api.notifications.NotificationsApi
import io.github.feelfreelinux.wykopmobilny.api.pm.PMApi
import io.github.feelfreelinux.wykopmobilny.api.tag.TagApi
import io.github.feelfreelinux.wykopmobilny.api.user.LoginApi
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.entry.add.AddEntryPresenter
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.entry.comment.EditEntryCommentPresenter
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.entry.edit.EditEntryPresenter
import io.github.feelfreelinux.wykopmobilny.ui.modules.loginscreen.LoginScreenPresenter
import io.github.feelfreelinux.wykopmobilny.ui.modules.mainnavigation.MainNavigationPresenter
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.entry.EntryDetailPresenter
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.hot.HotPresenter
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.tag.entries.TagEntriesPresenter
import io.github.feelfreelinux.wykopmobilny.ui.modules.notifications.notificationsservice.WykopNotificationsJobPresenter
import io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist.hashtags.HashTagsNotificationsListPresenter
import io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist.notification.NotificationsListPresenter
import io.github.feelfreelinux.wykopmobilny.ui.modules.pm.conversation.ConversationPresenter
import io.github.feelfreelinux.wykopmobilny.ui.modules.pm.conversationslist.ConversationsListPresenter
import io.github.feelfreelinux.wykopmobilny.utils.rx.SubscriptionHelperApi
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi

@Module
class PresentersModule {
    @Provides
    fun provideLoginPresenter(userManagerApi: UserManagerApi,
                              subscriptionHelperApi: SubscriptionHelperApi,
                              userApi: LoginApi) = LoginScreenPresenter(userManagerApi,
            subscriptionHelperApi,
            userApi)

    @Provides
    fun provideMainNavigationPresenter(subscriptionHelperApi: SubscriptionHelperApi, userManagerApi: UserManagerApi, notificationsApi: NotificationsApi)
            = MainNavigationPresenter(subscriptionHelperApi, userManagerApi, notificationsApi)

    @Provides
    fun provideHotPresenter(subscriptionHelperApi: SubscriptionHelperApi, entriesApi: EntriesApi)
            = HotPresenter(subscriptionHelperApi, entriesApi)

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
    fun provideEditEntryCommentPresenter(subscriptionHelperApi: SubscriptionHelperApi, entriesApi: EntriesApi)
            = EditEntryCommentPresenter(subscriptionHelperApi, entriesApi)

    @Provides
    fun provideNotificationsListPresenter(subscriptionHelperApi: SubscriptionHelperApi, notificationsApi: NotificationsApi)
            = NotificationsListPresenter(subscriptionHelperApi, notificationsApi)

    @Provides
    fun provideConversationsListPresenter(subscriptionHelperApi: SubscriptionHelperApi, pmApi: PMApi)
            = ConversationsListPresenter(subscriptionHelperApi, pmApi)

    @Provides
    fun provideConversationPresenter(subscriptionHelperApi: SubscriptionHelperApi, pmApi: PMApi)
            = ConversationPresenter(subscriptionHelperApi, pmApi)

    @Provides
    fun provideHashTagsNotificationListPresenter(subscriptionHelperApi: SubscriptionHelperApi, notificationsApi: NotificationsApi)
            = HashTagsNotificationsListPresenter(subscriptionHelperApi, notificationsApi)

    @Provides
    fun provideWykopNoticationsJobPresenter(subscriptionHelperApi: SubscriptionHelperApi, notificationsApi: NotificationsApi, userManagerApi: UserManagerApi)
            = WykopNotificationsJobPresenter(subscriptionHelperApi, notificationsApi, userManagerApi)

    @Provides
    fun provideTagEntriesFragment(subscriptionHelperApi: SubscriptionHelperApi, tagApi: TagApi) =
            TagEntriesPresenter(subscriptionHelperApi, tagApi)
}