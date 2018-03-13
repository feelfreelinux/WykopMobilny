package io.github.feelfreelinux.wykopmobilny.ui.widgets

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.webkit.WebSettings
import android.webkit.WebView
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext

class TwitterWebView : WebView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    val html = "<!DOCTYPE html><html><script src=\"https://platform.twitter.com/widgets.js\"></script><script type=\"text/javascript\">function loadTweet(){var tweet=document.getElementById(\"tweet\"); twttr.widgets.createTweet( \"599202861751410688\", tweet,{conversation : 'none', cards : 'hidden', linkColor : '#cc0000', theme : 'dark'});}</script><body onload=\"loadTweet()\"><div class=\"tweet\" id=\"tweet\"></div></body></html>"

    init {

        settings.setAppCachePath(getActivityContext()!!.cacheDir.absolutePath)
        settings.setAppCacheEnabled(true)
        settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK

    }
    fun loadTweet(url : String) {
        val id = url.substringAfterLast("/status/").removeSuffix("/")
        settings.javaScriptEnabled = true
        loadData(html.replace("599202861751410688", id), "text/html; charset=UTF-8", "UTF-8")
        setBackgroundColor(Color.TRANSPARENT)
    }
}