package io.github.feelfreelinux.wykopmobilny.utils

import android.view.Gravity
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout

class ProgressBarHandler(val layout: ViewGroup) {
    private var progressBar : ProgressBar = ProgressBar(layout.context)

    init {
        progressBar.isIndeterminate = true
        progressBar.isVisible = true

        val params = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT)

        val relativeLayout = RelativeLayout(layout.context)
        relativeLayout.gravity = Gravity.CENTER
        relativeLayout.addView(progressBar)

        layout.addView(relativeLayout, params)
    }

    var isLoading : Boolean
        get() = progressBar.isVisible
        set(value) { progressBar.isVisible = value }
}