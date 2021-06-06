package io.github.wykopmobilny.ui.modules.loginscreen

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import io.github.wykopmobilny.APP_KEY
import io.github.wykopmobilny.R
import io.github.wykopmobilny.base.BaseActivity
import io.github.wykopmobilny.databinding.ActivityWebviewBinding
import io.github.wykopmobilny.blacklist.api.Blacklist
import io.github.wykopmobilny.storage.api.BlacklistPreferencesApi
import io.github.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.wykopmobilny.utils.viewBinding
import javax.inject.Inject

class LoginScreenActivity : BaseActivity(), LoginScreenView {

    companion object {
        const val USER_LOGGED_IN = 21
        const val CONNECT_URL: String = "https://a2.wykop.pl/login/connect/appkey/$APP_KEY"

        fun createIntent(context: Context) = Intent(context, LoginScreenActivity::class.java)
    }

    @Inject
    lateinit var navigatorApi: NewNavigatorApi

    @Inject
    lateinit var presenter: LoginScreenPresenter

    @Inject
    lateinit var blacklistPreferences: BlacklistPreferencesApi

    private val binding by viewBinding(ActivityWebviewBinding::inflate)

    private val progressDialog by lazy { ProgressDialog(this) }

    override fun importBlacklist(blacklist: Blacklist) {
        if (blacklist.tags?.tags != null) {
            blacklistPreferences.blockedTags = HashSet<String>(blacklist.tags!!.tags!!.map { it.tag.removePrefix("#") })
        }
        if (blacklist.users?.users != null) {
            blacklistPreferences.blockedUsers = HashSet<String>(blacklist.users!!.users!!.map { it.nick.removePrefix("@") })
        }
        blacklistPreferences.blockedImported = true
        progressDialog.hide()
        finishActivity()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.toolbar.toolbar.title = getString(R.string.login)
        setSupportActionBar(binding.toolbar.toolbar)
        presenter.subscribe(this)
        setupWebView()
    }

    override fun goBackToSplashScreen() {
        val builder = AlertDialog.Builder(this)
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
        binding.webView.apply {
            CookieSyncManager.createInstance(this@LoginScreenActivity)
            CookieSyncManager.getInstance().startSync()
            val cookieManager = CookieManager.getInstance()

            // AppRTC requires third party cookies to work
            cookieManager.setAcceptCookie(true)
            cookieManager.setAcceptThirdPartyCookies(binding.webView, true)
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
