package io.github.feelfreelinux.wykopmobilny.ui.modules.loginscreen

import android.annotation.TargetApi
import android.os.Build
import android.support.annotation.RequiresApi
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import io.github.feelfreelinux.wykopmobilny.utils.printout

typealias TokenUrlCallback = (url : String) -> Unit

class LoginActivityWebClient(private val tokenUrlCallback: TokenUrlCallback) : WebViewClient() {
    @Suppress("OverridingDeprecatedMember")
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        url?.let { tokenUrlCallback.invoke(it) }
        return false
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        request?.let {
            tokenUrlCallback.invoke(it.url.toString())
        }
        return false
    }
}
