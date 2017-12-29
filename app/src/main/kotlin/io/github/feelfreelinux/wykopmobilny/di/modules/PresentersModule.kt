package io.github.feelfreelinux.wykopmobilny.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.api.links.LinksApi
import io.github.feelfreelinux.wykopmobilny.api.mywykop.MyWykopApi
import io.github.feelfreelinux.wykopmobilny.api.notifications.NotificationsApi
import io.github.feelfreelinux.wykopmobilny.api.pm.PMApi
import io.github.feelfreelinux.wykopmobilny.api.search.SearchApi
import io.github.feelfreelinux.wykopmobilny.api.suggest.SuggestApi
import io.github.feelfreelinux.wykopmobilny.api.tag.TagApi
import io.github.feelfreelinux.wykopmobilny.api.user.LoginApi
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.entry.add.AddEntryPresenter
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.entry.comment.EditEntryCommentPresenter
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.entry.edit.EditEntryPresenter
import io.github.feelfreelinux.wykopmobilny.ui.modules.links.linkdetails.LinkDetailsPresenter
import io.github.feelfreelinux.wykopmobilny.ui.modules.links.promoted.PromotedPresenter
import io.github.feelfreelinux.wykopmobilny.ui.modules.loginscreen.LoginScreenPresenter
import io.github.feelfreelinux.wykopmobilny.ui.modules.mainnavigation.MainNavigationPresenter
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.entry.EntryDetailPresenter
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.hot.HotPresenter
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.tag.entries.TagEntriesPresenter
import io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.index.MyWykopIndexPresenter
import io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.tags.MyWykopTagsPresenter
import io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.users.MyWykopUsersPresenter
import io.github.feelfreelinux.wykopmobilny.ui.modules.notifications.notificationsservice.WykopNotificationsJobPresenter
import io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist.hashtags.HashTagsNotificationsListPresenter
import io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist.notification.NotificationsListPresenter
import io.github.feelfreelinux.wykopmobilny.ui.modules.pm.conversation.ConversationPresenter
import io.github.feelfreelinux.wykopmobilny.ui.modules.pm.conversationslist.ConversationsListPresenter
import io.github.feelfreelinux.wykopmobilny.ui.modules.search.entry.EntrySearchPresenter
import io.github.feelfreelinux.wykopmobilny.ui.modules.search.links.LinkSearchPresenter
import io.github.feelfreelinux.wykopmobilny.ui.modules.search.users.UsersSearchPresenter
import io.github.feelfreelinux.wykopmobilny.ui.suggestions.HashTagsSuggestionsAdapter
import io.github.feelfreelinux.wykopmobilny.ui.suggestions.UsersSuggestionsAdapter
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
    fun provideTagEntriesFragmentPresenter(subscriptionHelperApi: SubscriptionHelperApi, tagApi: TagApi) =
            TagEntriesPresenter(subscriptionHelperApi, tagApi)

    @Provides
    fun provideLinkDetailsPresenter(subscriptionHelperApi: SubscriptionHelperApi, linksApi: LinksApi) = LinkDetailsPresenter(subscriptionHelperApi, linksApi)
    @Provides
    fun provideMyWykopIndexPresenter(subscriptionHelperApi: SubscriptionHelperApi, myWykopApi: MyWykopApi) = MyWykopIndexPresenter(subscriptionHelperApi, myWykopApi)

    @Provides
    fun provideMyWykopTagsPresenter(subscriptionHelperApi: SubscriptionHelperApi, myWykopApi: MyWykopApi) = MyWykopTagsPresenter(subscriptionHelperApi, myWykopApi)

    @Provides
    fun provideMyWykopUsersPresenter(subscriptionHelperApi: SubscriptionHelperApi, myWykopApi: MyWykopApi) = MyWykopUsersPresenter(subscriptionHelperApi, myWykopApi)

    @Provides
    fun providePromotedPresenter(subscriptionHelperApi: SubscriptionHelperApi, linksApi: LinksApi) = PromotedPresenter(subscriptionHelperApi, linksApi)

    @Provides
    fun provideEntrySearchPresenter(subscriptionHelperApi: SubscriptionHelperApi, searchApi: SearchApi) = EntrySearchPresenter(subscriptionHelperApi, searchApi)

    @Provides
    fun provideLinkSearchPresenter(subscriptionHelperApi: SubscriptionHelperApi, searchApi: SearchApi) = LinkSearchPresenter(subscriptionHelperApi, searchApi)

    @Provides
    fun provideUsersSearchPresenter(subscriptionHelperApi: SubscriptionHelperApi, searchApi: SearchApi) = UsersSearchPresenter(subscriptionHelperApi, searchApi)
}