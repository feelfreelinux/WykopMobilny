package io.github.feelfreelinux.wykopmobilny.base

import android.view.View
import io.github.feelfreelinux.wykopmobilny.ui.modules.mainnavigation.MainNavigationInterface

abstract class BaseNavigationFragment : BaseFragment() {
    val navigation by lazy { activity as MainNavigationInterface }
    var fab : View? = null
}