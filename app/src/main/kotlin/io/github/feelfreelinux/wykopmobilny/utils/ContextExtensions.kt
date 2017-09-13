package io.github.feelfreelinux.wykopmobilny.utils

import android.content.Context
import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import io.github.feelfreelinux.wykopmobilny.R

fun Context.openBrowser(url : String) {
    // Start in-app browser, handled by Chrome Customs Tabs
    val builder = CustomTabsIntent.Builder()
    val customTabsIntent = builder.build()
    builder.setToolbarColor(R.attr.colorPrimaryDark)
    customTabsIntent.launchUrl(this, Uri.parse(url))
}