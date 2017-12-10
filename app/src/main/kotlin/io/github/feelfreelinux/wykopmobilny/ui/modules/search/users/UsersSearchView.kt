package io.github.feelfreelinux.wykopmobilny.ui.modules.search.users

import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Author

interface UsersSearchView : BaseView {
    fun showUsers(entryList : List<Author>)
}