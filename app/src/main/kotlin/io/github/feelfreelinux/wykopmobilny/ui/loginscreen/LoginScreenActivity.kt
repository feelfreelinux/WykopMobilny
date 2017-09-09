package io.github.feelfreelinux.wykopmobilny.ui.loginscreen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.github.salomonbrys.kodein.instance
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.api.WykopApi
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.ui.mainnavigation.launchNavigationActivity
import io.github.feelfreelinux.wykopmobilny.APP_KEY
import io.github.feelfreelinux.wykopmobilny.utils.*
import io.github.feelfreelinux.wykopmobilny.utils.api.IApiPreferences
import kotlinx.android.synthetic.main.activity_webview.*
import kotlinx.android.synthetic.main.toolbar.*

const val CONNECT_URL : String = "https://a.wykop.pl/user/connect/appkey/$APP_KEY"
const val USER_LOGGED_IN = 21

class LoginScreenActivity : BaseActivity(), LoginScreenContract.View {
    private val presenter by lazy {
        LoginScreenPresenter(kodein.instance<IApiPreferences>().value, kodein.instance<WykopApi>().value)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

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

