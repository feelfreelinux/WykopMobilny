package io.github.feelfreelinux.wykopmobilny.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.view.ViewCompat

@Suppress("unused") // Used in XML layout files
class ScrollAwareFABBehavior(context: Context, attrs: AttributeSet) :
    com.google.android.material.floatingactionbutton.FloatingActionButton.Behavior(context, attrs) {

    override fun onStartNestedScroll(
        coordinatorLayout: androidx.coordinatorlayout.widget.CoordinatorLayout,
        child: com.google.android.material.floatingactionbutton.FloatingActionButton,
        directTargetChild: View,
        target: View,
        nestedScrollAxes: Int,
        type: Int
    ): Boolean {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL || super.onStartNestedScroll(
            coordinatorLayout, child, directTargetChild, target,
            nestedScrollAxes, type
        )
    }

    override fun onNestedScroll(
        coordinatorLayout: androidx.coordinatorlayout.widget.CoordinatorLayout,
        child: com.google.android.material.floatingactionbutton.FloatingActionButton,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type)
        if (dyConsumed > 0 && child.visibility == View.VISIBLE) {
            child.hide(object : com.google.android.material.floatingactionbutton.FloatingActionButton.OnVisibilityChangedListener() {
                override fun onHidden(fab: com.google.android.material.floatingactionbutton.FloatingActionButton?) {
                    super.onHidden(fab)
                    child.visibility = View.INVISIBLE
                }
            })
        } else if (dyConsumed < 0 && child.visibility != View.VISIBLE) {
            child.show()
        }
    }
}