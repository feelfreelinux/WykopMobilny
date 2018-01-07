package io.github.feelfreelinux.wykopmobilny.ui.modules.mainnavigation

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.feelfreelinux.wykopmobilny.ui.modules.links.promoted.PromotedFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.links.promoted.PromotedFragmentModule
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.hot.HotFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.hot.HotFragmentModule
import io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.MyWykopFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.MyWykopFragmentProvider
import io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist.hashtags.HashTagsNotificationsListFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist.hashtags.HashTagsNotificationsListFragmentModule
import io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist.notification.NotificationsListFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist.notification.NotificationsListFragmentModule
import io.github.feelfreelinux.wykopmobilny.ui.modules.pm.conversationslist.ConversationsListFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.pm.conversationslist.ConversationsListFragmentModule
import io.github.feelfreelinux.wykopmobilny.ui.modules.search.SearchFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.search.SearchFragmentProvider

@Module
abstract class MainNavigationFragmentProvider {
    @ContributesAndroidInjector(modules = [PromotedFragmentModule::class])
    abstract fun providePromotedFragment() : PromotedFragment

    @ContributesAndroidInjector(modules = [HotFragmentModule::class])
    abstract fun provideHotFragment() : HotFragment

    @ContributesAndroidInjector(modules = [HashTagsNotificationsListFragmentModule::class])
    abstract fun provideHashTagsNotificationsListFragment() : HashTagsNotificationsListFragment

    @ContributesAndroidInjector(modules = [NotificationsListFragmentModule::class])
    abstract fun provideNotificationsListFragment() : NotificationsListFragment

    @ContributesAndroidInjector(modules = [ConversationsListFragmentModule::class])
    abstract fun provideConversationListFragment() : ConversationsListFragment

    @ContributesAndroidInjector(modules = [MyWykopFragmentProvider::class])
    abstract fun provideMyWykopFragment() : MyWykopFragment

    @ContributesAndroidInjector(modules = [SearchFragmentProvider::class])
    abstract fun provideSearchFragment() : SearchFragment
}