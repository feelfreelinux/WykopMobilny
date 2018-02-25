package io.github.feelfreelinux.wykopmobilny.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.adapter.AdvancedProgressAdapter
import io.github.feelfreelinux.wykopmobilny.base.adapter.SimpleBaseProgressAdapter
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.BaseLinkViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.LinkViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.RecyclableViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.SimpleLinkViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.widgets.link.linkitem.LinkItemPresenterFactory
import io.github.feelfreelinux.wykopmobilny.ui.widgets.link.linkitem.LinkItemWidget
import io.github.feelfreelinux.wykopmobilny.ui.widgets.link.linkitem.SimpleItemWidget
import io.github.feelfreelinux.wykopmobilny.utils.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.printout
import javax.inject.Inject

class LinkAdapter @Inject constructor(val settingsPreferencesApi : SettingsPreferencesApi, val linkItemPresenterFactory: LinkItemPresenterFactory) : AdvancedProgressAdapter<Link>() {
    companion object {
        val LINK_VIEWTYPE = 1
        val SIMPLE_LINK_VIEWTYPE = 2
    }

    override fun getItemViewType(position: Int): Int = when {
        dataset[position] == null -> AdvancedProgressAdapter.VIEWTYPE_PROGRESS
        else -> if (settingsPreferencesApi.linkSimpleList) SIMPLE_LINK_VIEWTYPE else LINK_VIEWTYPE
    }

    override fun createViewHolder(viewType: Int, parent: ViewGroup): RecyclerView.ViewHolder =
            when (viewType) {
                LINK_VIEWTYPE -> LinkViewHolder(LinkItemWidget.createView(parent.context), settingsPreferencesApi, linkItemPresenterFactory.create())
                else -> SimpleLinkViewHolder(SimpleItemWidget.createView(parent.context), settingsPreferencesApi, linkItemPresenterFactory.create())
            }

    override fun bindHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = dataset[position]!!
        if (holder is SimpleLinkViewHolder) holder.bindView(item)
        else (holder as? LinkViewHolder)?.bindView(item)
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder?) {
        (holder as? RecyclableViewHolder)?.cleanRecycled()
        super.onViewRecycled(holder)
    }

}
