package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import androidx.recyclerview.widget.RecyclerView
import io.github.feelfreelinux.wykopmobilny.databinding.ObservedTagListItemBinding
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.ObservedTagResponse
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi

class ObservedTagViewHolder(
    private val binding: ObservedTagListItemBinding,
    private val navigatorApi: NewNavigatorApi
) : RecyclerView.ViewHolder(binding.root) {

    fun bindView(tag: ObservedTagResponse) {
        binding.blockedTextView.text = tag.tag
        binding.root.setOnClickListener { navigatorApi.openTagActivity(tag.tag.removePrefix("#")) }
    }
}
