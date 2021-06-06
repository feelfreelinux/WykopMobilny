package io.github.wykopmobilny.ui.modules.search.users

import io.github.wykopmobilny.base.BaseView
import io.github.wykopmobilny.models.dataclass.Author

interface UsersSearchView : BaseView {
    fun showUsers(entryList: List<Author>)
}
