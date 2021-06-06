package io.github.wykopmobilny.ui.adapters.viewholders

import androidx.recyclerview.widget.RecyclerView
import io.github.wykopmobilny.databinding.LinkRelatedListItemBinding
import io.github.wykopmobilny.models.dataclass.Related
import io.github.wykopmobilny.ui.widgets.link.related.RelatedWidgetPresenter
import io.github.wykopmobilny.utils.usermanager.UserManagerApi

class RelatedViewHolder(
    private val binding: LinkRelatedListItemBinding,
    private val userManagerApi: UserManagerApi,
    private val relatedWidgetPresenter: RelatedWidgetPresenter
) : RecyclerView.ViewHolder(binding.root) {

    fun bindView(related: Related) {
        binding.relatedView.setRelatedData(related, userManagerApi, relatedWidgetPresenter)
    }
}
