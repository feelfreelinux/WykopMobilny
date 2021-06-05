package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Downvoter
import io.github.feelfreelinux.wykopmobilny.utils.toPrettyDate
import kotlinx.android.synthetic.main.downvoters_list_item.view.*

class DownvoterViewHolder(val view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

    companion object {
        const val BURY_REASON_DUPLICATE = 1
        const val BURY_REASON_SPAM = 2
        const val BURY_REASON_FAKE_INFO = 3
        const val BURY_REASON_WRONG_CONTENT = 4
        const val BURY_REASON_UNSUITABLE_CONTENT = 5
    }

    fun bindView(downvoter: Downvoter) {
        view.authorHeaderView.setAuthorData(downvoter.author, downvoter.date.toPrettyDate())
        view.reason_textview.text = when (downvoter.reason) {
            BURY_REASON_DUPLICATE -> view.resources.getString(R.string.reason_duplicate)
            BURY_REASON_SPAM -> view.resources.getString(R.string.reason_spam)
            BURY_REASON_FAKE_INFO -> view.resources.getString(R.string.reason_fake_info)
            BURY_REASON_WRONG_CONTENT -> view.resources.getString(R.string.reason_wrong_content)
            BURY_REASON_UNSUITABLE_CONTENT -> view.resources.getString(R.string.reason_unsuitable_content)
            else -> ""
        }
    }
}
