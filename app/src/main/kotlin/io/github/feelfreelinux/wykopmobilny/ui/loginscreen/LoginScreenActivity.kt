package io.github.feelfreelinux.wykopmobilny.ui.loginscreen

import android.os.Bundle
import com.github.salomonbrys.kodein.instance
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.api.WykopApi
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.ui.mainnavigation.lauchMainNavigation
import io.github.feelfreelinux.wykopmobilny.APP_KEY
import io.github.feelfreelinux.wykopmobilny.utils.*
import io.github.feelfreelinux.wykopmobilny.utils.api.ApiPreferences
import kotlinx.android.synthetic.main.activity_webview.*
import kotlinx.android.synthetic.main.toolbar.*

const val CONNECT_URL : String = "https://a.wykop.pl/user/connect/appkey/$APP_KEY"
class LoginScreenActivity : BaseActivity(), LoginScreenContract.View {
    private val presenter by lazy {
        LoginScreenPresenter(kodein.instance<ApiPreferences>().value, kodein.instance<WykopApi>().value)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)
        setSupportActionBar(toolbar)
        toolbar.title = getString(R.string.login)
        presenter.subscribe(this)
    }

    override fun startNavigationActivity() {
        lauchMainNavigation()
        finish()
    }

    // @TODO Create some sort of splash screen while loading data in presenter. prpbly should go to this section
    override fun hideWebView() =
        webView.gone()

    override fun setupWebView() {
        webView.visible()
        webView.webViewClient = LoginActivityWebClient(presenter.userLoggedCallback)
        webView.loadUrl(CONNECT_URL)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
    }
}

