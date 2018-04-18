package io.github.feelfreelinux.wykopmobilny.ui.modules.addlink.fragments.urlinput

import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.responses.NewLinkResponse

interface AddLinkUrlInputFragmentView : BaseView {
    fun setLinkDraft(draft : NewLinkResponse)
    fun showDuplicatesLoading(visibility : Boolean)
}