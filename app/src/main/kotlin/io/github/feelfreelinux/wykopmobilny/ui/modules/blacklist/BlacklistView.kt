package io.github.feelfreelinux.wykopmobilny.ui.modules.blacklist

import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.models.scraper.Blacklist

interface BlacklistView : BaseView {
    fun importBlacklist(blacklist : Blacklist)
    fun refreshResults()
}