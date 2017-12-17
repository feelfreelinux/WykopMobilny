package io.github.feelfreelinux.wykopmobilny.utils

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import io.github.feelfreelinux.wykopmobilny.R
import android.net.NetworkInfo
import android.net.ConnectivityManager
import android.view.inputmethod.InputMethodManager


fun Context.openBrowser(url : String) {
    // Start in-app browser, handled by Chrome Customs Tabs
    val builder = CustomTabsIntent.Builder()
    val customTabsIntent = builder.build()
    builder.setToolbarColor(R.attr.colorPrimaryDark)
    customTabsIntent.launchUrl(this, Uri.parse(url))
}

fun Activity.hideKeyboard() {
    val view = this.currentFocus
    if (view != null) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun Context.isConnectedToInternet(): Boolean {

    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val activeNetwork = cm.activeNetworkInfo

    return activeNetwork != null && activeNetwork.isConnected
}
