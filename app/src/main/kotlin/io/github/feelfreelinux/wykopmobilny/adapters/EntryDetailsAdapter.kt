package io.github.feelfreelinux.wykopmobilny.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.objects.EntryDetailsResponse


/**
 * Created by marcinmazepa on 27/07/2017.
 */
class EntryDetailsAdapter : RecyclerView.Adapter<EntryDetailsViewHolder>() {

    var entryList = emptyList<EntryDetailsResponse>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onBindViewHolder(holder: EntryDetailsViewHolder?, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryDetailsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_entry_layout, parent, false)
        return EntryDetailsViewHolder(view)
    }
}

class EntryDetailsViewHolder(view: View) : RecyclerView.ViewHolder(view){

    fun bindView(){

    }

}


