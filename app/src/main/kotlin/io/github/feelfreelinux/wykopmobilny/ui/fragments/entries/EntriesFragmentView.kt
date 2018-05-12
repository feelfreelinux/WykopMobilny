package io.github.feelfreelinux.wykopmobilny.ui.fragments.entries

import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry

interface EntriesFragmentView : BaseView {
    fun updateEntry(entry : Entry)
}