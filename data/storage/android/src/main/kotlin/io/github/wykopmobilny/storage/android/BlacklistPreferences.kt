package io.github.wykopmobilny.storage.android

import android.content.Context
import io.github.wykopmobilny.storage.api.Blacklist
import io.github.wykopmobilny.storage.api.BlacklistPreferencesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class BlacklistPreferences @Inject constructor(
    context: Context,
) : BasePreferences(context), BlacklistPreferencesApi {

    override var blockedTags by stringSetPref("blockedTagsSet")
    override var blockedUsers by stringSetPref("blockedUsersSet")
    override var blockedImported by booleanPref(key = "blockedImported")

    override val blacklist: Flow<Blacklist?> =
        preferences.filter { it == "blockedTagsSet" || it == "blockedUsersSet" }
            .onStart { emit("") }
            .map {
                val tags = blockedTags ?: return@map null
                val users = blockedUsers ?: return@map null

                Blacklist(tags = tags, users = users)
            }
            .distinctUntilChanged()

    override suspend fun updateBlacklist(newValue: Blacklist?) = withContext(Dispatchers.Default) {
        blockedTags = newValue?.tags
        blockedUsers = newValue?.users
        blockedImported = newValue != null
    }
}
