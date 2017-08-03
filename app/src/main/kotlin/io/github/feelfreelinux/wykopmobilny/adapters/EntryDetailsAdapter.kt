package io.github.feelfreelinux.wykopmobilny.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.adapters.holders.EntryDetailsListItem
import io.github.feelfreelinux.wykopmobilny.adapters.holders.EntryDetailsViewHolder
import io.github.feelfreelinux.wykopmobilny.objects.Comment
import io.github.feelfreelinux.wykopmobilny.objects.Embed
import io.github.feelfreelinux.wykopmobilny.objects.Entry
import io.github.feelfreelinux.wykopmobilny.presenters.EntryDetailsPresenter
import io.github.feelfreelinux.wykopmobilny.utils.*
import kotlinx.android.synthetic.main.item_entry_layout.view.*
import org.ocpsoft.prettytime.PrettyTime
import java.util.*


class EntryDetailsAdapter(val presenter : EntryDetailsPresenter) : RecyclerView.Adapter<EntryDetailsViewHolder>() {
    override fun onBindViewHolder(holder: EntryDetailsViewHolder, position: Int) {
        presenter.onBindListItem(position, holder)
    }

    override fun getItemCount() = presenter.getItemsCount()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryDetailsViewHolder =
        EntryDetailsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_entry_layout, parent, false))

}
