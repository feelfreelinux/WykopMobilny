package io.github.wykopmobilny.storage.android

import android.content.Context
import dagger.Reusable
import io.github.wykopmobilny.storage.api.Blacklist
import io.github.wykopmobilny.storage.api.BlacklistPreferencesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import javax.inject.Inject

@Reusable
internal class BlacklistPreferences @Inject constructor(
    context: Context,
) : BasePreferences(context), BlacklistPreferencesApi {

    override var blockedTags by stringSetPref("blockedTagsSet")
    override var blockedUsers by stringSetPref("blockedUsersSet")

    override val blacklist: Flow<Blacklist?> =
        preferences.filter { it == "blockedTagsSet" || it == "blockedUsersSet" }
            .onStart { emit("") }
            .map {
                val tags = blockedTags ?: return@map null
                val users = blockedUsers ?: return@map null

                Blacklist(tags = tags, users = users)
            }
            .distinctUntilChanged()

    override suspend fun update(updater: (Blacklist) -> Blacklist) = withContext(Dispatchers.Default) {
        val newValue = updater(blacklist.first() ?: Blacklist.empty())
        blockedTags = newValue.tags
        blockedUsers = newValue.users
    }

    override suspend fun clear() {
        blockedTags = null
        blockedUsers = null
    }
}
