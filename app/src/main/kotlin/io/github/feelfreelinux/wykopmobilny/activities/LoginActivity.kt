package io.github.feelfreelinux.wykopmobilny.activities

import android.annotation.TargetApi
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.objects.APP_KEY
import io.github.feelfreelinux.wykopmobilny.utils.ApiPreferences
import io.github.feelfreelinux.wykopmobilny.utils.WykopApiManager
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private val apiPreferences = ApiPreferences()
    private val apiManager: WykopApiManager by lazy {
        WykopApiManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        title = getString(R.string.login)

        checkIfUserIsLogged()
    }

    private fun checkIfUserIsLogged() {
        if (apiPreferences.isUserAuthorized())
            startMikroblog()
        else {
            showLoginWebView()
        }
    }

    private fun showLoginWebView() {
        // Set custom url loader to WebView
        webView.webViewClient = object : WebViewClient() {
            @SuppressWarnings("deprecation")
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                url?.let { parseUrl(it) }
                return false
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                request?.let { parseUrl(it.url.toString()) }
                return false
            }

            private fun parseUrl(redirectUrl: String) {
                if (redirectUrl.containsUserData()) {
                    saveDataToPrefs(redirectUrl)
                    startMikroblog()
                }
            }

            private fun saveDataToPrefs(redirectUrl: String) {
                apiPreferences.login = redirectUrl
                        .split("/token/").first()
                        .substringAfterLast("/login/")

                apiPreferences.userKey = redirectUrl
                        .split("/token/").last()
                        .replace("/", "")
            }

            private fun String.containsUserData() = contains("/token/") and contains("/login/")
        }
        // Open wykop connect api site
        webView.loadUrl("http://a.wykop.pl/user/connect/appkey/$APP_KEY")
    }

    fun startMikroblog() {
        supportActionBar?.hide()
        apiManager.getUserSessionToken(
                successCallback = {
                    launchMikroblogHotList(apiManager.getData())
                    finish()
                })
    }
}
