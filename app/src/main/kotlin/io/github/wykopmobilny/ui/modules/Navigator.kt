package io.github.wykopmobilny.ui.modules

import android.app.Activity
import io.github.wykopmobilny.models.dataclass.Link
import io.github.wykopmobilny.ui.modules.input.entry.add.AddEntryActivity
import io.github.wykopmobilny.ui.modules.links.linkdetails.LinkDetailsActivity

interface NavigatorApi {
    fun openAddEntryActivity(context: Activity, receiver: String? = null, extraBody: String? = null)
    fun openLinkDetailsActivity(context: Activity, link: Link)
}

class Navigator : NavigatorApi {

    override fun openAddEntryActivity(context: Activity, receiver: String?, extraBody: String?) =
        context.startActivity(AddEntryActivity.createIntent(context, receiver, extraBody))

    override fun openLinkDetailsActivity(context: Activity, link: Link) =
        context.startActivity(LinkDetailsActivity.createIntent(context, link))
}
