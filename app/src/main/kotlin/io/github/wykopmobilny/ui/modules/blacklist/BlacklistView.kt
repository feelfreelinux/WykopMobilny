package io.github.wykopmobilny.ui.modules.blacklist

import io.github.wykopmobilny.base.BaseView
import io.github.wykopmobilny.models.scraper.Blacklist

interface BlacklistView : BaseView {
    fun importBlacklist(blacklist: Blacklist)
    fun refreshResults()
}
