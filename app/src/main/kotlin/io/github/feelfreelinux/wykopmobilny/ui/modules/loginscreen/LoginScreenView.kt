package io.github.feelfreelinux.wykopmobilny.ui.modules.loginscreen

import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.models.scraper.Blacklist

interface LoginScreenView : BaseView {
    fun goBackToSplashScreen()
    fun importBlacklist(blacklist : Blacklist)
}