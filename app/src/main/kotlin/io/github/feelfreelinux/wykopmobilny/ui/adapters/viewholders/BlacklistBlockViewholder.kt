package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.blacklist_block_form_item.view.*

class BlacklistBlockViewholder(val view : View) : RecyclerView.ViewHolder(view) {
    fun bind(isUserView : Boolean, blockListener : (String) -> Unit) {
        view.apply {
            blockUserEditText.hint = if (isUserView) "Zablokuj użytkownika" else "Zablokuj tag"
            lockImageView.setOnClickListener {
                blockListener(blockUserEditText.text.toString())
            }
        }
    }

}