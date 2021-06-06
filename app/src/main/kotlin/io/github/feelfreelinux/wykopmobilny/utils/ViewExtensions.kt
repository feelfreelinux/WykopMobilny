package io.github.feelfreelinux.wykopmobilny.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View

internal val View.layoutInflater
    get() = LayoutInflater.from(context)

internal val Context.layoutInflater
    get() = LayoutInflater.from(this)
