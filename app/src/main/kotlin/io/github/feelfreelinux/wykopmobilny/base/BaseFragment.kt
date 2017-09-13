package io.github.feelfreelinux.wykopmobilny.base

import android.support.v4.app.Fragment
import com.github.salomonbrys.kodein.LazyKodein
import com.github.salomonbrys.kodein.android.appKodein
import io.github.feelfreelinux.wykopmobilny.ui.elements.dialogs.showExceptionDialog

abstract class BaseFragment : Fragment() {
    val kodein = LazyKodein(appKodein)

    fun showErrorDialog(e : Exception) =
            context.showExceptionDialog(e)
}