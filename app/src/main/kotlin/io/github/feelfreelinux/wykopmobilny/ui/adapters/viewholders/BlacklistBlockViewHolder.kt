package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.text.Editable
import android.text.Selection
import android.text.TextWatcher
import android.view.View
import io.github.feelfreelinux.wykopmobilny.api.suggest.SuggestApi
import io.github.feelfreelinux.wykopmobilny.ui.suggestions.HashTagsSuggestionsAdapter
import io.github.feelfreelinux.wykopmobilny.ui.suggestions.UsersSuggestionsAdapter
import io.github.feelfreelinux.wykopmobilny.ui.suggestions.WykopSuggestionsTokenizer
import kotlinx.android.synthetic.main.blacklist_block_form_item.view.*

class BlacklistBlockViewHolder(val view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

    fun bind(isUserView: Boolean, blockListener: (String) -> Unit, suggestApi: SuggestApi) {
        view.apply {
            val usersSuggestionAdapter = UsersSuggestionsAdapter(context, suggestApi)
            val hashTagsSuggestionAdapter = HashTagsSuggestionsAdapter(context, suggestApi)
            val prefix = if (isUserView) "@" else "#"
            blockUserEditText.hint = if (isUserView) "Zablokuj użytkownika" else "Zablokuj tag"
            lockImageView.setOnClickListener {
                blockListener(blockUserEditText.text.toString().removePrefix(prefix))
            }
            blockUserEditText.setText(prefix, false)
            Selection.setSelection(blockUserEditText.text, blockUserEditText.text.length)

            blockUserEditText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {
                    if (!s.toString().startsWith(prefix)) {
                        blockUserEditText.setText(prefix)
                        Selection.setSelection(blockUserEditText.text, blockUserEditText.text.length)

                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })

            blockUserEditText.setTokenizer(WykopSuggestionsTokenizer({
                if (blockUserEditText.adapter !is UsersSuggestionsAdapter)
                    blockUserEditText.setAdapter(usersSuggestionAdapter)
            }, {
                if (blockUserEditText.adapter !is HashTagsSuggestionsAdapter)
                    blockUserEditText.setAdapter(hashTagsSuggestionAdapter)
            }))
            blockUserEditText.threshold = 3
        }
    }

}