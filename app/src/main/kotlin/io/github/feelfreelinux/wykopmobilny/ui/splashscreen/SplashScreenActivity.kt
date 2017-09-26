package io.github.feelfreelinux.wykopmobilny.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.ui.loginscreen.LoginScreenActivity
import io.github.feelfreelinux.wykopmobilny.ui.loginscreen.USER_LOGGED_IN
import io.github.feelfreelinux.wykopmobilny.ui.mainnavigation.launchNavigationActivity
import javax.inject.Inject

class SplashScreenActivity : BaseActivity(), SplashScreenView {
    @Inject lateinit var presenter : SplashScreenPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WykopApp.uiInjector.inject(this)
        presenter.subscribe(this)
        presenter.checkIsUserLoggedIn()
    }

    override fun startNavigationActivity() {
        launchNavigationActivity()
        finish()
    }
}

