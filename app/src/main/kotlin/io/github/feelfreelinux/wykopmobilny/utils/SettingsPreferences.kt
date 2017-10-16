package io.github.feelfreelinux.wykopmobilny.utils

import android.content.Context
import java.util.concurrent.TimeUnit

interface SettingsPreferencesApi {
    var notificationsSchedulerDelay : Long
}

class SettingsPreferences(context : Context) : Preferences(context), SettingsPreferencesApi {
    override var notificationsSchedulerDelay by longPref(defaultValue = TimeUnit.MINUTES.toMillis(15))
}