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
    val LOGIN_REQUEST_CODE = 114
    @Inject lateinit var presenter : SplashScreenPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WykopApp.uiInjector.inject(this)
        presenter.subscribe(this)
        presenter.checkIsUserLoggedIn()
    }

    override fun startLoginActivity() {
        startActivityForResult(Intent(this, LoginScreenActivity::class.java), LOGIN_REQUEST_CODE)
    }

    override fun startNavigationActivity() {
        launchNavigationActivity()
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            LOGIN_REQUEST_CODE -> {
                if (resultCode == USER_LOGGED_IN) presenter.getUserToken()
            }
        }
    }
}

