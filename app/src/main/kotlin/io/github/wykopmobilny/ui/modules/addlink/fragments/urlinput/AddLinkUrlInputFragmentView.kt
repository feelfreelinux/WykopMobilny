package io.github.wykopmobilny.ui.modules.addlink.fragments.urlinput

import io.github.wykopmobilny.api.responses.NewLinkResponse
import io.github.wykopmobilny.base.BaseView

interface AddLinkUrlInputFragmentView : BaseView {
    fun setLinkDraft(draft: NewLinkResponse)
    fun showDuplicatesLoading(visibility: Boolean)
}
