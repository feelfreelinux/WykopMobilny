package io.github.feelfreelinux.wykopmobilny.utils.preferences

import android.content.Context
import io.github.feelfreelinux.wykopmobilny.ui.modules.links.upcoming.UpcomingPresenter
import javax.inject.Inject

interface LinksPreferencesApi {
    var readLinksIds: Set<String>
    var linkCommentsDefaultSort: String?
    var upcomingDefaultSort: String?
}

class LinksPreferences @Inject constructor(context: Context) : Preferences(context, false), LinksPreferencesApi {
    override var readLinksIds by stringSetPref(defaultValue = HashSet())
    override var linkCommentsDefaultSort by stringPref(defaultValue = "best")
    override var upcomingDefaultSort by stringPref(defaultValue = UpcomingPresenter.SORTBY_COMMENTS)
}
