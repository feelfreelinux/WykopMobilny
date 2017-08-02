package io.github.feelfreelinux.wykopmobilny.activities

import android.os.Bundle
import com.github.salomonbrys.kodein.LazyKodein
import com.github.salomonbrys.kodein.android.appKodein
import com.github.salomonbrys.kodein.instance
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.objects.APP_KEY
import io.github.feelfreelinux.wykopmobilny.objects.UserSessionReponse
import io.github.feelfreelinux.wykopmobilny.projectors.WykopWebViewClient
import io.github.feelfreelinux.wykopmobilny.utils.ApiPreferences
import io.github.feelfreelinux.wykopmobilny.utils.WykopApiManager
import io.github.feelfreelinux.wykopmobilny.utils.invisible
import io.github.feelfreelinux.wykopmobilny.utils.printout
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : WykopActivity() {
    private val kodein = LazyKodein(appKodein)
    private val apiPreferences: ApiPreferences by kodein.instance()
    private val webViewClient: WykopWebViewClient by kodein.instance()
    private val apiManager: WykopApiManager by kodein.instance()

    val answerConstant: String by kodein.instance("serverURL")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        title = getString(R.string.login)
        printout(answerConstant)
        setupLoginViewClient()
        checkIfUserIsLogged()
    }

    private fun setupLoginViewClient() {
        webViewClient.onLoginSuccess {
            startMikroblog()
        }
    }


    private fun checkIfUserIsLogged() {
        if (apiPreferences.isUserAuthorized())
            startMikroblog()
        else {
            showLoginWebView()
        }
    }

    private fun showLoginWebView() {
        webView.webViewClient = webViewClient
        webView.loadUrl("http://a.wykop.pl/user/connect/appkey/$APP_KEY")
    }

    fun startMikroblog() {
        supportActionBar?.hide()
        webView.invisible()
        apiManager.getUserSessionToken(
                { data ->
                    run {
                        val userSession = data as UserSessionReponse
                        apiPreferences.userToken = userSession.userkey
                        apiPreferences.avatarUrl = userSession.avatar_med

                        lauchMainNavigation()
                        finish()
                    }
                })
    }
}

