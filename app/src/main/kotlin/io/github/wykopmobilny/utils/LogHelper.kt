package io.github.wykopmobilny.utils

import android.util.Log
import io.github.wykopmobilny.BuildConfig

public var APP_TAG = "WykopAPI"

fun printout(msg: String) {
    if (BuildConfig.DEBUG) Log.v(APP_TAG, msg)
}

fun wykopLog(logger: (String, String, Throwable) -> Int, msg: String, th: Throwable) {
    logger(APP_TAG, msg, th)
}

fun wykopLog(logger: (String, String) -> Int, msg: String) {
    logger(APP_TAG, msg)
}
