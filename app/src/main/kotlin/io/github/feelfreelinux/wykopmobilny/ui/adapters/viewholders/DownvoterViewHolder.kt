package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.support.v7.widget.RecyclerView
import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Downvoter
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Upvoter
import io.github.feelfreelinux.wykopmobilny.ui.widgets.link.LinkPresenter
import kotlinx.android.synthetic.main.downvoters_list_item.view.*

class DownvoterViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    fun bindView(downvoter: Downvoter) {
        view.authorHeaderView.setAuthorData(downvoter.author, downvoter.date)
        view.reason_textview.text = when(downvoter.reason) {
            LinkPresenter.BURY_REASON_DUPLICATE -> view.resources.getString(R.string.reason_duplicate)
            LinkPresenter.BURY_REASON_SPAM -> view.resources.getString(R.string.reason_spam)
            LinkPresenter.BURY_REASON_FAKE_INFO -> view.resources.getString(R.string.reason_fake_info)
            LinkPresenter.BURY_REASON_WRONG_CONTENT -> view.resources.getString(R.string.reason_wrong_content)
            LinkPresenter.BURY_REASON_UNSUITABLE_CONTENT -> view.resources.getString(R.string.reason_unsuitable_content)
            else -> ""
        }
    }
}