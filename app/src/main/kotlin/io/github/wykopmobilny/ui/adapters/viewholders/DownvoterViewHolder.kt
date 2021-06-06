package io.github.wykopmobilny.ui.adapters.viewholders

import androidx.recyclerview.widget.RecyclerView
import io.github.wykopmobilny.R
import io.github.wykopmobilny.databinding.DownvotersListItemBinding
import io.github.wykopmobilny.models.dataclass.Downvoter
import io.github.wykopmobilny.utils.toPrettyDate

class DownvoterViewHolder(private val binding: DownvotersListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        const val BURY_REASON_DUPLICATE = 1
        const val BURY_REASON_SPAM = 2
        const val BURY_REASON_FAKE_INFO = 3
        const val BURY_REASON_WRONG_CONTENT = 4
        const val BURY_REASON_UNSUITABLE_CONTENT = 5
    }

    fun bindView(downvoter: Downvoter) {
        binding.authorHeaderView.setAuthorData(downvoter.author, downvoter.date.toPrettyDate())
        binding.reasonTextview.text = when (downvoter.reason) {
            BURY_REASON_DUPLICATE -> R.string.reason_duplicate
            BURY_REASON_SPAM -> R.string.reason_spam
            BURY_REASON_FAKE_INFO -> R.string.reason_fake_info
            BURY_REASON_WRONG_CONTENT -> R.string.reason_wrong_content
            BURY_REASON_UNSUITABLE_CONTENT -> R.string.reason_unsuitable_content
            else -> null
        }?.let(binding.root.resources::getString).orEmpty()
    }
}
