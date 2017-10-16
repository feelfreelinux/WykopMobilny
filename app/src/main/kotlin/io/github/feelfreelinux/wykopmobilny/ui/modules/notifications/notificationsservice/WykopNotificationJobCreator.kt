package io.github.feelfreelinux.wykopmobilny.ui.modules.notifications.notificationsservice

import com.evernote.android.job.Job
import com.evernote.android.job.JobCreator

class WykopNotificationJobCreator : JobCreator {
    override fun create(tag: String): Job? {
        return when (tag) {
            WykopNotificationsJob.TAG -> WykopNotificationsJob()
            else -> null
        }
    }
}
