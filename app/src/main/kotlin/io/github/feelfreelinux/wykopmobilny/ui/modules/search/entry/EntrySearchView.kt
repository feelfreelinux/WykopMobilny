package io.github.feelfreelinux.wykopmobilny.ui.modules.search.entry

import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.ui.fragments.entries.EntriesFragmentView

interface EntrySearchView : EntriesFragmentView {
    var showSearchEmptyView : Boolean
}