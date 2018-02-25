package io.github.feelfreelinux.wykopmobilny.ui.swipeback.app

import android.view.LayoutInflater
import android.graphics.drawable.ColorDrawable
import android.app.Activity
import android.graphics.Color
import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.ui.swipeback.SwipeBackLayout
import io.github.feelfreelinux.wykopmobilny.ui.swipeback.SwipeLayoutUtils


class SwipeBackActivityHelper(private val mActivity: Activity) {

    var swipeBackLayout: SwipeBackLayout? = null
        private set

    fun onActivityCreate() {
        mActivity.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mActivity.window.decorView.background = null
        swipeBackLayout = LayoutInflater.from(mActivity).inflate(
                R.layout.swipebacklayout, null) as SwipeBackLayout
        swipeBackLayout!!.addSwipeListener(object : SwipeBackLayout.SwipeListener {
            override fun onScrollStateChange(state: Int, scrollPercent: Float) {}

            override fun onEdgeTouch(edgeFlag: Int) {
                SwipeLayoutUtils.convertActivityToTranslucent(mActivity)
            }

            override fun onScrollOverThreshold() {}
        })
    }

    fun onPostCreate() {
        swipeBackLayout!!.attachToActivity(mActivity)
    }

    fun findViewById(id: Int): View? {
        return if (swipeBackLayout != null) {
            swipeBackLayout!!.findViewById(id)
        } else null
    }
}