package io.github.wykopmobilny.storage.android

import android.content.Context
import io.github.wykopmobilny.storage.api.BlacklistPreferencesApi
import javax.inject.Inject

internal class BlacklistPreferences @Inject constructor(
    context: Context
) : Preferences(context), BlacklistPreferencesApi {

    override var blockedTags: Set<String> by stringSetPref("blockedTagsSet")
    override var blockedUsers: Set<String> by stringSetPref("blockedUsersSet")
    override var scraperSession: String? by stringPref("sessionString", "nosession")
    override var blockedImported: Boolean by booleanPref(defaultValue = false)
}
