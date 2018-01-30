package io.github.feelfreelinux.wykopmobilny.utils

import android.content.Context
import javax.inject.Inject

interface LinksPreferencesApi {
    var readLinksIds: Set<String>
}

class LinksPreferences (context : Context) : Preferences(context, false), LinksPreferencesApi {
    override var readLinksIds by stringSetPref(defaultValue = HashSet())
}