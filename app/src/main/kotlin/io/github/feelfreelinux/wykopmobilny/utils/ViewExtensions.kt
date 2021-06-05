package io.github.feelfreelinux.wykopmobilny.utils

import android.view.LayoutInflater
import android.view.View

internal val View.layoutInflater
    get() = LayoutInflater.from(context)
