package io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist.hashtags.HashTagsNotificationsListFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist.hashtags.HashTagsNotificationsListFragmentModule
import io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist.notification.NotificationsListFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist.notification.NotificationsListFragmentModule

@Module
abstract class NotificationsListFragmentProvider {
    @ContributesAndroidInjector(modules = [NotificationsListFragmentModule::class])
    abstract fun provideNotificationsFragment(): NotificationsListFragment

    @ContributesAndroidInjector(modules = [HashTagsNotificationsListFragmentModule::class])
    abstract fun provideHashTagsNotificationsFragment(): HashTagsNotificationsListFragment
}