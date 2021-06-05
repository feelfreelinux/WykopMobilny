package io.github.feelfreelinux.wykopmobilny.ui.modules.profile

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent

class NoSwipeViewPager(context: Context, attributeSet: AttributeSet) : androidx.viewpager.widget.ViewPager(context, attributeSet) {

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }
}
