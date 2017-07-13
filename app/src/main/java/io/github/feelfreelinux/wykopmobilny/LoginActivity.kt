package io.github.feelfreelinux.wykopmobilny

import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    val appkey = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Get shared prefs, check is user connection data in memory
        val sharedPrefs = getSharedPreferences("io.github.feelfreelinux.wykopmobilny", 0) // 0 = MODE.Private
        if (sharedPrefs.contains("login") && sharedPrefs.contains("userkey"))
            startApplication()
        else { // if not, proceed to connection page
            // Set custom url loader to WebView
            webView.webViewClient = object : WebViewClient() {

                @SuppressWarnings("deprecation")
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    if (url != null) parseUrl(url)
                    return false
                }

                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                    if (request != null) parseUrl(request.url.toString())
                    return false
                }

                private fun parseUrl(redirectUrl : String) {
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
                        startApplication()
                    }
                }
            }
            // Open wykop connect api site
            webView.loadUrl("http://a.wykop.pl/user/connect/appkey/"+ appkey)
        }
    }

    fun startApplication() {
        // Yaay!
        startActivity(Intent(this, Mikroblog::class.java))
        finish()
    }
}
