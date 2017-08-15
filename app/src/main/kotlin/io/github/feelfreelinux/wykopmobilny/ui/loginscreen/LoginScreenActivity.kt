package io.github.feelfreelinux.wykopmobilny.ui.loginscreen

import android.os.Bundle
import com.github.salomonbrys.kodein.LazyKodein
import com.github.salomonbrys.kodein.android.appKodein
import com.github.salomonbrys.kodein.instance
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.ui.mainnavigation.lauchMainNavigation
import io.github.feelfreelinux.wykopmobilny.objects.APP_KEY
import io.github.feelfreelinux.wykopmobilny.utils.*
import kotlinx.android.synthetic.main.activity_login.*

class LoginScreenActivity : BaseActivity(), LoginScreenContract.View {
    val CONNECT_URL : String = "https://a.wykop.pl/user/connect/appkey/$APP_KEY"

    private val kodein = LazyKodein(appKodein)
    val apiPrefs : ApiPreferences by kodein.instance()
    val apiManager : WykopApi by kodein.instance()
    private val presenter by lazy {
        LoginScreenPresenter(apiPrefs, apiManager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
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

