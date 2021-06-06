package io.github.wykopmobilny.ui.modules.notifications.notificationsservice

import com.evernote.android.job.JobCreator
import io.github.wykopmobilny.storage.api.SettingsPreferencesApi
import io.github.wykopmobilny.ui.modules.notifications.WykopNotificationManagerApi
import javax.inject.Inject

class WykopNotificationJobCreator @Inject constructor(
    val settingsPreferencesApi: SettingsPreferencesApi,
    private val wykopNotificationManagerApi: WykopNotificationManagerApi,
    private val wykopNotificationsJobPresenter: WykopNotificationsJobPresenter
) : JobCreator {

    override fun create(tag: String) =
        if (tag == WykopNotificationsJob.TAG) {
            WykopNotificationsJob(
                wykopNotificationsJobPresenter,
                settingsPreferencesApi,
                wykopNotificationManagerApi
            )
        } else {
            null
        }
}
