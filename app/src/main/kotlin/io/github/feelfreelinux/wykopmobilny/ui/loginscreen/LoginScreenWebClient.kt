package io.github.feelfreelinux.wykopmobilny.ui.loginscreen

import android.annotation.TargetApi
import android.os.Build
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

typealias TokenUrlCallback = (url : String) -> Unit

class LoginActivityWebClient(private val tokenUrlCallback: TokenUrlCallback) : WebViewClient() {
    @Suppress("OverridingDeprecatedMember")
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        url?.let { tokenUrlCallback.invoke(it) }
        return false
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        request?.let { tokenUrlCallback.invoke(it.url.toString()) }
        return false
    }
}
