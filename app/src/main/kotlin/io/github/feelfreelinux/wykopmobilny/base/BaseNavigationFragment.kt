package io.github.feelfreelinux.wykopmobilny.base

import io.github.feelfreelinux.wykopmobilny.ui.mainnavigation.MainNavigationInterface

abstract class BaseNavigationFragment : BaseFragment() {
    val navigation by lazy { activity as MainNavigationInterface }
}