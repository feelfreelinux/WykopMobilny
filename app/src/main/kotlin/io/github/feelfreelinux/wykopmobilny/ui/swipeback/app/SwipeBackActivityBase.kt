package io.github.feelfreelinux.wykopmobilny.ui.swipeback.app

import io.github.feelfreelinux.wykopmobilny.ui.swipeback.SwipeBackLayout

interface SwipeBackActivityBase {
    fun getSwipeBackLayout() : SwipeBackLayout?
    fun scrollToFinishActivity()
    fun setSwipeBackEnable(isEnabled : Boolean)
}