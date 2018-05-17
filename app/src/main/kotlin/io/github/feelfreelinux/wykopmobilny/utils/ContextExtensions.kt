package io.github.feelfreelinux.wykopmobilny.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.Uri
import android.support.annotation.ColorInt
import android.support.customtabs.CustomTabsIntent
import android.util.TypedValue
import android.view.inputmethod.InputMethodManager
import io.github.feelfreelinux.wykopmobilny.R
import android.R.attr.label
import android.content.ClipData
import android.content.ClipboardManager
import android.widget.Toast


fun Context.openBrowser(url : String) {
    // Start in-app browser, handled by Chrome Customs Tabs
    val builder = CustomTabsIntent.Builder()
    val customTabsIntent = builder.build()
    val typedValue = TypedValue()
    theme.resolveAttribute(R.attr.colorPrimaryDark, typedValue, true)
    builder.setToolbarColor(typedValue.data)
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

fun Context.copyText(text : String, label : String = "wykopmobilny") {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(label, text)
    clipboard.primaryClip = clip
    showToast(getString(R.string.text_copied))
}

fun Context.showToast(text : String, long : Boolean = false) {
    Toast.makeText(this, text, if (long) Toast.LENGTH_LONG else Toast.LENGTH_SHORT).show()
}
