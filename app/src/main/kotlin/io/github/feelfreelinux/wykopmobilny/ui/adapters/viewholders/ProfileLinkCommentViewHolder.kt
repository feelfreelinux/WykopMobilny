package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.view.View
import io.github.feelfreelinux.wykopmobilny.glide.GlideApp
import io.github.feelfreelinux.wykopmobilny.models.dataclass.LinkComment
import io.github.feelfreelinux.wykopmobilny.ui.widgets.link.comment.LinkCommentPresenter
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi

class ProfileLinkCommentViewHolder(val view: View, val linkCommentPresenter: LinkCommentPresenter, val userManagerApi: UserManagerApi, val settingsPreferencesApi: SettingsPreferencesApi) : RecyclableViewHolder(view) {
    fun bindView(comment : LinkComment) {
    }

    override fun cleanRecycled() { 
    }
}