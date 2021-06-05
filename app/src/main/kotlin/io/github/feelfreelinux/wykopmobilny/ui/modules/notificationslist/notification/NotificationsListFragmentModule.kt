package io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist.notification

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.notifications.NotificationsApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

@Module
class NotificationsListFragmentModule {
    @Provides
    fun providesNotificationsListPresenter(schedulers: Schedulers, notificationsApi: NotificationsApi) =
        NotificationsListPresenter(schedulers, notificationsApi)
}
