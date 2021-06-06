package io.github.wykopmobilny.ui.modules.notificationslist.notification

import dagger.Module
import dagger.Provides
import io.github.wykopmobilny.api.notifications.NotificationsApi
import io.github.wykopmobilny.base.Schedulers

@Module
class NotificationsListFragmentModule {
    @Provides
    fun providesNotificationsListPresenter(schedulers: Schedulers, notificationsApi: NotificationsApi) =
        NotificationsListPresenter(schedulers, notificationsApi)
}
