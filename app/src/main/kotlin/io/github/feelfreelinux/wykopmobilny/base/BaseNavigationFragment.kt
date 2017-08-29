package io.github.feelfreelinux.wykopmobilny.base

import android.graphics.drawable.Drawable
import io.github.feelfreelinux.wykopmobilny.ui.mainnavigation.MainNavigationInterface

abstract class BaseNavigationFragment : BaseFragment() {
    val navigation by lazy { activity as MainNavigationInterface }

    override fun onDetach() {
        super.onDetach()
        navigation.isRefreshing = false
        navigation.isLoading = false
    }

    fun resetToolbarOverlayIcon() {
        // Set overflow icon to default
        navigation.activityToolbar.apply {
            overflowIcon = tag as Drawable
            title = null
        }

    }
}