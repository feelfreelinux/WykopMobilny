package io.github.feelfreelinux.wykopmobilny.ui.mikroblog.entry

import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.base.BaseView

/**
 * Created by rlot on 9/14/17.
 */
interface EntryDetailView : BaseView {
    fun showEntry(entry: Entry)
}