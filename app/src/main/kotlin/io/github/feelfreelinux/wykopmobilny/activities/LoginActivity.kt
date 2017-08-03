package io.github.feelfreelinux.wykopmobilny.activities

import android.os.Bundle
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.objects.APP_KEY
import io.github.feelfreelinux.wykopmobilny.objects.Profile
import io.github.feelfreelinux.wykopmobilny.presenters.WykopWebViewClient
import io.github.feelfreelinux.wykopmobilny.utils.ApiPreferences
import io.github.feelfreelinux.wykopmobilny.utils.WykopApiManager
import io.github.feelfreelinux.wykopmobilny.utils.invisible
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : WykopActivity() {

    private val apiPreferences = ApiPreferences()
    private val webViewClient = WykopWebViewClient()
    private val apiManager: WykopApiManager by lazy { WykopApiManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        title = getString(R.string.login)
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
                        val userSession = data as Profile
                        apiPreferences.userToken = userSession.userKey
                        apiPreferences.avatarUrl = userSession.avatarMed

                        lauchMainNavigation()
                        finish()
                    }
                })
    }
}

