package io.github.feelfreelinux.wykopmobilny.ui.modules.notifications.notificationsservice

import com.evernote.android.job.JobCreator
import io.github.feelfreelinux.wykopmobilny.ui.modules.notifications.WykopNotificationManagerApi
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi
import javax.inject.Inject

class WykopNotificationJobCreator @Inject constructor(
    val settingsPreferencesApi: SettingsPreferencesApi,
    private val wykopNotificationManagerApi: WykopNotificationManagerApi,
    private val wykopNotificationsJobPresenter: WykopNotificationsJobPresenter
) : JobCreator {

    override fun create(tag: String) =
        when (tag) {
            WykopNotificationsJob.TAG ->
                WykopNotificationsJob(
                    wykopNotificationsJobPresenter,
                    settingsPreferencesApi,
                    wykopNotificationManagerApi
                )
            else -> null
        }
}
