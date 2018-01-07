package io.github.feelfreelinux.wykopmobilny.ui.modules.notifications.notificationsservice

import com.evernote.android.job.Job
import com.evernote.android.job.JobCreator
import io.github.feelfreelinux.wykopmobilny.ui.modules.notifications.WykopNotificationManagerApi
import io.github.feelfreelinux.wykopmobilny.utils.SettingsPreferencesApi
import javax.inject.Inject

class WykopNotificationJobCreator @Inject constructor(val settingsPreferencesApi: SettingsPreferencesApi,
                                                      val wykopNotificationManagerApi: WykopNotificationManagerApi,
                                                      val wykopNotificationsJobPresenter: WykopNotificationsJobPresenter) : JobCreator {
    override fun create(tag: String): Job? {
        return when (tag) {
            WykopNotificationsJob.TAG -> WykopNotificationsJob(wykopNotificationsJobPresenter, settingsPreferencesApi, wykopNotificationManagerApi)
            else -> null
        }
    }
}
