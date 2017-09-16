package io.github.feelfreelinux.wykopmobilny.ui.loginscreen

import android.os.Bundle
import io.github.feelfreelinux.wykopmobilny.APP_KEY
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.utils.api.CredentialsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import kotlinx.android.synthetic.main.activity_webview.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

const val CONNECT_URL : String = "https://a.wykop.pl/user/connect/appkey/$APP_KEY"
const val USER_LOGGED_IN = 21

class LoginScreenActivity : BaseActivity(), LoginScreenView {
    @Inject lateinit var presenter : LoginScreenPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)
        WykopApp.uiInjector.inject(this)
        toolbar.title = getString(R.string.login)
        setSupportActionBar(toolbar)

        presenter.subscribe(this)
        setupWebView()
    }

    override fun goBackToSplashScreen() {
        setResult(USER_LOGGED_IN)
        finish()
    }


    fun setupWebView() {
        webView.isVisible = true
        webView.webViewClient = LoginActivityWebClient({ presenter.handleUrl(it) })
        webView.loadUrl(CONNECT_URL)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
    }
}

