package io.github.feelfreelinux.wykopmobilny.ui.dialogs.votersdialog

import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Voter

interface VotersDialogView : BaseView {
    fun showVoters(voters : List<Voter>)
}