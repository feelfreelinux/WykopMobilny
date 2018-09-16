package io.github.feelfreelinux.wykopmobilny.utils.preferences

import android.content.Context
import javax.inject.Inject

interface BlacklistPreferencesApi {
    var blockedTags: Set<String>
    var blockedUsers: Set<String>
    var scraperSession: String?
    var blockedImported: Boolean
}

class BlacklistPreferences @Inject constructor(context: Context) : Preferences(context, false), BlacklistPreferencesApi {
    override var blockedTags: Set<String> by stringSetPref("blockedTagsSet")
    override var blockedUsers: Set<String> by stringSetPref("blockedUsersSet")
    override var scraperSession: String? by stringPref("sessionString", "nosession")
    override var blockedImported: Boolean by booleanPref(defaultValue = false)
}

