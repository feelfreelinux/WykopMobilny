package io.github.feelfreelinux.wykopmobilny.base

import android.support.v4.app.Fragment
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.showExceptionDialog

abstract class BaseFragment : Fragment() {
    fun showErrorDialog(e : Throwable) =
            context.showExceptionDialog(e)
}