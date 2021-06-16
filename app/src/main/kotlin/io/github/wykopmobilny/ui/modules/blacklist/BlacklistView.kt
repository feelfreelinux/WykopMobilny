package io.github.wykopmobilny.ui.modules.blacklist

import io.github.wykopmobilny.base.BaseView
import io.github.wykopmobilny.blacklist.api.ApiBlacklist

interface BlacklistView : BaseView {
    fun importBlacklist(blacklist: ApiBlacklist)
    fun refreshResults()
}
