package io.github.feelfreelinux.wykopmobilny.ui.modules.loginscreen

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.webkit.CookieManager
import io.github.feelfreelinux.wykopmobilny.APP_KEY
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.NavigatorApi
import kotlinx.android.synthetic.main.activity_webview.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

@Suppress("DEPRECATION")
class LoginScreenActivity : BaseActivity(), LoginScreenView {

    companion object {
        const val USER_LOGGED_IN = 21
        const val CONNECT_URL: String = "https://a.wykop.pl/user/connect/appkey/$APP_KEY"

        fun createIntent(context: Context): Intent {
            return Intent(context, LoginScreenActivity::class.java)
        }
    }

    @Inject
    lateinit var navigatorApi: NavigatorApi
    @Inject
    lateinit var presenter: LoginScreenPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WykopApp.uiInjector.inject(this)
        setContentView(R.layout.activity_webview)
        toolbar.title = getString(R.string.login)
        setSupportActionBar(toolbar)
    }

    override fun onResume() {
        super.onResume()
        presenter.subscribe(this)
        setupWebView()
    }

    override fun goBackToSplashScreen() {
        navigatorApi.openMainActivity(this)
        setResult(USER_LOGGED_IN)
        finish()
    }

    private fun setupWebView() {
        webView.apply {
            val cookieManager = CookieManager.getInstance()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                cookieManager.removeAllCookies(null)
            else cookieManager.removeAllCookie()

            webViewClient = LoginActivityWebClient({ presenter.handleUrl(it) })
            loadUrl(CONNECT_URL)
        }
    }

    override fun onPause() {
        super.onPause()
        presenter.unsubscribe()
    }
}

