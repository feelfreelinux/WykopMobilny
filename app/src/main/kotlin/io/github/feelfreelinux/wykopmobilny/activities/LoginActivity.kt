package io.github.feelfreelinux.wykopmobilny.activities

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import io.github.feelfreelinux.wykopmobilny.BuildConfig
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.objects.SHARED_PREFS_NAME
import io.github.feelfreelinux.wykopmobilny.utils.WykopApiManager
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    private val appKey = BuildConfig.APP_KEY
    private val secret = BuildConfig.APP_SECRET

    private val sharedPrefs: SharedPreferences by lazy {
        getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        title = getString(R.string.login)

        checkIfUserIsLogged()
    }

    private fun checkIfUserIsLogged() {
        if (sharedPrefs.isUserAuthorized())
            startApplication(sharedPrefs.getString("login", "null"), sharedPrefs.getString("userkey", "null"))
        else {
            showLoginWebView(sharedPrefs)
        }
    }

    private fun showLoginWebView(sharedPrefs: SharedPreferences) {
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
                // Get login and userkey from redirect
                if (redirectUrl.contains("/token/") && redirectUrl.contains("/login/")) {
                    val login = redirectUrl
                            .split("/token/").first()
                            .substringAfterLast("/login/")

                    val userKey = redirectUrl
                            .split("/token/").last()
                            .replace("/", "")

                    // Save login data to storage
                    sharedPrefs.edit()
                            .putString("login", login)
                            .putString("userkey", userKey)
                            .apply()
                    startApplication(login, userKey)
                }
            }
        }
        // Open wykop connect api site
        webView.loadUrl("http://a.wykop.pl/user/connect/appkey/" + appKey)
    }

    fun startApplication(login: String, accountKey: String) {
        // Yaay!
        webView.visibility = View.INVISIBLE
        supportActionBar?.hide()
        var wam = WykopApiManager(login, accountKey, secret, appKey, this)
        wam.initAction = object : WykopApiManager.WykopApiAction {
            override fun success(json: JSONObject) {
                val intent = Intent(this@LoginActivity, MikroblogHotList::class.java)
                intent.putExtra("wamData", wam.getData())
                startActivity(intent)
                finish()
            }
        }
        wam.getUserSessionToken()
    }

    private fun SharedPreferences.isUserAuthorized(): Boolean {
        return contains("login") and contains("userkey")
    }
}
