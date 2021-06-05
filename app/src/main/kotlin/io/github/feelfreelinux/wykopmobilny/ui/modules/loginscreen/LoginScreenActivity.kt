package io.github.feelfreelinux.wykopmobilny.ui.modules.loginscreen

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import io.github.feelfreelinux.wykopmobilny.APP_KEY
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.models.scraper.Blacklist
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.createAlertBuilder
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.utils.preferences.BlacklistPreferences
import kotlinx.android.synthetic.main.activity_webview.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

@Suppress("DEPRECATION")
class LoginScreenActivity : BaseActivity(), LoginScreenView {

    companion object {
        const val USER_LOGGED_IN = 21
        const val CONNECT_URL: String = "https://a2.wykop.pl/login/connect/appkey/$APP_KEY"

        fun createIntent(context: Context) = Intent(context, LoginScreenActivity::class.java)
    }

    @Inject lateinit var navigatorApi: NewNavigatorApi

    @Inject lateinit var presenter: LoginScreenPresenter

    @Inject lateinit var blacklistPreferences: BlacklistPreferences

    private val progressDialog by lazy { ProgressDialog(this) }

    override fun importBlacklist(blacklist: Blacklist) {
        if (blacklist.tags?.blockedTags != null) {
            blacklistPreferences.blockedTags = HashSet<String>(blacklist.tags!!.blockedTags!!.map { it.tag.removePrefix("#") })
        }
        if (blacklist.users?.blockedUsers != null) {
            blacklistPreferences.blockedUsers = HashSet<String>(blacklist.users!!.blockedUsers!!.map { it.nick.removePrefix("@") })
        }
        blacklistPreferences.blockedImported = true
        progressDialog.hide()
        finishActivity()
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
        val builder = createAlertBuilder()
        builder.setTitle(getString(R.string.blacklist_import_title))
        builder.setMessage(getString(R.string.blacklist_import_ask))
        builder.setPositiveButton(getString(R.string.blacklist_import_action)) { _, _ ->
            progressDialog.isIndeterminate = true
            progressDialog.setTitle(getString(R.string.blacklist_import_progress))
            progressDialog.show()
            presenter.importBlacklist()
        }
        builder.setNegativeButton(android.R.string.cancel) { _, _ -> finishActivity() }
        builder.setCancelable(false)
        builder.show()
    }

    private fun finishActivity() {
        navigatorApi.openMainActivity()
        setResult(USER_LOGGED_IN)
        finish()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        webView.apply {
            CookieSyncManager.createInstance(this@LoginScreenActivity)
            CookieSyncManager.getInstance().startSync()
            val cookieManager = CookieManager.getInstance()

            if (Build.VERSION.SDK_INT >= 21) {
                // AppRTC requires third party cookies to work
                cookieManager.setAcceptCookie(true)
                cookieManager.setAcceptThirdPartyCookies(webView, true)
            } else {
                cookieManager.setAcceptCookie(true)
            }
            settings.javaScriptEnabled = true

            webViewClient = LoginActivityWebClient { presenter.handleUrl(it) }
            loadUrl(CONNECT_URL)
        }
    }

    override fun onStop() {
        presenter.unsubscribe()
        super.onStop()
    }
}
