package io.github.wykopmobilny.ui.modules.loginscreen

import io.github.wykopmobilny.base.BaseView
import io.github.wykopmobilny.blacklist.api.ApiBlacklist

interface LoginScreenView : BaseView {
    fun goBackToSplashScreen()
    fun importBlacklist(blacklist: ApiBlacklist)
}
