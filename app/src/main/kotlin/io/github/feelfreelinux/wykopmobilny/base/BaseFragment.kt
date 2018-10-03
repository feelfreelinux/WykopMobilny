package io.github.feelfreelinux.wykopmobilny.base

import dagger.android.support.DaggerFragment
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.showExceptionDialog

abstract class BaseFragment : DaggerFragment() {

    protected val supportFragmentManager by lazy { (activity as BaseActivity).supportFragmentManager }

    fun showErrorDialog(e: Throwable) {
        val baseActivity = activity as? BaseActivity
        if (activity != null && activity is BaseActivity && baseActivity!!.isRunning) {
            activity?.showExceptionDialog(e)
        }
    }
}