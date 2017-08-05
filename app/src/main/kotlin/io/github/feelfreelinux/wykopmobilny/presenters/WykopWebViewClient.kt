package io.github.feelfreelinux.wykopmobilny.presenters

import android.annotation.TargetApi
import android.os.Build
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import io.github.feelfreelinux.wykopmobilny.utils.ApiPreferences

typealias loginSuccessCallback = () -> Unit

class WykopWebViewClient(val apiPreferences : ApiPreferences) : WebViewClient() {
    private var loginSuccessCallback: loginSuccessCallback? = null

    @Suppress("OverridingDeprecatedMember")
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
        if (redirectUrl.hasUserData()) {
            saveDataToPrefs(redirectUrl)
            loginSuccessCallback?.invoke()
        }else {
            throw IllegalStateException("Redirect url ($redirectUrl) doesn't contain userData")
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

    fun onLoginSuccess(loginSuccessCallback: loginSuccessCallback) {
        this.loginSuccessCallback = loginSuccessCallback
    }

    private fun String.hasUserData() = contains("/token/") and contains("/login/")
}