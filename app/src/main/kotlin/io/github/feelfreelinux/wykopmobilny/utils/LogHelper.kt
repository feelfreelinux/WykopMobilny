package io.github.feelfreelinux.wykopmobilny.utils

import android.util.Log
import io.github.feelfreelinux.wykopmobilny.BuildConfig

fun printout(msg: String) {
    if (BuildConfig.DEBUG) Log.v("WykopAPI", msg)
}