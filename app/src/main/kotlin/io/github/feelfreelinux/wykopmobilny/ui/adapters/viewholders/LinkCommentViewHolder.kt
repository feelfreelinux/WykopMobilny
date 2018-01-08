package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.View
import io.github.feelfreelinux.wykopmobilny.models.dataclass.LinkComment
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.utils.SettingsPreferencesApi
import kotlinx.android.synthetic.main.link_comment_layout.view.*
import kotlinx.android.synthetic.main.link_comment_list_item.view.*

class LinkCommentViewHolder(val view: View, val navigatorApi: NewNavigatorApi, val settingsPreferencesApi: SettingsPreferencesApi) : RecyclerView.ViewHolder(view) {
    fun bindView(comment : LinkComment) {
        val margin = if (comment.id != comment.parentId) 8f else 0f
        val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, margin, view.resources.displayMetrics)
        view.setPadding(px.toInt(), 0, 0, 0)
        view.linkComment.setLinkCommentData(comment, navigatorApi, settingsPreferencesApi)
    }
}