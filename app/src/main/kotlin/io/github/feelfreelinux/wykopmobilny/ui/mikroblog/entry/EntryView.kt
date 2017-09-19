package io.github.feelfreelinux.wykopmobilny.ui.mikroblog.entry

import io.github.feelfreelinux.wykopmobilny.api.Entry
import io.github.feelfreelinux.wykopmobilny.api.EntryResponse
import io.github.feelfreelinux.wykopmobilny.base.BaseView

/**
 * Created by rlot on 9/14/17.
 */
interface EntryView : BaseView {
    fun showEntry(entry: Entry)
}