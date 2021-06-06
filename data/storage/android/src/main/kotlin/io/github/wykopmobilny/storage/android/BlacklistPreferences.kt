package io.github.wykopmobilny.storage.android

import android.content.Context
import io.github.wykopmobilny.storage.api.BlacklistPreferencesApi
import javax.inject.Inject

internal class BlacklistPreferences @Inject constructor(
    context: Context
) : BasePreferences(context), BlacklistPreferencesApi {

    override var blockedTags by stringSetPref("blockedTagsSet")
    override var blockedUsers by stringSetPref("blockedUsersSet")
    override var blockedImported by booleanPref(key = "blockedImported")
}
