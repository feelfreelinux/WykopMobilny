package io.github.wykopmobilny.storage.android

import android.content.Context
import io.github.wykopmobilny.storage.api.LinksPreferencesApi
import javax.inject.Inject

internal class LinksPreferences @Inject constructor(
    context: Context
) : Preferences(context), LinksPreferencesApi {

    override var readLinksIds by stringSetPref(defaultValue = HashSet())
    override var linkCommentsDefaultSort by stringPref(defaultValue = "best")
    override var upcomingDefaultSort by stringPref(defaultValue = null)
}
