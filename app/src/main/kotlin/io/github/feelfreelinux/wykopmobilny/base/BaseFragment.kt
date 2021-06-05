package io.github.feelfreelinux.wykopmobilny.base

import dagger.android.support.DaggerFragment
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.showExceptionDialog

abstract class BaseFragment : DaggerFragment() {

    protected val supportFragmentManager by lazy { (activity as BaseActivity).supportFragmentManager }

    fun showErrorDialog(e: Throwable) {
        val activity = activity
        if (activity is BaseActivity && activity.isRunning) {
            activity.showExceptionDialog(e)
        }
    }
}
