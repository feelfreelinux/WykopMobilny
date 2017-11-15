package io.github.feelfreelinux.wykopmobilny.base

import android.support.v4.app.Fragment
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.showExceptionDialog

abstract class BaseFragment : Fragment() {
    val supportFragmentManager by lazy { (activity as BaseActivity).supportFragmentManager }
    fun showErrorDialog(e : Throwable) =
            context.showExceptionDialog(e)
}