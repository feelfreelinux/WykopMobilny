package io.github.feelfreelinux.wykopmobilny.utils

import android.content.Context
import java.util.concurrent.TimeUnit

interface SettingsPreferencesApi {
    var notificationsSchedulerDelay : String?
}

class SettingsPreferences(context : Context) : Preferences(context, true), SettingsPreferencesApi {
    override var notificationsSchedulerDelay by stringPref(defaultValue = "15")
}