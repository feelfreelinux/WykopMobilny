package io.github.feelfreelinux.wykopmobilny.base

import android.support.v4.app.Fragment
import io.github.feelfreelinux.wykopmobilny.utils.printout

abstract class BaseFragment : Fragment() {
    fun showErrorDialog(e : Throwable) =
        (activity as BaseActivity).showErrorDialog(e)
}