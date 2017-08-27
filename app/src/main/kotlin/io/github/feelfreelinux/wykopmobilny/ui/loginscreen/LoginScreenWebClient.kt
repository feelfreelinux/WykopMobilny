package io.github.feelfreelinux.wykopmobilny.ui.loginscreen

import android.annotation.TargetApi
import android.os.Build
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

typealias UserLoggedCallback = (login : String, token : String) -> Unit

class LoginActivityWebClient(private val userLoggedCallback: UserLoggedCallback) : WebViewClient() {
    @Suppress("OverridingDeprecatedMember")
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        url?.let { parseReturnURL(it) }
        return false
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        request?.let { parseReturnURL(it.url.toString()) }
        return false
    }

    fun parseReturnURL(redirectUrl : String) {
        redirectUrl.apply {
            if (!contains("/token/") and !contains("/login/"))
                throw IllegalStateException("Redirect url ($redirectUrl) doesn't contain userData")
        }

        val login = redirectUrl
                .split("/token/").first()
                .substringAfterLast("/login/")
        val token = redirectUrl
                .split("/token/").last()
                .replace("/", "")
        userLoggedCallback.invoke(login, token)
    }
}
