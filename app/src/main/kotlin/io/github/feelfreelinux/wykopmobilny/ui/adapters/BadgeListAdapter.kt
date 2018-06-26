package io.github.feelfreelinux.wykopmobilny.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.adapter.SimpleBaseProgressAdapter
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Upvoter
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.responses.BadgeResponse
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.BadgeViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.UpvoterViewHolder
import javax.inject.Inject

class BadgeListAdapter : SimpleBaseProgressAdapter<BadgeViewHolder, BadgeResponse>() {
    override fun createViewHolder(parent: ViewGroup): BadgeViewHolder
            = BadgeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.badge_list_item, parent, false))

    override fun bindHolder(holder: BadgeViewHolder, position: Int) {
        holder.bindView(data[position])
    }
}
