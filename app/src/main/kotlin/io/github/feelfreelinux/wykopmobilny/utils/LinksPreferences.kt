package io.github.feelfreelinux.wykopmobilny.utils

import android.content.Context

class LinksPreferences(context : Context) : Preferences(context, false) {
    var readLinksIds by stringSetPref(defaultValue = HashSet())
}