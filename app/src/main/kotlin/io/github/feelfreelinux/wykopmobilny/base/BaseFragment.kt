package io.github.feelfreelinux.wykopmobilny.base

import dagger.android.support.DaggerFragment
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.showExceptionDialog

abstract class BaseFragment : DaggerFragment() {
    val supportFragmentManager by lazy { (activity as BaseActivity).supportFragmentManager }
    fun showErrorDialog(e : Throwable) {
        activity?.showExceptionDialog(e)
    }
}