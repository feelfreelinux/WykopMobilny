package io.github.wykopmobilny.ui.modules.notificationslist.hashtags

import dagger.Module
import dagger.Provides
import io.github.wykopmobilny.api.notifications.NotificationsApi
import io.github.wykopmobilny.base.Schedulers

@Module
class HashTagsNotificationsListFragmentModule {
    @Provides
    fun providesHashTagsNotificationsListPresenter(schedulers: Schedulers, notificationsApi: NotificationsApi) =
        HashTagsNotificationsListPresenter(schedulers, notificationsApi)
}
